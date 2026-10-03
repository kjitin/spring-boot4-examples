# Chapter 18 – Serverless with Spring Cloud Function

- `FunctionConfiguration.java`: `uppercase` and `greetUser` functions.
- `application.properties`: composition `greetUser|uppercase` (the default function).
- HTTP: `curl -H "Content-Type: text/plain" -d hello localhost:8080/uppercase`
- AWS Lambda: `../mvnw package -Paws` builds `target/ch18-cloud-function-0.0.1-SNAPSHOT-aws.jar`.
  Set the handler to `org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest`.

## Changes from the book
- The shade plugin's `PropertiesMergingResourceTransformer` comes from `spring-boot-maven-plugin`, which must be a
  dependency of the shade plugin; the book omits it. The transformer list also merges Boot's
  `AutoConfiguration.imports` and replaces the shade config inherited from the Boot parent. Boot's repackaging is
  skipped in that profile, because Lambda needs a flat jar.
- Added `aws-lambda-java-core`: the adapter declares the Lambda interfaces as optional.
- The test invokes the real `FunctionInvoker` handler, as the Lambda runtime would.
