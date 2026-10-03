package com.example.production;

import io.opentelemetry.contrib.awsxray.AwsXrayIdGenerator;
import org.springframework.boot.micrometer.tracing.opentelemetry.autoconfigure.SdkTracerProviderBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * AWS X-Ray: the book's io.opentelemetry:opentelemetry-exporter-aws-xray artifact does not exist. X-Ray ingests
 * OpenTelemetry spans through the AWS Distro for OpenTelemetry (ADOT) collector over OTLP; the application only
 * needs X-Ray-compatible trace IDs (timestamp-prefixed), provided by opentelemetry-aws-xray.
 */
@Configuration
@Profile("aws")
public class XRayTracingConfig {

    @Bean
    SdkTracerProviderBuilderCustomizer xrayIdGenerator() {
        return builder -> builder.setIdGenerator(AwsXrayIdGenerator.getInstance());
    }
}
