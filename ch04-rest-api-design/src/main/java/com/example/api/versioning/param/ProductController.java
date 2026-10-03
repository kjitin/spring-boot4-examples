package com.example.api.versioning.param;

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

    @GetMapping(params = "version=1")
    public ResponseEntity<List<ProductDto>> getProductsByQueryParamV1() {
        return ResponseEntity.ok(productService.findAllV1());
    }

    @GetMapping(params = "version=2")
    public ResponseEntity<List<ProductDto>> getProductsByQueryParamV2() {
        return ResponseEntity.ok(productService.findAllV2());
    }
}
