# Chapter 2 – Spring Boot 4 Under the Hood

| Book section | Code |
|---|---|
| Auto-configuration internals | `myservice/autoconfigure/MyServiceAutoConfiguration.java`, registered in `META-INF/spring/...AutoConfiguration.imports` |
| `@ConfigurationProperties` (JavaBean + record, validated) | `config/AppDataSourceProperties.java`, `config/AppDataSourceRecordProperties.java`, `config/AppConfig.java` |
| Profiles | `profile/DevConfig.java`, `profile/ProdConfig.java`, `application-dev.properties`, `application-prod.properties` |
| Environment abstraction | `env/MyEnvironmentReader.java` |
| Actuator | `pom.xml` (actuator starter), `application.properties` |

Run: `../mvnw spring-boot:run -Dspring-boot.run.profiles=dev`, then open http://localhost:8080/actuator.

## Changes from the book
- `DevConfig` and `ProdConfig` were in one snippet; Java needs one public class per file, so they are split.
- Added `AnotherServiceAutoConfiguration` (referenced by `@AutoConfigureAfter` but not shown) and the
  `AutoConfiguration.imports` file that actually registers the auto-configuration.
- Tests prove the conditions work: the bean backs off when you define your own, `my.service.enabled=false`
  disables it, and missing required properties fail startup.
