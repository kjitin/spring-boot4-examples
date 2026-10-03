package com.example.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppDataSourceProperties.class, AppDataSourceRecordProperties.class})
public class AppConfig {
}
