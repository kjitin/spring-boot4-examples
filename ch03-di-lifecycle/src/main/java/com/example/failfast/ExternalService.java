package com.example.failfast;

import org.springframework.stereotype.Component;

// Assume ExternalService is a dependency that checks connectivity to an external system
@Component
class ExternalService {
    public boolean isAvailable() {
        // Simulate a check
        return Math.random() > 0.1; // 90% chance of being available
    }
}
