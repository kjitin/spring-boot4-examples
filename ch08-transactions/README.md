# Chapter 8 – Transaction Management in Depth

| Book section | Code |
|---|---|
| Propagation `REQUIRED`, `SUPPORTS`, `MANDATORY`, `REQUIRES_NEW` | `propagation/*` |
| Isolation | `isolation/AccountService.java` |
| Optimistic locking (`@Version`) | `domain/Product.java`, `locking/ProductPriceService.java` |
| Pessimistic locking | `repository/ProductRepository.java`, `locking/ProductPriceService.java` |
| Layered transactional boundary | `layered/*` |
| Hexagonal transactional boundary | `hexagonal/*` (use case, ports, JPA and payment adapters) |

`TransactionTests` checks each behaviour: rollback across services, `MANDATORY` outside a transaction,
`REQUIRES_NEW` surviving an outer rollback, lost-update detection, serialized writers, and use-case rollback on payment failure.

## Changes from the book
- The propagation examples used `String` product IDs while the rest of the chapter used `Long`. All examples now share
  one `Long`-keyed domain model.
- Two `OrderService` classes exist (propagation and layered), so the propagation one has the explicit bean name `propagationOrderService`.
- `Order` maps to the `orders` table (`ORDER` is a SQL keyword).
- The hexagonal example's domain types (`Order`, `Product`, `OrderId`, `CreateOrderCommand`) and adapters are
  only stubs in the book. Minimal working versions are provided.
