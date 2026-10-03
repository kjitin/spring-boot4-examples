package com.example.testing;

import org.springframework.stereotype.Service;

@Service
class OrderService {
    private final ProductService productService;

    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    public boolean placeOrder(String productId, int quantity) {
        // Some order logic
        return productService.deductStock(productId, quantity);
    }
}
