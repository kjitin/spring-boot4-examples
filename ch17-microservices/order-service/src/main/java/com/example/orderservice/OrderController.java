package com.example.orderservice;

import com.example.resilience.ProductServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final ProductServiceClient productServiceClient;

    public OrderController(OrderService orderService, ProductServiceClient productServiceClient) {
        this.orderService = orderService;
        this.productServiceClient = productServiceClient;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody String details) {
        orderService.placeOrder(details);
        return "PLACED";
    }

    @GetMapping("/available-products")
    public String availableProducts() {
        return productServiceClient.getProducts();
    }
}
