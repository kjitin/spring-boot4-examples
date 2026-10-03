package com.example.circular;

import org.springframework.stereotype.Component;

// Example of a circular dependency (will fail with constructor injection)
@Component
public class ServiceA {
    private final ServiceB serviceB;
    public ServiceA(ServiceB serviceB) { this.serviceB = serviceB; }
}
