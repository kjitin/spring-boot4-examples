# Chapter 3 – Dependency Injection and Bean Lifecycles

| Book section | Code |
|---|---|
| Circular dependency gotcha | `circular/ServiceA.java`, `circular/ServiceB.java` (not component-scanned; `CircularDependencyTests` shows startup failing) |
| `Optional` injection, `@Primary`, `@Qualifier` | `di/*` |
| Bean scopes | `scopes/*` (+ `ScopeController` to observe request/session scopes) |
| Lifecycle callbacks | `lifecycle/MyLifecycleBean.java` |
| Fail-fast checks | `failfast/CriticalServiceChecker.java` |
| Custom health check | `health/CustomServiceHealthIndicator.java` |

## Changes from the book
- **Bug fix:** `UserService` put `@Qualifier("emailNotificationService")` on the field while using constructor
  injection. Spring ignores it there and injects the `@Primary` SMS bean. The qualifier is now on the constructor parameter.
- Spring Boot 4 moved `Health`/`HealthIndicator` to `org.springframework.boot.health.contributor`.
- The example services fail at random (`Math.random()`), so the tests replace them with Mockito mocks to stay deterministic.
