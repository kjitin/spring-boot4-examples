package com.example.jpa.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

// Bidirectional OneToMany (Product knows about OrderItems, OrderItem knows about Product)
@Entity
public class Product {
    @Id @GeneratedValue private Long id;
    private String name;
    private double price;
    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<OrderItem> items = new ArrayList<>();

    protected Product() {}

    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setProduct(this);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public List<OrderItem> getItems() { return items; }
}
