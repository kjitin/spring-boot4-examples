package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "app.datasource")
@Validated
public record AppDataSourceRecordProperties(
    @NotEmpty String url,
    @NotEmpty String username,
    @NotEmpty String password
) {}
