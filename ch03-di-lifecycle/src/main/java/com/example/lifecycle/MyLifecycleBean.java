package com.example.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class MyLifecycleBean {

    public MyLifecycleBean() {
        System.out.println("1. MyLifecycleBean constructor called");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("2. @PostConstruct called: Dependencies injected, ready for initialization");
    }

    public void businessMethod() {
        System.out.println("3. Business method executed");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("4. @PreDestroy called: Preparing for bean destruction");
    }
}
