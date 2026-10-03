package com.example.tx.hexagonal.adapter;

import com.example.tx.domain.OrderItem;
import com.example.tx.hexagonal.domain.Order;
import com.example.tx.hexagonal.domain.OrderId;
import com.example.tx.hexagonal.port.OrderPort;
import com.example.tx.repository.OrderRepository;
import com.example.tx.repository.ProductRepository;
import org.springframework.stereotype.Repository;

// Adapters (implementations of ports, participate in transaction)
@Repository
public class JpaOrderAdapter implements OrderPort {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public JpaOrderAdapter(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void save(Order order) {
        com.example.tx.domain.Order entity = new com.example.tx.domain.Order(order.getCustomerId());
        order.getLines().forEach(line -> entity.addItem(new OrderItem(entity,
                productRepository.getReferenceById(line.product().getId()), line.quantity())));
        order.assignId(new OrderId(orderRepository.save(entity).getId()));
    }
}
