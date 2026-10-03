package com.example.caching;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

// Same services, distributed cache: requires Docker (skipped otherwise)
@SpringBootTest
@ActiveProfiles("redis")
@Testcontainers(disabledWithoutDocker = true)
class RedisCachingTests {

    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @Autowired ProductService productService;
    @Autowired CacheManager cacheManager;
    @Autowired StringRedisTemplate redisTemplate;

    @Test
    void productsAreCachedInRedis() {
        assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
        productService.getProductById(1L);
        assertThat(redisTemplate.keys("products::*")).containsExactly("products::1");

        long start = System.nanoTime();
        assertThat(productService.getProductById(1L)).get().extracting(Product::getName).isEqualTo("Laptop");
        assertThat((System.nanoTime() - start) / 1_000_000).isLessThan(500);
    }
}
