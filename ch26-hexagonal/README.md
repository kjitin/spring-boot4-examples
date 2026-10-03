# Chapter 26 – Hexagonal Architecture (Ports and Adapters)

| Layer | Code |
|---|---|
| Driving port | `application/port/UserServicePort.java` |
| Driven port | `application/port/UserRepositoryPort.java` |
| Application service | `application/UserService.java` |
| Driving adapter (REST) | `infrastructure/web/UserController.java` |
| Driven adapter (JPA) | `infrastructure/persistence/JpaUserRepositoryAdapter.java`, `JpaUserRepository.java` |
| Domain | `domain/User.java`, `domain/UserNotFoundException.java` |

`HexagonalTests` tests the application core with an in-memory adapter (no Spring) and then through the real web and JPA adapters.

## Notes
- `User`, `JpaUserRepository` and `UserNotFoundException` are referenced but not shown in the book. Because the
  book's adapter passes `User` straight to Spring Data, `User` is a JPA entity here.
