package com.example.production;

import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.OpenTelemetry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.metrics.test.autoconfigure.AutoConfigureMetrics;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataResponse;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// "aws" profile with a mocked CloudWatch client: no AWS account or collector needed
@SpringBootTest
@ActiveProfiles("aws")
@AutoConfigureMetrics
@ExtendWith(OutputCaptureExtension.class)
class ProductionConfigTests {

    @MockitoBean
    CloudWatchAsyncClient cloudWatch;

    @Autowired MeterRegistry meterRegistry;
    @Autowired CloudWatchMeterRegistry cloudWatchMeterRegistry;
    @Autowired OpenTelemetry openTelemetry;

    @Test
    void metricsArePublishedToCloudWatchNamespace() {
        when(cloudWatch.putMetricData(any(PutMetricDataRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(PutMetricDataResponse.builder().build()));
        assertThat(((CompositeMeterRegistry) meterRegistry).getRegistries()).contains(cloudWatchMeterRegistry);

        meterRegistry.counter("orders.placed").increment();
        cloudWatchMeterRegistry.close(); // flushes the current step

        verify(cloudWatch, atLeastOnce()).putMetricData(org.mockito.ArgumentMatchers.<PutMetricDataRequest>argThat(
                request -> request.namespace().equals("MySpringBootApp")));
    }

    @Test
    void traceIdsAreXRayCompatible() {
        Tracer tracer = openTelemetry.getTracer("test");
        Span span = tracer.spanBuilder("op").startSpan();
        String traceId = span.getSpanContext().getTraceId();
        span.end();

        // X-Ray trace IDs start with the epoch seconds of the request (8 hex digits)
        long epochSeconds = Long.parseLong(traceId.substring(0, 8), 16);
        assertThat(epochSeconds).isCloseTo(System.currentTimeMillis() / 1000, org.assertj.core.data.Offset.offset(60L));
    }

    @Test
    void logsAreJson(CapturedOutput output) {
        org.slf4j.LoggerFactory.getLogger(ProductionConfigTests.class).info("structured hello");
        assertThat(output.getOut()).contains("\"message\":\"structured hello\"").contains("\"level\":\"INFO\"");
    }
}
