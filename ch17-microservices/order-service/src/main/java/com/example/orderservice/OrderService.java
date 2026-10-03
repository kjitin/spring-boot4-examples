package com.example.orderservice;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/*
 * Changed from the book: the annotation-based Spring Cloud Stream model (@Output/@Input/@StreamListener and
 * the OrderEventProducer/OrderEventConsumer binding interfaces) was removed in Spring Cloud Stream 4.
 * Producers now use StreamBridge; the "order-out" binding name and its configuration are unchanged.
 */
@Service
public class OrderService {

    public static final String OUTPUT = "order-out";

    private final StreamBridge streamBridge;

    public OrderService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void placeOrder(String orderDetails) {
        // ... business logic to place order
        String orderId = "order-123";
        String eventPayload = "{\"orderId\":\"" + orderId + "\", \"status\":\"PLACED\"}";
        streamBridge.send(OUTPUT, MessageBuilder.withPayload(eventPayload).build());
    }
}
