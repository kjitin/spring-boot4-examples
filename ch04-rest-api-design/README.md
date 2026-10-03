# Chapter 4 – Advanced REST API Design with Spring MVC

| Book section | Code |
|---|---|
| Resource modeling / CRUD controller | `api/product/ProductController.java` (+ in-memory `ProductService`, `dto/*`) |
| Versioning: URI path variable | `api/versioning/uri/ProductController.java` |
| Versioning: separate controllers | `api/versioning/separate/ProductV1Controller.java`, `ProductV2Controller.java` |
| Versioning: custom header | `api/versioning/header/ProductController.java` |
| Versioning: content negotiation (media type) | `api/versioning/mediatype/ProductController.java` |
| Versioning: query parameter | `api/versioning/param/ProductController.java` |
| Idempotency keys | `api/order/OrderController.java`, `OrderService.java` |
| Request/response DTOs | `api/product/dto/*` |

## Notes
- The book's `ProductService`/`OrderService` are "defined elsewhere"; simple in-memory versions are provided.
- The versioning strategies are alternative versions of the same controller, so they are not component-scanned
  together. `VersioningStrategiesTests` loads each one on its own.
