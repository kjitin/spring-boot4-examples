package com.example.caching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CachingTests {

    @Autowired ProductService productService;
    @Autowired ProductUpdateService productUpdateService;
    @Autowired ProductDeletionService productDeletionService;
    @Autowired CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        productDeletionService.clearAllProductsCache();
    }

    static long timeMillis(Runnable action) {
        long start = System.nanoTime();
        action.run();
        return (System.nanoTime() - start) / 1_000_000;
    }

    @Test
    void usesCaffeine() {
        assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);
    }

    @Test
    void cacheableSkipsTheSlowLookupOnSecondCall() {
        assertThat(timeMillis(() -> productService.getProductById(1L))).isGreaterThanOrEqualTo(1000);
        assertThat(timeMillis(() -> productService.getProductById(1L))).isLessThan(200);
        assertThat(productService.getProductById(1L)).get().extracting(Product::getName).isEqualTo("Laptop");
    }

    @Test
    void cachePutRefreshesTheEntry() {
        productService.getProductById(2L);
        productUpdateService.updateProduct(new Product(2L, "Wireless Mouse", 35.0));

        // Served from the cache: the repository still holds "Mouse"
        assertThat(productService.getProductById(2L)).get().extracting(Product::getName).isEqualTo("Wireless Mouse");
    }

    @Test
    void cacheEvictRemovesTheEntry() {
        productService.getProductById(3L);
        productDeletionService.deleteProduct(3L);
        assertThat(cacheManager.getCache("products").get(3L)).isNull();
        assertThat(timeMillis(() -> productService.getProductById(3L))).isGreaterThanOrEqualTo(1000);
    }
}
