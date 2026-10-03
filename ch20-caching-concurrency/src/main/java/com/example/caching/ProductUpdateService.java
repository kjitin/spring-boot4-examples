package com.example.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ProductUpdateService.class);
    private final ProductRepository productRepository;

    public ProductUpdateService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        log.info("Updating product with ID {} in database...", product.getId());
        // Simulate database update
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // In a real scenario, you would update the actual repository
        // For this example, we'll just return the product to be cached
        return product;
    }
}
