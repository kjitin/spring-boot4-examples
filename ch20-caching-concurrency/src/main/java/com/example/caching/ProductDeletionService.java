package com.example.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ProductDeletionService {

    private static final Logger log = LoggerFactory.getLogger(ProductDeletionService.class);

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID {} from database and evicting from cache...", id);
        // Simulate database deletion
        // productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearAllProductsCache() {
        log.info("Clearing all entries from 'products' cache.");
    }
}
