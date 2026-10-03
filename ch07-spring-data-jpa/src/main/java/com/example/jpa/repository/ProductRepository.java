package com.example.jpa.repository;

import com.example.jpa.domain.Product;
import com.example.jpa.projection.ProductNameAndPrice;
import com.example.jpa.projection.ProductSummaryDto;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Repository method (inherited from JpaRepository): List<Product> findAll();

    // Fixing N+1 with JOIN FETCH
    @Query("SELECT p FROM Product p JOIN FETCH p.items")
    List<Product> findAllWithItems();

    // Fixing N+1 with an entity graph
    @EntityGraph(attributePaths = {"items"})
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithItemsGraph();

    // Interface-based projection
    List<ProductNameAndPrice> findByNameContaining(String name);

    // DTO (class-based) projection
    @Query("SELECT new com.example.jpa.projection.ProductSummaryDto(p.name, p.price) FROM Product p WHERE p.category = :category")
    List<ProductSummaryDto> findByCategory(@Param("category") String category);

    // Query hints
    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("SELECT p FROM Product p")
    List<Product> findAllReadOnly();

    @QueryHints({@QueryHint(name = "org.hibernate.fetchSize", value = "50")})
    @Query("SELECT p FROM Product p")
    Stream<Product> streamAllProducts();

    // Bulk update instead of loading every entity
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category = :category")
    int updatePriceForCategory(@Param("category") String category, @Param("multiplier") double multiplier);

    // Native SQL when JPQL is not enough (window functions + CTE)
    @Query(value = "WITH ranked_products AS (SELECT p.*, ROW_NUMBER() OVER (PARTITION BY p.category ORDER BY p.price DESC) as rn FROM product p) SELECT * FROM ranked_products WHERE rn <= 3", nativeQuery = true)
    List<Product> findTop3ProductsPerCategory();
}
