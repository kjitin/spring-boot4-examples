package com.example.resilience;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getFallbackProducts")
    public String getProducts() {
        // Simulate calling a potentially failing external product service
        return restTemplate.getForObject("http://localhost:8082/products", String.class);
    }

    private String getFallbackProducts(Throwable t) {
        // Fallback logic when the circuit breaker is open or an exception occurs
        return "[]"; // Return an empty list or cached data
    }
}
