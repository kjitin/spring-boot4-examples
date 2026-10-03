package com.example.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Fallback controller for circuit breaker
@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public String productFallback() {
        return "Product service is currently unavailable. Please try again later.";
    }
}
