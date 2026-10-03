// Example of a request DTO
package com.example.api.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
    @NotBlank(message = "Product name is required")
    String name,
    @Positive(message = "Price must be positive")
    double price,
    String description
) {}
