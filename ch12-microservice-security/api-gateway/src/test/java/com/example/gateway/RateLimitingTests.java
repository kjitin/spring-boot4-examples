package com.example.gateway;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Requires Docker (Redis backs the RequestRateLimiter); skipped when Docker is not available
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class RateLimitingTests {

    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    // Stand-in for the downstream product-service
    static HttpServer productService = startProductService();

    static HttpServer startProductService() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            server.createContext("/api/products", exchange -> {
                byte[] body = "[\"Laptop\"]".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, body.length);
                exchange.getResponseBody().write(body);
                exchange.close();
            });
            server.start();
            return server;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @DynamicPropertySource
    static void productServiceUri(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.discovery.client.simple.instances.product-service[0].uri",
                () -> "http://localhost:" + productService.getAddress().getPort());
    }

    @AfterAll
    static void stop() {
        productService.stop(0);
    }

    @LocalServerPort
    int port;

    @Test
    void routesToProductServiceAndRateLimitsBursts() throws Exception {
        HttpClient http = HttpClient.newHttpClient();
        List<Integer> statuses = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            HttpResponse<String> response = http.send(
                    HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/products")).build(),
                    HttpResponse.BodyHandlers.ofString());
            statuses.add(response.statusCode());
            if (i == 0) {
                assertThat(response.body()).isEqualTo("[\"Laptop\"]");
                assertThat(response.headers().firstValue("X-RateLimit-Burst-Capacity")).hasValue("20");
            }
        }
        assertThat(statuses).contains(200, 429);
        assertThat(statuses.stream().filter(s -> s == 200).count()).isLessThan(40);
    }
}
