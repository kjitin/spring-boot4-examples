package com.example.di;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

// Stub the randomly failing fail-fast check so the context always starts
@MockitoBean(types = com.example.failfast.CriticalServiceChecker.class)
@SpringBootTest
class InjectionTests {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    MyService myService;

    @Test
    void primaryBeanIsInjectedByDefault() {
        assertThat(orderService.notifyCustomer("hi")).isEqualTo("SMS: hi");
    }

    @Test
    void qualifierSelectsSpecificBean() {
        assertThat(userService.notifyUser("hi")).isEqualTo("Email: hi");
    }

    @Test
    void optionalDependencyIsEmptyWhenNoBeanExists() {
        assertThat(myService.hasAnotherService()).isFalse();
    }
}
