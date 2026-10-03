package com.example.api.versioning.uri;

import com.example.api.product.ProductService;
import com.example.api.product.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/{version}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String version) {
        if ("v1".equals(version)) {
            // Logic for V1
            return ResponseEntity.ok(productService.findAllV1());
        } else if ("v2".equals(version)) {
            // Logic for V2
            return ResponseEntity.ok(productService.findAllV2());
        }
        return ResponseEntity.badRequest().build();
    }
}
