# Chapter 7 – Spring Data JPA in Real Systems

| Book section | Code |
|---|---|
| Identifier strategies | `ids/Product.java` (UUID), `ids/User.java` (IDENTITY) |
| Relationships, `@BatchSize` | `domain/Product.java`, `domain/OrderItem.java` |
| N+1 problem and fixes (JOIN FETCH, `@EntityGraph`) | `service/ProductReportService.java`, `repository/ProductRepository.java` |
| Interface and DTO projections | `projection/*`, `ProductRepository` |
| Query hints, streaming, bulk `@Modifying`, native SQL | `ProductRepository` |

`ProductRepositoryTests` counts SQL statements with Hibernate statistics: lazy loading issues several queries,
while JOIN FETCH and the entity graph use exactly one.

## Changes from the book
- The UUID `Product` example sets `@Entity(name = "UuidProduct")` so it can live next to the main `Product`.
  `User` maps to `app_user`, because `user` is a reserved word.
- `ProductSummaryDto` gets the getters marked `// Getters` in the book.
