package com.example.tx.propagation;

import com.example.tx.domain.Product;
import com.example.tx.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductRepository productRepository;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Product> getAllProducts() {
        // If called from a transactional context, it joins. Otherwise, it runs without a transaction.
        return productRepository.findAll();
    }
}
