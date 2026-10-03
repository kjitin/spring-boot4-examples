package com.example.api.versioning.separate;

import com.example.api.product.ProductService;
import com.example.api.product.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller {

    private final ProductService productService;

    public ProductV1Controller(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.findAllV1();
    }
}
