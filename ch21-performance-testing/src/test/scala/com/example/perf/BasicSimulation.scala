package com.example.perf

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080") // Base URL for all requests
    .acceptHeader("application/json")
    .userAgentHeader("Gatling/PerformanceTest")

  val scn = scenario("Basic Spring Boot API Test")
    .exec(http("Get All Products")
      .get("/products")
      .check(status.is(200)))
    .pause(1) // Think time
    .exec(http("Get Product by ID")
      .get("/products/1")
      .check(status.is(200)))

  setUp(scn.inject(atOnceUsers(10)).protocols(httpProtocol))
    // Fail the build if any request fails
    .assertions(global.failedRequests.count.is(0))
}
