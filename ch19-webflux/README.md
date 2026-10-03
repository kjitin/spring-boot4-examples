# Chapter 19 – Reactive Programming with Spring WebFlux and Reactor

| Book section | Code |
|---|---|
| Annotated reactive controller, SSE stream | `webflux/ProductController.java`, `ProductService.java`, `Product.java` |
| Functional endpoints | `webflux/FunctionalEndpointConfiguration.java` |
| Flux/Mono creation and operators | `reactor/ReactorBasics.java` (`main` method + `ReactorBasicsTests` with `StepVerifier`) |
| Backpressure | `backpressure/BackpressureExample.java` |
| R2DBC | `r2dbc/*`, `schema.sql`, `data.sql` |

This app is also the target of the Chapter 21 Gatling simulation (`/products`, `/products/1`).

## Changes from the book
- Two `ProductService` beans exist (WebFlux demo and R2DBC), so the R2DBC one is named `r2dbcProductService`.
- The H2 URL adds `CASE_INSENSITIVE_IDENTIFIERS=TRUE`. Spring Data R2DBC quotes `"products"` in lower case, while H2
  stores unquoted DDL names in upper case.
- The WebFlux `Product` got a no-argument constructor so JSON can be read back in tests.
