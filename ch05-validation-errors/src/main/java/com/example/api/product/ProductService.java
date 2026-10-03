package com.example.api.product;

import com.example.api.product.dto.CreateProductRequest;
import com.example.api.product.dto.ProductDto;
import com.example.api.product.dto.ProductRequest;
import com.example.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, ProductDto> products = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public ProductDto create(CreateProductRequest request) {
        return save(new ProductDto(ids.incrementAndGet(), request.name(), request.price(), request.description()));
    }

    public ProductDto create(ProductRequest request) {
        return save(new ProductDto(ids.incrementAndGet(), request.name(), request.price(), request.description()));
    }

    public ProductDto update(Long id, ProductRequest request) {
        findById(id);
        return save(new ProductDto(id, request.name(), request.price(), request.description()));
    }

    public ProductDto findById(Long id) {
        ProductDto product = products.get(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        return product;
    }

    private ProductDto save(ProductDto product) {
        products.put(product.id(), product);
        return product;
    }
}
