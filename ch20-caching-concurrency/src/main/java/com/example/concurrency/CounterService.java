package com.example.concurrency;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    // synchronized added: without it, readers may not see the latest value (no happens-before edge)
    public synchronized int getCount() {
        return count;
    }
}
