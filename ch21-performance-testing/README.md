# Chapter 21 – Performance Testing, Load Testing, and Profiling

`src/test/scala/com/example/perf/BasicSimulation.scala` is the book's Gatling simulation.

```bash
# 1. start the target app (Chapter 19)
cd ../ch19-webflux && ../mvnw spring-boot:run
# 2. run the load test (report in target/gatling/)
cd ../ch21-performance-testing && ../mvnw gatling:test
```

Other commands from the chapter:
```bash
jmeter -n -t /path/to/your/testplan.jmx -l /path/to/results.jtl -e -o /path/to/dashboard
java -XX:+FlightRecorder -jar your-app.jar
jcmd <pid> JFR.start name=MyRecording duration=60s filename=myrecording.jfr
```

## Changes from the book
- Fixed a broken string literal in `baseUrl(...)`. Uses `userAgentHeader` (the Gatling 3 DSL name) and current
  Gatling/plugin versions, plus `scala-maven-plugin` to compile the Scala simulation.
- Added an assertion so `gatling:test` fails if any request fails.
