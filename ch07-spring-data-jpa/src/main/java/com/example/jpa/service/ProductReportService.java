package com.example.jpa.service;

import com.example.jpa.domain.Product;
import com.example.jpa.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductReportService {

    private final ProductRepository productRepository;

    public ProductReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Service method that triggers the N+1 problem
    @Transactional(readOnly = true)
    public void printProductItems() {
        List<Product> products = productRepository.findAll(); // 1 query to get all products
        for (Product product : products) {
            // N queries, one for each product to get its items (reduced by @BatchSize on Product.items)
            System.out.println("Product: " + product.getName() + ", Items: " + product.getItems().size());
        }
    }

    // Same report without N+1, using JOIN FETCH
    @Transactional(readOnly = true)
    public void printProductItemsWithJoinFetch() {
        for (Product product : productRepository.findAllWithItems()) {
            System.out.println("Product: " + product.getName() + ", Items: " + product.getItems().size());
        }
    }
}
