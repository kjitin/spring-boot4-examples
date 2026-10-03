package com.example.tx.propagation;

import com.example.tx.domain.Product;
import com.example.tx.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired private ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void deductStock(Long productId, int quantity) {
        // This method will join the transaction started by placeOrder.
        Product product = productRepository.findById(productId).orElseThrow();
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
