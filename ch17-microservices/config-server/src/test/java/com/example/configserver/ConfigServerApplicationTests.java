package com.example.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

// Uses the "native" profile (local files) instead of the placeholder Git repository
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("native")
class ConfigServerApplicationTests {

    @Autowired
    TestRestTemplate rest;

    @Test
    void servesConfigurationForProductService() {
        String body = rest.getForObject("/product-service/default", String.class);
        assertThat(body).contains("\"product.greeting\":\"Hello from the Config Server\"");
    }
}
