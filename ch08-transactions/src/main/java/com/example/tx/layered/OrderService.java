package com.example.tx.layered;

import com.example.tx.domain.Order;
import com.example.tx.domain.OrderItem;
import com.example.tx.domain.Product;
import com.example.tx.repository.OrderRepository;
import com.example.tx.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service Layer (transactional boundary)
@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;

    @Transactional // Transaction starts here
    public OrderDto createOrder(CreateOrderRequest request) {
        Order order = new Order(request.getCustomerId());
        orderRepository.save(order);

        request.getItems().forEach(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow();
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product " + product.getName());
            }
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
            order.addItem(new OrderItem(order, product, itemRequest.getQuantity()));
        });

        orderRepository.save(order); // Updates order with items
        return OrderMapper.toDto(order);
    } // Transaction commits or rolls back here
}
