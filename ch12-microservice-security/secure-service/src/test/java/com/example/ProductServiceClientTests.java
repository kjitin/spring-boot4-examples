package com.example;

import com.example.serviceclient.ProductServiceClient;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

// A local HTTP server plays both the authorization server's token endpoint and the downstream product-service
@SpringBootTest
class ProductServiceClientTests {

    static final AtomicInteger tokenRequests = new AtomicInteger();
    static final List<String> authorizationHeaders = new CopyOnWriteArrayList<>();
    static final HttpServer server = start();

    static HttpServer start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            server.createContext("/token", exchange -> {
                tokenRequests.incrementAndGet();
                String form = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                assertThat(form).contains("grant_type=client_credentials");
                respond(exchange, "application/json",
                        "{\"access_token\":\"service-token\",\"token_type\":\"Bearer\",\"expires_in\":3600}");
            });
            server.createContext("/api/products", exchange -> {
                authorizationHeaders.add(exchange.getRequestHeaders().getFirst("Authorization"));
                respond(exchange, "application/json", "[\"Laptop\",\"Mouse\"]");
            });
            server.start();
            return server;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static void respond(com.sun.net.httpserver.HttpExchange exchange, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", type);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    @DynamicPropertySource
    static void endpoints(DynamicPropertyRegistry registry) {
        String base = "http://localhost:" + server.getAddress().getPort();
        registry.add("spring.security.oauth2.client.provider.internal-service-client.token-uri", () -> base + "/token");
        registry.add("product-service.url", () -> base);
    }

    @AfterAll
    static void stop() {
        server.stop(0);
    }

    @Autowired
    ProductServiceClient client;

    @Test
    void obtainsTokenWithClientCredentialsAndReusesIt() {
        assertThat(client.getProducts()).isEqualTo("[\"Laptop\",\"Mouse\"]");
        assertThat(client.getProducts()).isEqualTo("[\"Laptop\",\"Mouse\"]");

        assertThat(authorizationHeaders).containsExactly("Bearer service-token", "Bearer service-token");
        assertThat(tokenRequests).hasValue(1); // the access token is cached until it expires
    }
}
