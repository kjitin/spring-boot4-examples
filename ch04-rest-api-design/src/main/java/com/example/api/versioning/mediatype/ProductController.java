package com.example.api.versioning.mediatype;

import com.example.api.product.ProductService;
import com.example.api.product.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = "application/vnd.company.app-v1+json")
    public ResponseEntity<List<ProductDto>> getProductsV1ByAcceptHeader() {
        return ResponseEntity.ok(productService.findAllV1());
    }

    @GetMapping(produces = "application/vnd.company.app-v2+json")
    public ResponseEntity<List<ProductDto>> getProductsV2ByAcceptHeader() {
        return ResponseEntity.ok(productService.findAllV2());
    }
}
