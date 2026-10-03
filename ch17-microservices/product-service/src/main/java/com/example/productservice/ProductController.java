package com.example.productservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    // Comes from the Config Server (config-repo/config-data/product-service.yml) when it is running
    @Value("${product.greeting:Hello from local configuration}")
    private String greeting;

    // "/" is what the gateway forwards to after StripPrefix=2 removes /api/products
    @GetMapping({"/", "/products"})
    public List<String> products() {
        return List.of("Laptop", "Mouse", "Keyboard");
    }

    @GetMapping("/greeting")
    public String greeting() {
        return greeting;
    }
}
