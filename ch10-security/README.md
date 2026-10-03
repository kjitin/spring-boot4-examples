# Chapter 10 – Modern Spring Security Configuration

| Book section | Code |
|---|---|
| `SecurityFilterChain` with form login + HTTP Basic | `security/SecurityConfig.java` |
| Multiple filter chains | `securityvariants/multichain/MultipleSecurityChainsConfig.java` |
| Stateless API | `securityvariants/stateless/StatelessSecurityConfig.java` |
| Method security + `@PreAuthorize` | `security/MethodSecurityConfig.java`, `service/ProductService.java` |
| Custom `PermissionEvaluator` | `security/permission/CustomPermissionEvaluator.java` |
| CORS | `security/CorsConfig.java`, `securityvariants/cors/CorsSecurityConfig.java`, `@CrossOrigin` on `web/ProductController.java` |
| Session fixation | `securityvariants/session/SessionFixationSecurityConfig.java` |

Users: `user/password` (USER) and `admin/adminpass` (ADMIN, USER).

## Changes from the book
- **Bug fix:** `MultipleSecurityChainsConfig` had no `@Order`. Two chains without an order fail startup, because
  the catch-all chain would hide `/api/**`. Added `@Order(1)` and `@Order(2)`.
- Spring Security 7 removed the non-lambda `sessionFixation().migrateSession()` DSL; it is now `sessionFixation(f -> f.migrateSession())`.
- Each alternative `SecurityFilterChain` setup is in `securityvariants/` and is loaded on its own by `SecurityVariantsTests`.
