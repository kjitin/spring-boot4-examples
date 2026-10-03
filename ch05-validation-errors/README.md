# Chapter 5 – Validation, Error Handling, and Problem Details

| Book section | Code |
|---|---|
| Bean Validation on request DTOs | `api/product/dto/CreateProductRequest.java`, `api/product/ProductController.java` |
| Validation groups | `validation/groups/OnCreate.java`, `OnUpdate.java`, `dto/ProductRequest.java`, `GroupValidatedProductController.java` |
| Composed constraint | `validation/constraints/ValidProductName.java` + `ValidationMessages.properties` |
| `@ControllerAdvice` (map-based body) | `legacy/errorhandling/GlobalExceptionHandler.java` |
| RFC 7807 Problem Details | `errorhandling/CustomResponseEntityExceptionHandler.java` |
| Shared exception type | `common/exceptions/ResourceNotFoundException.java` |

## Changes from the book
- Both global handlers handle the same exceptions, so only the Problem Details handler is active in the app.
  The map-based handler is in `legacy/` and tested on its own.
- `ProductRequest` uses the book's final version, with the composed `@ValidProductName`. The group-validated
  endpoints are under `/api/v2/products` so they can coexist with the `@Valid` example under `/api/v1`.
- Added the `ValidationMessages.properties` entry that the composed constraint's message key refers to.
