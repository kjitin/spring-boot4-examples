# Chapter 23 – GraalVM Native Images

```bash
../mvnw -Pnative native:compile           # needs a local GraalVM for JDK 25+
./target/native-app
curl http://localhost:8080/hello          # Hello from Spring Boot Native!

../mvnw -Pnative spring-boot:build-image  # no local GraalVM needed, only Docker
docker run -p 8080:8080 native-app:0.0.1-SNAPSHOT
```
`k8s/deployment.yaml` is the low-memory native deployment from the chapter.

## Changes from the book
- Removed `org.springframework.experimental:spring-native` and the milestone repositories. They were only for
  Spring Boot 2.x; native support has been built in since Boot 3.
- **Spring Boot 4 native images require GraalVM for JDK 25+.** The buildpack config pins `BP_JVM_VERSION=25`.
  Without it, the buildpack uses the JDK version Maven runs on and fails.
- Added Actuator, because the native Kubernetes deployment's probes use `/actuator/health/*`.
