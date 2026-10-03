package com.example.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();

    public ProductRepository() {
        products.put(1L, new Product(1L, "Laptop", 1200.0));
        products.put(2L, new Product(2L, "Mouse", 25.0));
        products.put(3L, new Product(3L, "Keyboard", 75.0));
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }
}
