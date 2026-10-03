# Chapter 20 – Caching, Concurrency, and Asynchronous Processing

| Book section | Code |
|---|---|
| `@EnableCaching`, `@Cacheable`, `@CachePut`, `@CacheEvict` | `caching/*` |
| Caffeine | `application.properties` |
| Redis cache | `application-redis.properties` (`RedisCachingTests` needs Docker, skipped without it) |
| Custom `@Async` executor | `concurrency/AsyncConfig.java`, `NotificationService.java` |
| Thread safety | `concurrency/CounterService.java` |
| `CompletableFuture` composition | `concurrency/OrderProcessingService.java` |
| HikariCP pool tuning | `application.properties` |

## Changes from the book
- **Bug fix:** the Hikari properties had inline `# comments` after the values, which become part of the value in
  `.properties` files and fail to bind. The comments are now on their own lines.
- `Product` implements `Serializable` so the Redis cache (JDK serialization by default) can store it.
- `CounterService.getCount()` is `synchronized` too. Without it, readers may see stale values.
