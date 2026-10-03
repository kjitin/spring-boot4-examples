package com.example.tx.layered;

import java.util.List;

public record OrderDto(Long id, String customerId, List<Item> items) {
    public record Item(Long productId, String productName, int quantity) {}
}
