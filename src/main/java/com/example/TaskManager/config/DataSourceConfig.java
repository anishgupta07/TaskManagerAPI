package com.example.TaskManager.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/task-manager}") String defaultUrl,
            @Value("${spring.datasource.username:postgres}") String defaultUsername,
            @Value("${spring.datasource.password:}") String defaultPassword
    ) {
        HikariDataSource ds = new HikariDataSource();

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Running on Render — convert postgres:// URL to JDBC format
            try {
                URI dbUri = new URI(databaseUrl);
                String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
                ds.setJdbcUrl(jdbcUrl);
                ds.setUsername(dbUri.getUserInfo().split(":")[0]);
                ds.setPassword(dbUri.getUserInfo().split(":")[1]);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL: " + databaseUrl, e);
            }
        } else {
            // Local development — use application.properties defaults
            ds.setJdbcUrl(defaultUrl);
            ds.setUsername(defaultUsername);
            ds.setPassword(defaultPassword);
        }

        return ds;
    }
}
