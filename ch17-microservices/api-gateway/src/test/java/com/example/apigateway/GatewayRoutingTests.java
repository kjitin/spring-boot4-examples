package com.example.apigateway;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

// Eureka is replaced by static "simple" discovery instances: product-service is a stub HTTP server,
// order-service points at a port where nothing listens.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "eureka.client.enabled=false")
class GatewayRoutingTests {

    static final HttpServer productService = start();
    static String lastPath;

    static HttpServer start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            server.createContext("/", exchange -> {
                lastPath = exchange.getRequestURI().getPath();
                byte[] body = "[\"Laptop\",\"Mouse\"]".getBytes(StandardCharsets.UTF_8);
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

    static int unusedPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @DynamicPropertySource
    static void instances(DynamicPropertyRegistry registry) throws IOException {
        int deadPort = unusedPort();
        registry.add("spring.cloud.discovery.client.simple.instances.product-service[0].uri",
                () -> "http://localhost:" + productService.getAddress().getPort());
        registry.add("spring.cloud.discovery.client.simple.instances.order-service[0].uri",
                () -> "http://localhost:" + deadPort);
    }

    @AfterAll
    static void stop() {
        productService.stop(0);
    }

    @LocalServerPort
    int port;

    final HttpClient http = HttpClient.newHttpClient();

    HttpResponse<String> get(String path) throws Exception {
        return http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    @Test
    @Order(1)
    void routesToProductServiceAndStripsPrefix() throws Exception {
        HttpResponse<String> response = get("/api/products/featured");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("[\"Laptop\",\"Mouse\"]");
        assertThat(lastPath).isEqualTo("/featured");
    }

    @Test
    @Order(3) // runs last: it stops the product-service stub
    void circuitBreakerFallbackWhenProductServiceIsDown() throws Exception {
        productService.stop(0); // simulate product-service going down
        HttpResponse<String> response = get("/api/products/featured");
        assertThat(response.body()).isEqualTo("Product service is currently unavailable. Please try again later.");
    }

    @Test
    @Order(2)
    void orderRouteWithoutCircuitBreakerReturnsError() throws Exception {
        assertThat(get("/api/orders/1").statusCode()).isGreaterThanOrEqualTo(500);
    }
}
