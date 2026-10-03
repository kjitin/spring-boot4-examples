package com.example.inventoryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/*
 * Changed from the book: @StreamListener and @Input binding interfaces were removed in Spring Cloud Stream 4.
 * A Consumer bean is bound automatically as "handleOrderEvent-in-0"; application.yml maps that binding to
 * the book's "order-in" binding name.
 */
@Configuration
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @Bean
    public Consumer<String> handleOrderEvent() {
        return eventPayload -> {
            log.info("Received order event: {}", eventPayload);
            // Process the order event, e.g., deduct stock
        };
    }
}
