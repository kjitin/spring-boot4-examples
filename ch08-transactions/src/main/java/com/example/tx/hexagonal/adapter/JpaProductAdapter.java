package com.example.tx.hexagonal.adapter;

import com.example.tx.hexagonal.domain.Product;
import com.example.tx.hexagonal.port.ProductPort;
import com.example.tx.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    public JpaProductAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(p -> new Product(p.getId(), p.getName(), p.getPrice(), p.getStock()));
    }

    @Override
    public void save(Product product) {
        com.example.tx.domain.Product entity = productRepository.findById(product.getId()).orElseThrow();
        entity.setStock(product.getStock());
    }
}
