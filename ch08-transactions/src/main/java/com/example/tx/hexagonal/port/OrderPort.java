package com.example.tx.hexagonal.port;

import com.example.tx.hexagonal.domain.Order;

// Ports (interfaces for domain interaction)
public interface OrderPort {
    void save(Order order);
}
