package com.example.migration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VirtualThreadsTests {

    @LocalServerPort
    int port;

    @Test
    void requestsAndAsyncTasksRunOnVirtualThreads() throws Exception {
        String body = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/thread")).build(),
                HttpResponse.BodyHandlers.ofString()).body();
        assertThat(body).contains("\"request\":true").contains("\"async\":true");
    }
}
