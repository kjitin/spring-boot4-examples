package com.example.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class OrderProcessingService {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingService.class);
    private final Executor taskExecutor;

    public OrderProcessingService(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<String> processOrder(String orderId) {
        log.info("Starting order processing for {} in thread {}", orderId, Thread.currentThread().getName());

        CompletableFuture<String> inventoryCheck = CompletableFuture.supplyAsync(() -> {
            log.info("Checking inventory for {} in thread {}", orderId, Thread.currentThread().getName());
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Inventory OK for " + orderId;
        }, taskExecutor);

        CompletableFuture<String> paymentProcess = CompletableFuture.supplyAsync(() -> {
            log.info("Processing payment for {} in thread {}", orderId, Thread.currentThread().getName());
            try { Thread.sleep(1200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Payment successful for " + orderId;
        }, taskExecutor);

        return CompletableFuture.allOf(inventoryCheck, paymentProcess)
                .thenApplyAsync(v -> {
                    String inventoryResult = inventoryCheck.join();
                    String paymentResult = paymentProcess.join();
                    log.info("Order {} fully processed. Results: {}, {} in thread {}", orderId, inventoryResult, paymentResult, Thread.currentThread().getName());
                    return "Order " + orderId + " processed successfully: " + inventoryResult + ", " + paymentResult;
                }, taskExecutor)
                .exceptionally(ex -> {
                    log.error("Error processing order {}: {}", orderId, ex.getMessage());
                    return "Order " + orderId + " failed: " + ex.getMessage();
                });
    }
}
