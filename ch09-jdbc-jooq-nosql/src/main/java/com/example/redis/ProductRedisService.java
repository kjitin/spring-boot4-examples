package com.example.redis;

import com.example.jdbc.ProductJdbcRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ProductRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheProduct(String productId, ProductJdbcRepository.Product product) {
        redisTemplate.opsForValue().set("product:" + productId, product, 1, TimeUnit.HOURS);
    }

    public ProductJdbcRepository.Product getCachedProduct(String productId) {
        return (ProductJdbcRepository.Product) redisTemplate.opsForValue().get("product:" + productId);
    }

    public void deleteCachedProduct(String productId) {
        redisTemplate.delete("product:" + productId);
    }
}
