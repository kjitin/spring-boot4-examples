package com.example.tx.hexagonal;

import java.util.List;

public class CreateOrderCommand {
    private final String customerId;
    private final List<ItemCommand> items;
    private final String paymentInfo;

    public CreateOrderCommand(String customerId, List<ItemCommand> items, String paymentInfo) {
        this.customerId = customerId;
        this.items = items;
        this.paymentInfo = paymentInfo;
    }

    public String getCustomerId() { return customerId; }
    public List<ItemCommand> getItems() { return items; }
    public String getPaymentInfo() { return paymentInfo; }

    public record ItemCommand(Long productId, int quantity) {
        public Long getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }
}
