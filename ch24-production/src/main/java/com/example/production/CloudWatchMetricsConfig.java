package com.example.production;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;

/**
 * Spring Boot has no built-in CloudWatch export (the book's management.metrics.export.cloudwatch.* properties
 * come from Spring Cloud AWS 2.x). This configuration keeps those property names and registers Micrometer's
 * CloudWatch (AWS SDK v2) registry; Boot adds it to the composite MeterRegistry automatically.
 */
@Configuration
@ConditionalOnBooleanProperty("management.metrics.export.cloudwatch.enabled")
public class CloudWatchMetricsConfig {

    @Bean
    @ConditionalOnMissingBean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        // Region and credentials come from the default AWS provider chain (env vars, profile, instance role)
        return CloudWatchAsyncClient.create();
    }

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(Environment environment, CloudWatchAsyncClient client) {
        CloudWatchConfig config = new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String namespace() {
                return environment.getProperty("management.metrics.export.cloudwatch.namespace", "MySpringBootApp");
            }

            @Override
            public Duration step() {
                return environment.getProperty("management.metrics.export.cloudwatch.step", Duration.class, Duration.ofMinutes(1));
            }
        };
        return new CloudWatchMeterRegistry(config, Clock.SYSTEM, client);
    }
}
