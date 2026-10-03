package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductEndpointsTests {

    @Autowired
    WebTestClient client;

    @Test
    void annotatedEndpoints() {
        client.get().uri("/products").exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(3);
        client.get().uri("/products/2").exchange()
                .expectBody().jsonPath("$.name").isEqualTo("Mouse");
        client.get().uri("/products/99").exchange()
                .expectStatus().isOk().expectBody().isEmpty();
    }

    @Test
    void functionalEndpoints() {
        client.get().uri("/functional/products").exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class).hasSize(3);
        client.get().uri("/functional/products/3").exchange()
                .expectBody().jsonPath("$.name").isEqualTo("Keyboard");
    }

    @Test
    void serverSentEventsStreamOnePerSecond() {
        Flux<Product> stream = client.mutate().responseTimeout(Duration.ofSeconds(10)).build()
                .get().uri("/products/stream").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange().expectStatus().isOk()
                .returnResult(Product.class).getResponseBody();

        StepVerifier.create(stream.map(Product::getName))
                .expectNext("Laptop", "Mouse", "Keyboard")
                .verifyComplete();
    }
}
