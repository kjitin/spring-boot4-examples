# Use a multi-stage build for smaller images

# Stage 1: Build the application
# (Java 21 instead of the book's 17 to match the rest of the examples; Boot 4 supports 17+)
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
# Copy Maven wrapper and pom.xml
# (a trailing "# comment" on the COPY line, as in the book, would be treated as extra file names)
COPY mvnw pom.xml ./
# Download dependencies to speed up subsequent builds
RUN ./mvnw dependency:go-offline
COPY src ./src
# Build the JAR
RUN ./mvnw package -Dmaven.test.skip=true

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
