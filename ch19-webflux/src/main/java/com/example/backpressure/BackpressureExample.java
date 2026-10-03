package com.example.backpressure;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureExample {

    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofMillis(1)) // Fast producer: emits every 1ms
            .onBackpressureDrop(droppedItem -> System.out.println("Dropped: " + droppedItem)) // Drop if consumer is slow
            .publishOn(Schedulers.boundedElastic()) // Process on a different thread pool
            .doOnNext(item -> {
                try {
                    Thread.sleep(10); // Slow consumer: processes every 10ms
                    System.out.println("Consumed: " + item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            })
            .subscribe();

        Thread.sleep(5000); // Keep main thread alive for 5 seconds
    }
}
