package com.example.TaskManager.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUrlPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            return; // Local dev — do nothing, use application.properties
        }

        try {
            // Normalize both postgres:// and postgresql:// schemes
            String normalized = databaseUrl
                    .replace("postgresql://", "http://")
                    .replace("postgres://", "http://");

            URI dbUri = new URI(normalized);
            String host = dbUri.getHost();
            int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
            String path = dbUri.getPath();
            String[] userInfo = dbUri.getUserInfo().split(":", 2);

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + "?sslmode=require";

            Map<String, Object> props = new HashMap<>();
            props.put("spring.datasource.url", jdbcUrl);
            props.put("spring.datasource.username", userInfo[0]);
            props.put("spring.datasource.password", userInfo[1]);

            // Add with highest priority so it overrides any env vars
            environment.getPropertySources().addFirst(
                    new MapPropertySource("renderDatabaseConfig", props)
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse DATABASE_URL: " + databaseUrl, e);
        }
    }
}
