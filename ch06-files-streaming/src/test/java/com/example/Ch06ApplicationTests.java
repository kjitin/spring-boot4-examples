package com.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.FileSystemUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Ch06ApplicationTests {

    @LocalServerPort
    int port;

    final HttpClient http = HttpClient.newHttpClient();

    @BeforeAll
    static void createDownloadFile() throws IOException {
        Files.createDirectories(Path.of("downloads"));
        Files.writeString(Path.of("downloads", "hello.txt"), "0123456789abcdef");
    }

    @AfterAll
    static void cleanUp() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of("downloads"));
        FileSystemUtils.deleteRecursively(Path.of("uploads"));
    }

    HttpResponse<String> get(String path, String... headers) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
        if (headers.length > 0) {
            builder.headers(headers);
        }
        return http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void uploadsMultipartFile() throws Exception {
        String boundary = "----boundary";
        String body = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"report.txt\"\r\n"
                + "Content-Type: text/plain\r\n\r\n"
                + "file content\r\n"
                + "--" + boundary + "--\r\n";
        HttpResponse<String> response = http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/upload"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build(), HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("File uploaded successfully: report.txt");
        assertThat(Path.of("uploads", "report.txt")).hasContent("file content");
    }

    @Test
    void downloadsFileAndSupportsRangeRequests() throws Exception {
        HttpResponse<String> full = get("/download/hello.txt");
        assertThat(full.statusCode()).isEqualTo(200);
        assertThat(full.headers().firstValue("Content-Disposition")).hasValue("attachment; filename=\"hello.txt\"");
        assertThat(full.body()).isEqualTo("0123456789abcdef");

        HttpResponse<String> partial = get("/download/hello.txt", "Range", "bytes=0-3");
        assertThat(partial.statusCode()).isEqualTo(206);
        assertThat(partial.body()).isEqualTo("0123");

        assertThat(get("/download/missing.txt").statusCode()).isEqualTo(404);
    }

    @Test
    void streamsValidJsonArray() throws Exception {
        HttpResponse<String> response = get("/large-json-stream");
        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode array = JsonMapper.builder().build().readTree(response.body());
        assertThat(array.isArray()).isTrue();
        assertThat(array.size()).isEqualTo(100_000);
        assertThat(array.get(99_999).get("name").asString()).isEqualTo("Item 99999");
    }

    @Test
    void streamsNdJson() throws Exception {
        HttpResponse<String> response = get("/large-ndjson-stream");
        assertThat(response.headers().firstValue("Content-Type")).hasValue("application/x-ndjson");
        List<String> lines = response.body().lines().toList();
        assertThat(lines).hasSize(100_000);
        assertThat(lines.getFirst()).startsWith("{\"id\":0,\"name\":\"Item 0\"");
    }

    @Test
    void streamsCsv() throws Exception {
        HttpResponse<String> response = get("/large-csv-stream");
        List<String> lines = response.body().lines().toList();
        assertThat(lines).hasSize(100_001);
        assertThat(lines.getFirst()).isEqualTo("ID,Name,Value");
        assertThat(lines.get(1)).matches("0,Item 0,\\d+\\.\\d{2}");
    }

    @Test
    void asyncEndpointsComplete() throws Exception {
        assertThat(get("/async-deferred-result").body()).isEqualTo("Operation completed asynchronously!");
        assertThat(get("/async-completable-future").body()).isEqualTo("Operation completed with CompletableFuture!");
    }
}
