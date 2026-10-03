package com.example;

import com.example.jdbc.ProductJdbcRepository.Product;
import com.example.mongodb.ProductDocument;
import com.example.mongodb.ProductMongoRepository;
import com.example.redis.ProductRedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Requires Docker: MongoDB and Redis run in Testcontainers (skipped when Docker is not available)
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class NoSqlTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:8");

    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @Autowired ProductMongoRepository mongoRepository;
    @Autowired ProductRedisService redisService;

    @Test
    void mongoDerivedQueries() {
        mongoRepository.deleteAll();
        mongoRepository.saveAll(List.of(
                new ProductDocument("Laptop", 1200, "Fast", List.of("electronics", "computers")),
                new ProductDocument("Novel", 15, "Good read", List.of("books")),
                new ProductDocument("Phone", 800, "Smart", List.of("electronics"))));

        assertThat(mongoRepository.findByTagsContaining("electronics"))
                .extracting(ProductDocument::getName).containsExactlyInAnyOrder("Laptop", "Phone");
        assertThat(mongoRepository.findByPriceGreaterThan(500))
                .extracting(ProductDocument::getName).containsExactlyInAnyOrder("Laptop", "Phone");
    }

    @Test
    void redisCaching() {
        Product product = new Product(1L, "Laptop", 1200.0, "Fast");
        redisService.cacheProduct("1", product);
        assertThat(redisService.getCachedProduct("1")).isEqualTo(product);

        redisService.deleteCachedProduct("1");
        assertThat(redisService.getCachedProduct("1")).isNull();
    }
}
