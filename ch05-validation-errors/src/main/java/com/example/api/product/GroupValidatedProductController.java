package com.example.api.product;

import com.example.api.product.dto.ProductDto;
import com.example.api.product.dto.ProductRequest;
import com.example.validation.groups.OnCreate;
import com.example.validation.groups.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Validation-group variant of the product endpoints (mapped under /api/v2 so it can live
// alongside the @Valid example above).
@RestController
@RequestMapping("/api/v2/products")
public class GroupValidatedProductController {

    private final ProductService productService;

    public GroupValidatedProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Validated(OnCreate.class) @RequestBody ProductRequest request) {
        ProductDto createdProduct = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody ProductRequest request) {
        ProductDto updatedProduct = productService.update(id, request);
        return ResponseEntity.ok(updatedProduct);
    }
}
