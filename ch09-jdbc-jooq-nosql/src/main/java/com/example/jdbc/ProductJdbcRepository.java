package com.example.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createProduct(Product product) {
        String sql = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, product.name(), product.price(), product.description());
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, name, price, description FROM product WHERE id = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new Product(rs.getLong("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("description")));
            }
            return Optional.empty();
        }, id);
    }

    public List<Product> findAll() {
        String sql = "SELECT id, name, price, description FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Product(rs.getLong("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("description"))
        );
    }

    public int updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.name(), product.price(), product.description(), product.id());
    }

    public int deleteProduct(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public record Product(Long id, String name, double price, String description) {}
}
