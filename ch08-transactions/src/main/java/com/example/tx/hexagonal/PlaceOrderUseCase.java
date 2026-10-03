package com.example.tx.hexagonal;

import com.example.tx.hexagonal.domain.Order;
import com.example.tx.hexagonal.domain.OrderId;
import com.example.tx.hexagonal.domain.Product;
import com.example.tx.hexagonal.domain.ProductNotFoundException;
import com.example.tx.hexagonal.port.OrderPort;
import com.example.tx.hexagonal.port.PaymentPort;
import com.example.tx.hexagonal.port.ProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Example: Hexagonal Architecture Transactional Boundary

// Application Service (initiates transaction)
@Service
@Transactional
public class PlaceOrderUseCase {

    private final OrderPort orderPort;
    private final ProductPort productPort;
    private final PaymentPort paymentPort;

    public PlaceOrderUseCase(OrderPort orderPort, ProductPort productPort, PaymentPort paymentPort) {
        this.orderPort = orderPort;
        this.productPort = productPort;
        this.paymentPort = paymentPort;
    }

    public OrderId placeOrder(CreateOrderCommand command) {
        // 1. Load/create domain objects
        Order order = Order.create(command.getCustomerId());
        command.getItems().forEach(itemCommand -> {
            Product product = productPort.findById(itemCommand.getProductId())
                                         .orElseThrow(() -> new ProductNotFoundException(itemCommand.getProductId()));
            product.deductStock(itemCommand.getQuantity()); // Business logic in domain
            productPort.save(product);
            order.addItem(product, itemCommand.getQuantity());
        });

        // 2. Perform payment (via port)
        paymentPort.processPayment(order.getTotalAmount(), command.getPaymentInfo());

        // 3. Save order (via port)
        orderPort.save(order);

        return order.getId();
    } // Transaction commits or rolls back here
}
