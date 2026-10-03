package com.example.lifecycle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class MyLifecycleBeanTests {

    @Test
    void callbacksRunInOrder(CapturedOutput output) {
        try (var context = new AnnotationConfigApplicationContext(MyLifecycleBean.class)) {
            context.getBean(MyLifecycleBean.class).businessMethod();
        }
        String out = output.getOut();
        assertThat(out).containsSubsequence(
                "1. MyLifecycleBean constructor called",
                "2. @PostConstruct called",
                "3. Business method executed",
                "4. @PreDestroy called");
    }
}
