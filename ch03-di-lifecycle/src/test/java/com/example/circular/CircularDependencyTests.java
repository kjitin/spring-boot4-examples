package com.example.circular;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class CircularDependencyTests {

    @Test
    void constructorInjectionCycleFailsAtStartup() {
        new ApplicationContextRunner()
                .withUserConfiguration(ServiceA.class, ServiceB.class)
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure())
                            .hasRootCauseInstanceOf(BeanCurrentlyInCreationException.class);
                });
    }
}
