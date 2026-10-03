package com.example.concurrency;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ConcurrencyTests {

    @Autowired NotificationService notificationService;
    @Autowired OrderProcessingService orderProcessingService;
    @Autowired CounterService counterService;
    @Autowired DataSource dataSource;

    @Test
    void asyncEmailReturnsImmediatelyAndRunsOnCustomPool(CapturedOutput output) {
        long start = System.nanoTime();
        notificationService.sendEmail("ada@example.com", "Hi", "Hello");
        assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(Duration.ofMillis(500));

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                assertThat(output).containsPattern("Email sent to ada@example.com in thread AsyncTask-\\d"));
    }

    @Test
    void asyncSmsReturnsFuture() throws Exception {
        CompletableFuture<String> result = notificationService.sendSms("+44123", "hi");
        assertThat(result.get(5, TimeUnit.SECONDS)).isEqualTo("SMS sent successfully to +44123");
    }

    @Test
    void parallelStepsAreCombined() throws Exception {
        long start = System.nanoTime();
        String result = orderProcessingService.processOrder("o-1").get(5, TimeUnit.SECONDS);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertThat(result).isEqualTo("Order o-1 processed successfully: Inventory OK for o-1, Payment successful for o-1");
        assertThat(elapsedMs).isLessThan(2000); // 1000ms + 1200ms steps ran in parallel
    }

    @Test
    void synchronizedCounterIsThreadSafe() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 10_000; i++) {
            pool.submit(counterService::increment);
        }
        pool.shutdown();
        assertThat(pool.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
        assertThat(counterService.getCount()).isEqualTo(10_000);
    }

    @Test
    void hikariPoolIsConfigured() {
        HikariDataSource hikari = (HikariDataSource) dataSource;
        assertThat(hikari.getMaximumPoolSize()).isEqualTo(10);
        assertThat(hikari.getMinimumIdle()).isEqualTo(2);
        assertThat(hikari.getConnectionTimeout()).isEqualTo(30000);
    }
}
