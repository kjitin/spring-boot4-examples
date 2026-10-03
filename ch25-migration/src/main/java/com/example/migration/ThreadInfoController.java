package com.example.migration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ThreadInfoController {

    private final ThreadInfoService service;

    public ThreadInfoController(ThreadInfoService service) {
        this.service = service;
    }

    // Reports whether the request (and an @Async task) ran on virtual threads
    @GetMapping("/thread")
    public Map<String, Boolean> thread() {
        CompletableFuture<Boolean> async = service.isVirtualAsync();
        return Map.of("request", Thread.currentThread().isVirtual(), "async", async.join());
    }
}
