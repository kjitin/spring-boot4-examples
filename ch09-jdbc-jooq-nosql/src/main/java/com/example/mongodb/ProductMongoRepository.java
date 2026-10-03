package com.example.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByTagsContaining(String tag);
    List<ProductDocument> findByPriceGreaterThan(double price);
}
