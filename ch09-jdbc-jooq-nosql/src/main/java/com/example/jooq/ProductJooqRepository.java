package com.example.jooq;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jooq.generated.tables.Product.PRODUCT;

@Repository
public class ProductJooqRepository {

    private final DSLContext dslContext;

    public ProductJooqRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<ProductRecord> findProductsWithHighPrice(double minPrice) {
        return dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.PRICE.greaterThan(minPrice))
                .orderBy(PRODUCT.PRICE.desc())
                .fetchInto(ProductRecord.class);
    }

    public record ProductRecord(Long id, String name, double price, String description) {}
}
