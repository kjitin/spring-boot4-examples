package com.example.streaming.json;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@RestController
public class LargeJsonStreamController {

    // Spring Boot 4 uses Jackson 3 (tools.jackson). AUTO_CLOSE_TARGET must be disabled, otherwise
    // writeValue(outputStream, ...) closes the response stream after the first element.
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .disable(StreamWriteFeature.AUTO_CLOSE_TARGET)
            .build();

    @GetMapping(value = "/large-json-stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> streamLargeJson() {
        StreamingResponseBody responseBody = outputStream -> {
            // Simulate fetching a large number of objects from a database
            Stream<MyDataDto> dataStream = generateLargeDataStream();
            AtomicBoolean first = new AtomicBoolean(true);

            outputStream.write("[".getBytes()); // Start JSON array
            dataStream.forEachOrdered(data -> {
                try {
                    // Write a comma before every element except the first, so there is no trailing comma
                    if (!first.compareAndSet(true, false)) {
                        outputStream.write(",".getBytes());
                    }
                    objectMapper.writeValue(outputStream, data);
                } catch (java.io.IOException | JacksonException e) {
                    throw new RuntimeException("Error writing JSON stream", e);
                }
            });
            outputStream.write("]".getBytes()); // End JSON array
        };

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    private Stream<MyDataDto> generateLargeDataStream() {
        // In a real application, this would come from a database cursor or external source
        return Stream.iterate(0, i -> i + 1)
                .limit(100000) // 100,000 objects
                .map(i -> new MyDataDto(i, "Item " + i, Math.random() * 100));
    }

    record MyDataDto(long id, String name, double value) {}
}
