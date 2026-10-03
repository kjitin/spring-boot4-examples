package com.example.testing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
}
