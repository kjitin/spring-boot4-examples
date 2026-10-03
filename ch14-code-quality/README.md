# Chapter 14 – Code Quality, Static Analysis, and Refactoring

`pom.xml` wires up all the tools from the chapter. Checkstyle runs in `validate`; SpotBugs, PMD and CPD run in `verify`,
and the build fails on violations. SonarQube needs a server: `../mvnw verify sonar:sonar`.

Refactoring examples (`src/main/java/com/example/quality/refactoring`):
| Refactoring | Before | After |
|---|---|---|
| Extract Method | `before/OrderPrinter.java` | `after/OrderPrinter.java` |
| Introduce Explaining Variable | `before/BrowserCheck.java` | `after/BrowserCheck.java` |
| Replace Conditional with Polymorphism | `before/PriceCalculator.java` | `after/Product.java`, `Book`, `Electronics`, `GeneralProduct` |

`RefactoringBehaviourTests` checks that each refactored version behaves exactly like the original.

## Changes from the book
- Plugin versions updated (`checkstyle 3.6.0`, `spotbugs 4.10.4.1`, `pmd 3.28.0` with PMD 7.28, `sonar 5.8`).
  The book's versions cannot analyse Java 21+ code.
- PMD 7 replaced `/rulesets/java/design.xml` with `category/java/design.xml`.
- Added the `spotbugs-security-include.xml` filter file that the config refers to.
- PMD skips the `before/` package, because those classes contain the smells on purpose (PMD flags them when included).
