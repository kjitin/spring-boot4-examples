package com.example.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

/** The Flux and Mono snippets from the chapter, runnable with main(). */
public class ReactorBasics {

    // From a list
    static Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("apple", "banana", "cherry"));

    // From individual items
    static Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5);

    // From a range
    static Flux<Long> longFlux = Flux.range(1, 10).map(Long::valueOf);

    // Interval (emits items periodically)
    static Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);

    // Error
    static Flux<String> errorFlux = Flux.error(new RuntimeException("Something went wrong"));

    // From an item
    static Mono<String> stringMono = Mono.just("hello");

    // Empty Mono
    static Mono<Object> emptyMono = Mono.empty();

    // Error Mono
    static Mono<String> errorMono = Mono.error(new IllegalArgumentException("Invalid input"));

    // From a Callable
    static Mono<Long> longMono = Mono.fromCallable(() -> System.currentTimeMillis());

    public static void main(String[] args) {
        stringFlux.map(String::toUpperCase) // Transform each item
                  .filter(s -> s.startsWith("B")) // Filter items
                  .log() // Log signals for debugging
                  .subscribe(System.out::println); // Subscribe to consume items

        integerFlux.zipWith(stringFlux, (i, s) -> i + "-" + s) // Combine with another Flux
                   .subscribe(System.out::println);

        intervalFlux.doOnNext(i -> System.out.println("Emitting: " + i))
                    .blockLast(); // Block until the last item is emitted (for testing/main method)

        stringMono.map(String::length) // Transform the item
                  .defaultIfEmpty(0) // Provide a default if empty
                  .subscribe(System.out::println); // Subscribe to consume the item

        Mono<String> fallbackMono = errorMono.onErrorResume(e -> {
            System.err.println("Error occurred: " + e.getMessage());
            return Mono.just("fallback value"); // Provide a fallback in case of error
        });
        fallbackMono.subscribe(System.out::println);
    }
}
