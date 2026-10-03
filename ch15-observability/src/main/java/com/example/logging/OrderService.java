package com.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void processOrder(String orderId, String userId) {
        MDC.put("orderId", orderId);
        MDC.put("userId", userId);
        try {
            log.info("Starting order processing for order {}", orderId);
            // Simulate some business logic
            if (Math.random() > 0.5) {
                log.debug("Order {} processed successfully.", orderId);
            } else {
                log.warn("Order {} encountered a minor issue.", orderId);
            }
            log.info("Finished order processing for order {}", orderId);
        } finally {
            MDC.remove("orderId");
            MDC.remove("userId");
        }
    }

    public void generateAndProcessRandomOrder() {
        String randomOrderId = UUID.randomUUID().toString();
        String randomUserId = "user-" + (int)(Math.random() * 1000);
        processOrder(randomOrderId, randomUserId);
    }
}
