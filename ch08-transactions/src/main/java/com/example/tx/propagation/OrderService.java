package com.example.tx.propagation;

import com.example.tx.domain.Order;
import com.example.tx.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("propagationOrderService") // explicit name: the layered example also has an OrderService
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private InventoryService inventoryService;

    @Transactional(propagation = Propagation.REQUIRED)
    public Order placeOrder(Order order, Long productId, int quantity) {
        // This method will either join an existing transaction or create a new one.
        Order savedOrder = orderRepository.save(order);
        inventoryService.deductStock(productId, quantity); // This method will join the transaction
        return savedOrder;
    }
}
