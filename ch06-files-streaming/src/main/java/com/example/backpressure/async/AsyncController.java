package com.example.backpressure.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync // Enable @Async support
public class AsyncController {

    private final SlowService slowService;

    public AsyncController(SlowService slowService) {
        this.slowService = slowService;
    }

    @GetMapping("/async-deferred-result")
    public DeferredResult<String> handleDeferredResult() {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        slowService.performSlowOperation(deferredResult);
        return deferredResult;
    }

    @GetMapping("/async-completable-future")
    public CompletableFuture<String> handleCompletableFuture() {
        return slowService.performSlowOperationCompletableFuture();
    }
}

@Service
class SlowService {

    @Async
    public void performSlowOperation(DeferredResult<String> deferredResult) {
        try {
            Thread.sleep(2000); // Simulate a slow operation
            deferredResult.setResult("Operation completed asynchronously!");
        } catch (InterruptedException e) {
            deferredResult.setErrorResult(e);
        }
    }

    @Async
    public CompletableFuture<String> performSlowOperationCompletableFuture() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000); // Simulate a slow operation
                return "Operation completed with CompletableFuture!";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
