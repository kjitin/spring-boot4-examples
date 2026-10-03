package com.example.streaming.ndjson;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.stream.Stream;

@RestController
public class LargeNdJsonStreamController {

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .disable(StreamWriteFeature.AUTO_CLOSE_TARGET)
            .build();

    @GetMapping(value = "/large-ndjson-stream", produces = "application/x-ndjson")
    public ResponseEntity<StreamingResponseBody> streamLargeNdJson() {
        StreamingResponseBody responseBody = outputStream -> {
            Stream<MyDataDto> dataStream = generateLargeDataStream();

            dataStream.forEachOrdered(data -> {
                try {
                    objectMapper.writeValue(outputStream, data);
                    outputStream.write("\n".getBytes()); // Newline after each JSON object
                } catch (java.io.IOException | JacksonException e) {
                    throw new RuntimeException("Error writing NDJSON stream", e);
                }
            });
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-ndjson"))
                .body(responseBody);
    }

    private Stream<MyDataDto> generateLargeDataStream() {
        return Stream.iterate(0, i -> i + 1)
                .limit(100000) // 100,000 objects
                .map(i -> new MyDataDto(i, "Item " + i, Math.random() * 100));
    }

    record MyDataDto(long id, String name, double value) {}
}
