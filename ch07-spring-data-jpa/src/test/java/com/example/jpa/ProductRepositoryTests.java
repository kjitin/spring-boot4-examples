package com.example.jpa;

import com.example.jpa.domain.OrderItem;
import com.example.jpa.domain.Product;
import com.example.jpa.ids.User;
import com.example.jpa.projection.ProductNameAndPrice;
import com.example.jpa.projection.ProductSummaryDto;
import com.example.jpa.repository.ProductRepository;
import com.example.jpa.service.ProductReportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTests {

    @Autowired ProductRepository repository;
    @Autowired ProductReportService reportService;
    @Autowired EntityManagerFactory emf;
    @Autowired EntityManager entityManager;
    @Autowired TransactionTemplate tx;

    Statistics statistics;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        for (int i = 1; i <= 25; i++) {
            Product product = new Product("Product " + i, i * 10.0, i % 2 == 0 ? "books" : "electronics");
            product.addItem(new OrderItem("item-a-" + i));
            product.addItem(new OrderItem("item-b-" + i));
            repository.save(product);
        }
        statistics = emf.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
    }

    @Test
    void lazyLoadingIssuesExtraQueriesWhileJoinFetchUsesOne() {
        reportService.printProductItems();
        long lazyQueries = statistics.getPrepareStatementCount();
        statistics.clear();

        reportService.printProductItemsWithJoinFetch();
        long joinFetchQueries = statistics.getPrepareStatementCount();

        // 1 query for products + batched collection loads (@BatchSize(10) => 3 batches instead of 25 queries)
        assertThat(lazyQueries).isGreaterThan(1).isLessThan(26);
        assertThat(joinFetchQueries).isEqualTo(1);
    }

    @Test
    void entityGraphFetchesItemsInOneQuery() {
        List<Product> products = tx.execute(status -> {
            List<Product> result = repository.findAllWithItemsGraph();
            result.forEach(p -> p.getItems().size());
            return result;
        });
        assertThat(products).hasSize(25);
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void projections() {
        List<ProductNameAndPrice> byName = repository.findByNameContaining("Product 2");
        assertThat(byName).extracting(ProductNameAndPrice::getName)
                .contains("Product 2", "Product 20", "Product 25");

        List<ProductSummaryDto> books = repository.findByCategory("books");
        assertThat(books).hasSize(12).allSatisfy(dto -> assertThat(dto.getPrice()).isPositive());
    }

    @Test
    void queryHintsAndStreaming() {
        assertThat(repository.findAllReadOnly()).hasSize(25);
        long count = tx.execute(status -> {
            try (Stream<Product> stream = repository.streamAllProducts()) {
                return stream.count();
            }
        });
        assertThat(count).isEqualTo(25);
    }

    @Test
    void bulkUpdateAndNativeQuery() {
        int updated = tx.execute(status -> repository.updatePriceForCategory("books", 2.0));
        assertThat(updated).isEqualTo(12);
        assertThat(repository.findByCategory("books")).extracting(ProductSummaryDto::getPrice).contains(40.0);

        List<Product> top3 = repository.findTop3ProductsPerCategory();
        assertThat(top3).hasSize(6);
        assertThat(top3).extracting(Product::getName).contains("Product 25", "Product 24");
    }

    @Test
    void identifierStrategies() {
        tx.executeWithoutResult(status -> {
            com.example.jpa.ids.Product uuidProduct = new com.example.jpa.ids.Product();
            uuidProduct.setName("uuid");
            entityManager.persist(uuidProduct);
            User user = new User();
            user.setUsername("alice");
            entityManager.persist(user);
            entityManager.flush();
            assertThat(uuidProduct.getId()).isNotNull();
            assertThat(user.getId()).isNotNull();
        });
    }
}
