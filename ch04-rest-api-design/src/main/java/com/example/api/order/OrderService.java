package com.example.api.order;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final Map<String, OrderDto> ordersByIdempotencyKey = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public Optional<OrderDto> findByIdempotencyKey(String idempotencyKey) {
        return Optional.ofNullable(ordersByIdempotencyKey.get(idempotencyKey));
    }

    public OrderDto createOrder(OrderDto orderDto, String idempotencyKey) {
        OrderDto created = new OrderDto(ids.incrementAndGet(), orderDto.productName(), orderDto.quantity());
        if (idempotencyKey != null) {
            // putIfAbsent protects against two concurrent requests with the same key
            OrderDto existing = ordersByIdempotencyKey.putIfAbsent(idempotencyKey, created);
            if (existing != null) {
                return existing;
            }
        }
        return created;
    }
}
