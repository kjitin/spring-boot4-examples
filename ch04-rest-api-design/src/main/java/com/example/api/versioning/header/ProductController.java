package com.example.api.versioning.header;

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

    @GetMapping(headers = "X-API-Version=1")
    public ResponseEntity<List<ProductDto>> getProductsV1() {
        return ResponseEntity.ok(productService.findAllV1());
    }

    @GetMapping(headers = "X-API-Version=2")
    public ResponseEntity<List<ProductDto>> getProductsV2() {
        return ResponseEntity.ok(productService.findAllV2());
    }
}
