# Chapter 9 – JDBC, jOOQ, and NoSQL

| Book section | Code |
|---|---|
| `JdbcTemplate` CRUD | `jdbc/ProductJdbcRepository.java` |
| Batch operations | `jdbc/ProductBatchJdbcRepository.java` |
| jOOQ | `jooq/ProductJooqRepository.java` + `jooq-codegen-maven` in `pom.xml` |
| Spring Data MongoDB | `mongodb/ProductDocument.java`, `ProductMongoRepository.java` |
| Spring Data Redis | `redis/ProductRedisService.java`, `redis/RedisConfig.java` |

The MongoDB and Redis tests use Testcontainers. They need Docker and are skipped without it.

## Changes from the book
- **jOOQ code generation** read the schema from a live PostgreSQL database at build time. It now uses jOOQ's
  `DDLDatabase` on `schema.sql`, so the build needs no database. Also fixed a typo in the book's PostgreSQL
  dependency (`</n>` instead of `</scope>`).
- **Redis:** Boot's default `RedisTemplate` uses JDK serialization, and the `Product` record is not `Serializable`,
  so the book's service would fail. `RedisConfig` defines a `RedisTemplate<String, Object>` with typed JSON (Jackson 3) values.
- `ProductDocument` and `ProductMongoRepository` are split into two files and get the constructors and accessors the book omits.
