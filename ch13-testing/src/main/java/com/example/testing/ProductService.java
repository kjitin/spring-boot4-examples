package com.example.testing;

import org.springframework.stereotype.Service;

@Service
class ProductService {
    public boolean deductStock(String productId, int quantity) {
        // Real logic to deduct stock
        return true;
    }
}
