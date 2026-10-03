# Chapter 13 – Unit, Integration, and End-to-End Testing

All tests from the chapter are in `src/test/java`. The classes under test (`GreetingController`, `GreetingService`,
`Product`, `ProductRepository`, `OrderService`, `ProductService`) are in `src/main/java`, as are the HTML pages used by the E2E tests.

| Test | Kind |
|---|---|
| `FullIntegrationTest` | Full app on a random port + `TestRestTemplate` |
| `WebLayerTest` | `@WebMvcTest` slice + `@MockitoBean` |
| `JpaRepositoryTest` | `@DataJpaTest` + `TestEntityManager` |
| `ServiceWithMockedDependencyTest` / `ServiceWithSpiedDependencyTest` | `@MockitoBean` / `@MockitoSpyBean` |
| `OrderServiceUnitTest` | Plain Mockito |
| `ProductRepositoryIntegrationTest`, `ComplexIntegrationTest` | Testcontainers (PostgreSQL, Redis); need Docker, skipped without it |
| `e2e/SeleniumE2ETest`, `e2e/PlaywrightE2ETest` | Browser E2E, opt-in: `../mvnw verify -Pe2e` (Selenium uses your local Chrome; Playwright downloads Chromium) |

## Changes for Spring Boot 4
- `@MockBean` and `@SpyBean` were removed. Use `@MockitoBean` and `@MockitoSpyBean` (`org.springframework.test.context.bean.override.mockito`).
- `TestRestTemplate` moved to `spring-boot-resttestclient` and is enabled with `@AutoConfigureTestRestTemplate`.
- Test slices moved packages: `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`,
  `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`, `org.springframework.boot.jpa.test.autoconfigure.TestEntityManager`.
- Testcontainers 2: `org.testcontainers.postgresql.PostgreSQLContainer` (non-generic). Uses `postgres:17` (the book's `postgres:13` is end-of-life).
- Added missing `assertThat` imports in two book tests. `Product` got `equals`/`hashCode`: the integration test
  compares instances loaded in different persistence contexts.
- `spring.jpa.hibernate.ddl-auto=create-drop`: Boot only generates the schema automatically for embedded databases.
