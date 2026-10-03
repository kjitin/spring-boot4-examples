package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private final Counter paymentSuccessCounter;
    private final Counter paymentFailureCounter;
    private final Timer paymentProcessingTimer;

    public PaymentService(MeterRegistry meterRegistry) {
        this.paymentSuccessCounter = Counter.builder("payment.processed.total")
                .tag("status", "success")
                .description("Total number of successful payments")
                .register(meterRegistry);
        this.paymentFailureCounter = Counter.builder("payment.processed.total")
                .tag("status", "failure")
                .description("Total number of failed payments")
                .register(meterRegistry);
        this.paymentProcessingTimer = Timer.builder("payment.processing.time")
                .description("Time taken to process payments")
                .publishPercentiles(0.5, 0.95, 0.99) // Publish 50th, 95th, 99th percentiles
                .publishPercentileHistogram() // Publish histogram buckets
                .minimumExpectedValue(Duration.ofMillis(10))
                .maximumExpectedValue(Duration.ofSeconds(5))
                .register(meterRegistry);
    }

    public boolean processPayment(double amount) {
        long startTime = System.nanoTime();
        try {
            // Simulate payment processing logic
            Thread.sleep(new Random().nextInt(1000) + 100); // 100ms to 1100ms
            if (amount > 1000 && new Random().nextBoolean()) {
                throw new RuntimeException("Payment failed for large amount");
            }
            paymentSuccessCounter.increment();
            return true;
        } catch (Exception e) {
            paymentFailureCounter.increment();
            return false;
        } finally {
            paymentProcessingTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        }
    }
}
