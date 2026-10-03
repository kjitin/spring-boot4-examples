package com.example;

import com.example.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationPropertiesValidationTests {

    @Test
    void failsFastWhenRequiredPropertiesAreMissing() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ValidationAutoConfiguration.class))
                .withUserConfiguration(AppConfig.class)
                .withPropertyValues("app.datasource.url=jdbc:h2:mem:demo")
                .run(context -> assertThat(context).hasFailed());
    }
}
