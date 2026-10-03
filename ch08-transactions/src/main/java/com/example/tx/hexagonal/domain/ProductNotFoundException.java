package com.example.tx.hexagonal.domain;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product " + productId + " not found");
    }
}
