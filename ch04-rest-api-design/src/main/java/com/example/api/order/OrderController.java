package com.example.api.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody OrderDto orderDto) {

        if (idempotencyKey != null) {
            Optional<OrderDto> existingOrder = orderService.findByIdempotencyKey(idempotencyKey);
            if (existingOrder.isPresent()) {
                // Return the previously created order to ensure idempotency
                return ResponseEntity.ok(existingOrder.get());
            }
        }

        OrderDto createdOrder = orderService.createOrder(orderDto, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
