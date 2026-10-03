package com.example.streaming.csv;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.stream.Stream;

@RestController
public class LargeCsvStreamController {

    @GetMapping(value = "/large-csv-stream", produces = "text/csv")
    public ResponseEntity<StreamingResponseBody> streamLargeCsv() {
        StreamingResponseBody responseBody = outputStream -> {
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {
                // Write CSV header
                writer.println("ID,Name,Value");

                // Simulate fetching a large number of objects
                Stream<MyDataDto> dataStream = generateLargeDataStream();

                dataStream.forEachOrdered(data -> {
                    // Locale.ROOT keeps the decimal separator a '.', whatever the server locale
                    writer.printf(Locale.ROOT, "%d,%s,%.2f\n", data.id(), data.name(), data.value());
                    writer.flush(); // Flush after each line to ensure streaming
                });
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"data.csv\"")
                .body(responseBody);
    }

    private Stream<MyDataDto> generateLargeDataStream() {
        return Stream.iterate(0, i -> i + 1)
                .limit(100000) // 100,000 objects
                .map(i -> new MyDataDto(i, "Item " + i, Math.random() * 100));
    }

    record MyDataDto(long id, String name, double value) {}
}
