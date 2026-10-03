package com.example;

import com.example.jdbc.ProductBatchJdbcRepository;
import com.example.jdbc.ProductJdbcRepository;
import com.example.jdbc.ProductJdbcRepository.Product;
import com.example.jooq.ProductJooqRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcAndJooqTests {

    @Autowired ProductJdbcRepository jdbcRepository;
    @Autowired ProductBatchJdbcRepository batchRepository;
    @Autowired ProductJooqRepository jooqRepository;
    @Autowired JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clean() {
        jdbcTemplate.update("DELETE FROM product");
    }

    @Test
    void jdbcTemplateCrud() {
        jdbcRepository.createProduct(new Product(null, "Laptop", 1200.0, "Fast"));
        Product saved = jdbcRepository.findAll().getFirst();
        assertThat(jdbcRepository.findById(saved.id())).contains(saved);

        assertThat(jdbcRepository.updateProduct(new Product(saved.id(), "Laptop Pro", 1500.0, "Faster"))).isEqualTo(1);
        assertThat(jdbcRepository.findById(saved.id()).orElseThrow().name()).isEqualTo("Laptop Pro");

        assertThat(jdbcRepository.deleteProduct(saved.id())).isEqualTo(1);
        assertThat(jdbcRepository.findById(saved.id())).isEmpty();
    }

    @Test
    void batchOperations() {
        int[] inserted = batchRepository.batchInsertProducts(List.of(
                new Product(null, "A", 10, null), new Product(null, "B", 20, null), new Product(null, "C", 30, null)));
        assertThat(inserted).containsExactly(1, 1, 1);

        List<Product> repriced = jdbcRepository.findAll().stream()
                .map(p -> new Product(p.id(), p.name(), p.price() * 2, p.description())).toList();
        batchRepository.batchUpdateProductPrices(repriced);
        assertThat(jdbcRepository.findAll()).extracting(Product::price).containsExactlyInAnyOrder(20.0, 40.0, 60.0);
    }

    @Test
    void jooqTypeSafeQuery() {
        batchRepository.batchInsertProducts(List.of(
                new Product(null, "Cheap", 5, null), new Product(null, "Mid", 50, null), new Product(null, "Premium", 500, "Top")));
        List<ProductJooqRepository.ProductRecord> expensive = jooqRepository.findProductsWithHighPrice(10);
        assertThat(expensive).extracting(ProductJooqRepository.ProductRecord::name).containsExactly("Premium", "Mid");
        assertThat(expensive.getFirst().description()).isEqualTo("Top");
    }
}
