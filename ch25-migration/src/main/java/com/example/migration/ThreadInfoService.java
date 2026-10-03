package com.example.migration;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ThreadInfoService {

    @Async
    public CompletableFuture<Boolean> isVirtualAsync() {
        return CompletableFuture.completedFuture(Thread.currentThread().isVirtual());
    }
}
