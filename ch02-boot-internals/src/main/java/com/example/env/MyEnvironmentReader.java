package com.example.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyEnvironmentReader {

    private final Environment environment;

    @Autowired
    public MyEnvironmentReader(Environment environment) {
        this.environment = environment;
    }

    public void printActiveProfiles() {
        for (String profile : environment.getActiveProfiles()) {
            System.out.println("Active Profile: " + profile);
        }
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }
}
