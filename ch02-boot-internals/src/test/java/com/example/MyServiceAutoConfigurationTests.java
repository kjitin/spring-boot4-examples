package com.example;

import com.example.myservice.MyService;
import com.example.myservice.autoconfigure.MyServiceAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MyServiceAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MyServiceAutoConfiguration.class));

    @Test
    void createsMyServiceByDefault() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(MyService.class));
    }

    @Test
    void backsOffWhenUserDefinesMyService() {
        MyService custom = new MyService();
        contextRunner.withBean(MyService.class, () -> custom)
                .run(context -> assertThat(context.getBean(MyService.class)).isSameAs(custom));
    }

    @Test
    void canBeDisabledWithProperty() {
        contextRunner.withPropertyValues("my.service.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(MyService.class));
    }
}
