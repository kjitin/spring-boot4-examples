# Chapter 22 – Deployment: JAR, Docker, and Kubernetes

```bash
docker build -t my-spring-app:latest .
docker run -p 8080:8080 my-spring-app:latest
curl localhost:8080/actuator/health/readiness
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
# kubectl apply -f k8s/ingress.yaml (if using Ingress)
```

## Changes from the book
- **Bug fix:** `COPY mvnw pom.xml ./ # Copy Maven wrapper…`. Docker does not support trailing comments, so `#`,
  `Copy`, … are treated as extra source files and the build fails. The comment is now on its own line.
- Base images use Java 21 (`eclipse-temurin:21-*`) to match the other examples. Boot 4 requires Java 17+.
- `management.endpoint.health.probes.enabled=true`, so the liveness/readiness endpoints used by the probes also exist outside Kubernetes.
- Includes its own Maven wrapper so the Docker build context is self-contained.
