package com.example.tx.hexagonal.port;

import com.example.tx.hexagonal.domain.Product;

import java.util.Optional;

public interface ProductPort {
    Optional<Product> findById(Long id);
    void save(Product product);
}
