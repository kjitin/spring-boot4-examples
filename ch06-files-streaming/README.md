# Chapter 6 – File Handling, Streaming, and Large Payloads

| Book section | Code |
|---|---|
| Multipart upload + limits | `file/upload/FileUploadController.java`, `application.properties` |
| Download + HTTP range requests | `file/download/FileDownloadController.java` |
| Streaming JSON / NDJSON / CSV | `streaming/json`, `streaming/ndjson`, `streaming/csv` |
| Async MVC (`DeferredResult`, `CompletableFuture`) | `backpressure/async/AsyncController.java` |
| Proxy / context-path settings | `application.properties` |

## Changes from the book
- **Bug fix (JSON stream):** the snippet left a trailing comma (as the book notes), so the output was invalid JSON.
  It also called `ObjectMapper.writeValue(outputStream, …)`, which closes the response stream after the first element
  by default. Now a comma goes before every element except the first, and `AUTO_CLOSE_TARGET` is disabled.
  The tests parse all 100,000 elements.
- Spring Boot 4 uses Jackson 3 (`tools.jackson.*` instead of `com.fasterxml.jackson.databind`).
- **Security:** the upload used the client's file name as-is, and the download used the raw path variable,
  both open to path traversal. Both now validate the path. CSV formatting uses `Locale.ROOT` so the decimal separator is always `.`.
