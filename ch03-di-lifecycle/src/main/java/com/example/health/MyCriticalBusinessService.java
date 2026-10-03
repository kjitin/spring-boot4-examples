package com.example.health;

import org.springframework.stereotype.Component;

// Assume MyCriticalBusinessService is a component that performs a critical business function
@Component
class MyCriticalBusinessService {
    public boolean isOperational() {
        // Simulate checking the operational status of a critical service
        return Math.random() > 0.05; // 95% chance of being operational
    }
}
