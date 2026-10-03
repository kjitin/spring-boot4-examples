package com.example.api.product;

import com.example.api.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple in-memory implementation of the ProductService the book refers to
 * ("ProductDto and ProductService would be defined elsewhere").
 */
@Service
public class ProductService {

    private final Map<Long, ProductDto> products = new ConcurrentHashMap<>();
    private final Map<Long, String> categories = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public ProductService() {
        seed("Laptop", 1200.0, "electronics");
        seed("Mouse", 25.0, "electronics");
        seed("Desk", 300.0, "furniture");
    }

    private void seed(String name, double price, String category) {
        ProductDto created = create(new ProductDto(null, name, price, name + " description", null));
        categories.put(created.id(), category);
    }

    public List<ProductDto> findAll(int page, int size, String category, String sort) {
        String[] sortParts = sort.split(",");
        Comparator<ProductDto> comparator = switch (sortParts[0]) {
            case "price" -> Comparator.comparingDouble(ProductDto::price);
            case "id" -> Comparator.comparing(ProductDto::id);
            default -> Comparator.comparing(ProductDto::name);
        };
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return products.values().stream()
                .filter(p -> category == null || category.equals(categories.get(p.id())))
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    public Optional<ProductDto> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public ProductDto create(ProductDto dto) {
        long id = ids.incrementAndGet();
        ProductDto created = new ProductDto(id, dto.name(), dto.price(), dto.description(), dto.imageUrl());
        products.put(id, created);
        return created;
    }

    public ProductDto update(Long id, ProductDto dto) {
        ProductDto updated = new ProductDto(id, dto.name(), dto.price(), dto.description(), dto.imageUrl());
        products.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        products.remove(id);
        categories.remove(id);
    }

    // Used by the versioning examples
    public List<ProductDto> findAllV1() {
        return findAll(0, Integer.MAX_VALUE, null, "id,asc").stream()
                .map(p -> new ProductDto(p.id(), p.name(), p.price(), null, null))
                .toList();
    }

    public List<ProductDto> findAllV2() {
        return findAll(0, Integer.MAX_VALUE, null, "id,asc").stream()
                .map(p -> new ProductDto(p.id(), p.name(), p.price(), p.description(), "/images/" + p.id() + ".png"))
                .toList();
    }
}
