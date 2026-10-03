package com.example.tx.layered;

import com.example.tx.domain.Order;

public final class OrderMapper {

    private OrderMapper() {}

    public static OrderDto toDto(Order order) {
        return new OrderDto(order.getId(), order.getCustomerId(), order.getItems().stream()
                .map(i -> new OrderDto.Item(i.getProduct().getId(), i.getProduct().getName(), i.getQuantity()))
                .toList());
    }
}
