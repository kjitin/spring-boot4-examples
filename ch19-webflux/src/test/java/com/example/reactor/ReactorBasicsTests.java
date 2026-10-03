package com.example.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ReactorBasicsTests {

    @Test
    void fluxOperators() {
        StepVerifier.create(ReactorBasics.stringFlux.map(String::toUpperCase).filter(s -> s.startsWith("B")))
                .expectNext("BANANA").verifyComplete();
        StepVerifier.create(ReactorBasics.integerFlux.zipWith(ReactorBasics.stringFlux, (i, s) -> i + "-" + s))
                .expectNext("1-apple", "2-banana", "3-cherry").verifyComplete();
        StepVerifier.create(ReactorBasics.longFlux).expectNextCount(10).verifyComplete();
        StepVerifier.create(ReactorBasics.errorFlux).expectErrorMessage("Something went wrong").verify();
    }

    @Test
    void intervalWithVirtualTime() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(5))
                .thenAwait(Duration.ofSeconds(5))
                .expectNext(0L, 1L, 2L, 3L, 4L)
                .verifyComplete();
    }

    @Test
    void monoOperators() {
        StepVerifier.create(ReactorBasics.stringMono.map(String::length).defaultIfEmpty(0))
                .expectNext(5).verifyComplete();
        StepVerifier.create(ReactorBasics.emptyMono).verifyComplete();
        StepVerifier.create(ReactorBasics.errorMono.onErrorResume(e -> Mono.just("fallback value")))
                .expectNext("fallback value").verifyComplete();
        StepVerifier.create(ReactorBasics.longMono).expectNextMatches(t -> t > 0).verifyComplete();
    }

    @Test
    void backpressureDropsItemsForSlowConsumer() {
        AtomicInteger dropped = new AtomicInteger();
        AtomicInteger consumed = new AtomicInteger();
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop(item -> dropped.incrementAndGet())
                .publishOn(Schedulers.boundedElastic(), 16)
                .doOnNext(item -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    consumed.incrementAndGet();
                })
                .take(Duration.ofMillis(500))
                .blockLast();

        assertThat(consumed.get()).isPositive();
        assertThat(dropped.get()).isGreaterThan(consumed.get());
    }
}
