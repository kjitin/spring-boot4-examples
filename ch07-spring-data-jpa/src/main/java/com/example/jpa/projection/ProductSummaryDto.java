package com.example.jpa.projection;

public class ProductSummaryDto {
    private final String name;
    private final double price;

    public ProductSummaryDto(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
}
