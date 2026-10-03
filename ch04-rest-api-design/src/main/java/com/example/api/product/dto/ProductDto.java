// Example of a response DTO
package com.example.api.product.dto;

public record ProductDto(
    Long id,
    String name,
    double price,
    String description,
    String imageUrl
) {}
