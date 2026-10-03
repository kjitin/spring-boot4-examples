package com.example.tx;

import com.example.tx.domain.Account;
import com.example.tx.domain.Order;
import com.example.tx.domain.Payment;
import com.example.tx.domain.Product;
import com.example.tx.hexagonal.CreateOrderCommand;
import com.example.tx.hexagonal.PlaceOrderUseCase;
import com.example.tx.hexagonal.domain.OrderId;
import com.example.tx.isolation.AccountService;
import com.example.tx.locking.ProductPriceService;
import com.example.tx.propagation.AuditService;
import com.example.tx.propagation.PaymentService;
import com.example.tx.propagation.ProductService;
import com.example.tx.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionTests {

    @Autowired @Qualifier("propagationOrderService") com.example.tx.propagation.OrderService propagationOrderService;
    @Autowired ProductService productService;
    @Autowired AuditService auditService;
    @Autowired PaymentService paymentService;
    @Autowired AccountService accountService;
    @Autowired ProductPriceService productPriceService;
    @Autowired PlaceOrderUseCase placeOrderUseCase;
    @Autowired ProductRepository productRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired AuditLogRepository auditLogRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired TransactionTemplate tx;
    @Autowired MockMvc mockMvc;

    Product laptop;
    Product mouse;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        paymentRepository.deleteAll();
        auditLogRepository.deleteAll();
        laptop = productRepository.save(new Product("Laptop", 1000.0, 5));
        mouse = productRepository.save(new Product("Mouse", 20.0, 1));
    }

    @Test
    void requiredRollsBackOrderWhenInventoryFails() {
        assertThatThrownBy(() -> propagationOrderService.placeOrder(new Order("c1"), mouse.getId(), 2))
                .hasMessage("Insufficient stock");
        assertThat(orderRepository.count()).isZero(); // the saved order was rolled back too

        propagationOrderService.placeOrder(new Order("c1"), laptop.getId(), 2);
        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(productRepository.findById(laptop.getId()).orElseThrow().getStock()).isEqualTo(3);
    }

    @Test
    void supportsRunsWithoutTransactionWhenCalledDirectly() {
        assertThat(productService.getAllProducts()).hasSize(2);
        Boolean active = tx.execute(status -> {
            productService.getAllProducts();
            return TransactionSynchronizationManager.isActualTransactionActive();
        });
        assertThat(active).isTrue();
    }

    @Test
    void mandatoryRequiresExistingTransaction() {
        assertThatThrownBy(() -> auditService.logAction("no tx"))
                .isInstanceOf(IllegalTransactionStateException.class);
        tx.executeWithoutResult(status -> auditService.logAction("inside tx"));
        assertThat(auditLogRepository.count()).isEqualTo(1);
    }

    @Test
    void requiresNewCommitsIndependentlyOfOuterRollback() {
        assertThatThrownBy(() -> tx.executeWithoutResult(status -> {
            paymentService.processPayment(new Payment(99.0));
            throw new IllegalStateException("outer transaction fails");
        })).isInstanceOf(IllegalStateException.class);
        assertThat(paymentRepository.count()).isEqualTo(1);
    }

    @Test
    void transferFundsIsAtomic() {
        Account alice = accountRepository.save(new Account("alice", 100));
        Account bob = accountRepository.save(new Account("bob", 0));

        accountService.transferFunds(alice.getId(), bob.getId(), 60);
        assertThatThrownBy(() -> accountService.transferFunds(alice.getId(), bob.getId(), 60))
                .hasMessage("Insufficient funds");

        assertThat(accountRepository.findById(alice.getId()).orElseThrow().getBalance()).isEqualTo(40);
        assertThat(accountRepository.findById(bob.getId()).orElseThrow().getBalance()).isEqualTo(60);
    }

    @Test
    void optimisticLockingDetectsLostUpdate() {
        Product copyA = productRepository.findById(laptop.getId()).orElseThrow();
        Product copyB = productRepository.findById(laptop.getId()).orElseThrow();

        productPriceService.changePrice(copyA, 900.0);
        assertThatThrownBy(() -> productPriceService.changePrice(copyB, 800.0))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
        assertThat(productRepository.findById(laptop.getId()).orElseThrow().getPrice()).isEqualTo(900.0);
    }

    @Test
    void pessimisticLockingSerializesWriters() throws Exception {
        Runnable reserve = () -> productPriceService.reserveStock(laptop.getId(), 1);
        List<Thread> threads = List.of(new Thread(reserve), new Thread(reserve), new Thread(reserve));
        threads.forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }
        assertThat(productRepository.findById(laptop.getId()).orElseThrow().getStock()).isEqualTo(2);
    }

    @Test
    void layeredServiceIsTheTransactionBoundary() throws Exception {
        String ok = """
                {"customerId":"c1","items":[{"productId":%d,"quantity":2}]}""".formatted(laptop.getId());
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(ok))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items[0].productName").value("Laptop"));

        String tooMany = """
                {"customerId":"c2","items":[{"productId":%d,"quantity":1},{"productId":%d,"quantity":5}]}"""
                .formatted(laptop.getId(), mouse.getId());
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(tooMany))
                .andExpect(status().isConflict());

        // The laptop deduction from the failed request was rolled back
        assertThat(productRepository.findById(laptop.getId()).orElseThrow().getStock()).isEqualTo(3);
        assertThat(orderRepository.count()).isEqualTo(1);
    }

    @Test
    void hexagonalUseCaseRollsBackWhenPaymentFails() {
        OrderId orderId = placeOrderUseCase.placeOrder(new CreateOrderCommand("c1",
                List.of(new CreateOrderCommand.ItemCommand(laptop.getId(), 1)), "VISA"));
        assertThat(orderId.value()).isNotNull();

        assertThatThrownBy(() -> placeOrderUseCase.placeOrder(new CreateOrderCommand("c2",
                List.of(new CreateOrderCommand.ItemCommand(laptop.getId(), 1)), "DECLINE")))
                .hasMessageContaining("declined");

        assertThat(productRepository.findById(laptop.getId()).orElseThrow().getStock()).isEqualTo(4);
        assertThat(orderRepository.count()).isEqualTo(1);
    }
}
