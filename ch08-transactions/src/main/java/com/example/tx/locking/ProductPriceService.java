package com.example.tx.locking;

import com.example.tx.domain.Product;
import com.example.tx.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPriceService {

    private final ProductRepository productRepository;

    public ProductPriceService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Optimistic locking: @Version on Product makes a concurrent update fail with
    // ObjectOptimisticLockingFailureException instead of silently overwriting.
    @Transactional
    public Product changePrice(Product detachedProduct, double newPrice) {
        detachedProduct.setPrice(newPrice);
        return productRepository.save(detachedProduct);
    }

    // Pessimistic locking: SELECT ... FOR UPDATE keeps other writers out until commit.
    @Transactional
    public void reserveStock(Long productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId).orElseThrow();
        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
    }
}
