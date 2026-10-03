package com.example.orderservice;

import com.example.resilience.ProductServiceClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

// No Consul, Kafka or product-service needed: Consul is disabled, the in-memory test binder replaces Kafka,
// and nothing listens on localhost:8082 so every product call fails and exercises the circuit breaker.
@SpringBootTest(properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.stream.default-binder=integration"
})
@Import(TestChannelBinderConfiguration.class)
class OrderServiceApplicationTests {

    @Autowired OrderService orderService;
    @Autowired OutputDestination output;
    @Autowired ProductServiceClient productServiceClient;
    @Autowired CircuitBreakerRegistry circuitBreakerRegistry;

    @Test
    void publishesOrderPlacedEvent() {
        orderService.placeOrder("1 x Laptop");

        Message<byte[]> message = output.receive(1000, "order-events");
        assertThat(message).isNotNull();
        assertThat(new String(message.getPayload())).isEqualTo("{\"orderId\":\"order-123\", \"status\":\"PLACED\"}");
    }

    @Test
    void circuitBreakerFallsBackAndOpens() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("productService");
        for (int i = 0; i < 10; i++) {
            assertThat(productServiceClient.getProducts()).isEqualTo("[]");
        }
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}
