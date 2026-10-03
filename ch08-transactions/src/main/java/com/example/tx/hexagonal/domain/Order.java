package com.example.tx.hexagonal.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private OrderId id;
    private final String customerId;
    private final List<Line> lines = new ArrayList<>();

    public record Line(Product product, int quantity) {}

    private Order(String customerId) {
        this.customerId = customerId;
    }

    public static Order create(String customerId) {
        return new Order(customerId);
    }

    public void addItem(Product product, int quantity) {
        lines.add(new Line(product, quantity));
    }

    public double getTotalAmount() {
        return lines.stream().mapToDouble(l -> l.product().getPrice() * l.quantity()).sum();
    }

    public void assignId(OrderId id) { this.id = id; }
    public OrderId getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<Line> getLines() { return lines; }
}
