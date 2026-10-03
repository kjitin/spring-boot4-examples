package com.example;

import com.example.logging.OrderService;
import com.example.metrics.PaymentService;
import com.example.tracing.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Small endpoints to try the examples by hand
@RestController
public class ObservabilityController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;

    public ObservabilityController(OrderService orderService, PaymentService paymentService, ProductService productService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @PostMapping("/orders/random")
    public void randomOrder() {
        orderService.generateAndProcessRandomOrder();
    }

    @PostMapping("/payments")
    public boolean pay(@RequestParam double amount) {
        return paymentService.processPayment(amount);
    }

    @GetMapping("/products/{id}")
    public String product(@PathVariable String id) {
        return productService.getProductDetails(id);
    }
}
