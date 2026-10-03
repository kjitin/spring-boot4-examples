package com.example;

import com.example.logging.OrderService;
import com.example.metrics.PaymentService;
import com.example.tracing.ProductService;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.SpanData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.metrics.test.autoconfigure.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "management.tracing.export.zipkin.enabled=false")
@AutoConfigureMockMvc
@AutoConfigureMetrics
@ExtendWith(OutputCaptureExtension.class)
class ObservabilityTests {

    @TestConfiguration
    static class InMemoryTracing {
        // Picked up by Boot's OpenTelemetry auto-configuration alongside the Zipkin exporter
        @Bean
        InMemorySpanExporter inMemorySpanExporter() {
            return InMemorySpanExporter.create();
        }
    }

    @Autowired OrderService orderService;
    @Autowired PaymentService paymentService;
    @Autowired ProductService productService;
    @Autowired InMemorySpanExporter spanExporter;
    @Autowired SdkTracerProvider tracerProvider;
    @Autowired MockMvc mockMvc;

    @Test
    void logsAreJsonAndCarryMdcValues(CapturedOutput output) {
        orderService.processOrder("order-42", "user-7");

        assertThat(output.getOut()).contains("\"message\":\"Starting order processing for order order-42\"");
        assertThat(output.getOut()).contains("\"orderId\":\"order-42\"").contains("\"userId\":\"user-7\"");
        assertThat(MDC.get("orderId")).isNull(); // cleaned up in the finally block
    }

    @Test
    void paymentMetricsAreExposedToPrometheus() throws Exception {
        paymentService.processPayment(10);
        paymentService.processPayment(20);

        String scrape = mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(scrape).contains("payment_processed_total{application=\"my-spring-app\",status=\"success\"} 2.0");
        assertThat(scrape).contains("payment_processing_time_seconds_count");
        // With publishPercentileHistogram() the Prometheus registry exports histogram buckets and ignores the
        // client-side publishPercentiles(); compute p95 in PromQL with histogram_quantile(0.95, ...)
        assertThat(scrape).contains("payment_processing_time_seconds_bucket{application=\"my-spring-app\",le=\"+Inf\"} 2");
    }

    @Test
    void customSpansAreRecorded() {
        assertThat(productService.getProductDetails("p-1")).isEqualTo("Details for product p-1");
        tracerProvider.forceFlush().join(5, java.util.concurrent.TimeUnit.SECONDS);

        List<SpanData> spans = spanExporter.getFinishedSpanItems();
        SpanData span = spans.stream().filter(s -> s.getName().equals("getProductDetails")).findFirst().orElseThrow();
        assertThat(span.getAttributes().asMap().values()).contains("p-1");
        assertThat(span.getEvents()).extracting(EventData::getName)
                .containsExactly("Fetching product from database", "Product fetched");
    }
}
