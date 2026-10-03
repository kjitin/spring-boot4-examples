# Chapter 25 – Migrating from Spring Boot 3 to Spring Boot 4

- `pom.xml`: Boot 4 parent, Java version properties, Spring Cloud BOM.
- `build.gradle` / `settings.gradle`: the Gradle equivalent with a Java toolchain. Verified with Gradle 9.8;
  the foojay plugin downloads the JDK if it is missing.
- `application.properties`: virtual threads. `VirtualThreadsTests` checks that both Tomcat requests and `@Async` tasks run on virtual threads.

## Changes from the book
- The book shows `4.0.0-M1` with Spring Cloud `2024.0.0-M1`. Spring Cloud 2024.0 is for Boot 3.4; Boot 4.0 pairs with `2025.1.x`.
- Java 17 is Boot 4's minimum, but **virtual threads need Java 21**, so the book's settings (Java 17 + virtual threads)
  cannot work together. This module uses 21.
- `spring.task.execution.virtual.enabled` does not exist. `spring.threads.virtual.enabled` also switches the task executor.
