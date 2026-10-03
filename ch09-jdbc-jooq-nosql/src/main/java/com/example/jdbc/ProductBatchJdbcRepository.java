package com.example.jdbc;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductBatchJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductBatchJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] batchInsertProducts(List<ProductJdbcRepository.Product> products) {
        String sql = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductJdbcRepository.Product product = products.get(i);
                ps.setString(1, product.name());
                ps.setDouble(2, product.price());
                ps.setString(3, product.description());
            }

            @Override
            public int getBatchSize() {
                return products.size();
            }
        });
    }

    public int[] batchUpdateProductPrices(List<ProductJdbcRepository.Product> products) {
        String sql = "UPDATE product SET price = ? WHERE id = ?";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductJdbcRepository.Product product = products.get(i);
                ps.setDouble(1, product.price());
                ps.setLong(2, product.id());
            }

            @Override
            public int getBatchSize() {
                return products.size();
            }
        });
    }
}
