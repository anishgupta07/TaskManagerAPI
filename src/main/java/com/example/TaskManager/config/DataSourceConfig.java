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

    private String toJdbcUrl(String rawUrl) throws URISyntaxException {
        // Normalize both postgres:// and postgresql:// to a parseable scheme
        String normalized = rawUrl
                .replace("postgresql://", "http://")
                .replace("postgres://", "http://");
        URI dbUri = new URI(normalized);
        String host = dbUri.getHost();
        int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
        String path = dbUri.getPath(); // e.g. /taskmanager
        return "jdbc:postgresql://" + host + ":" + port + path + "?sslmode=require";
    }

    private String[] extractCredentials(String rawUrl) throws URISyntaxException {
        String normalized = rawUrl
                .replace("postgresql://", "http://")
                .replace("postgres://", "http://");
        URI dbUri = new URI(normalized);
        return dbUri.getUserInfo().split(":", 2);
    }

    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/task-manager}") String defaultUrl,
            @Value("${spring.datasource.username:postgres}") String defaultUsername,
            @Value("${spring.datasource.password:}") String defaultPassword
    ) {
        HikariDataSource ds = new HikariDataSource();

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                ds.setJdbcUrl(toJdbcUrl(databaseUrl));
                String[] creds = extractCredentials(databaseUrl);
                ds.setUsername(creds[0]);
                ds.setPassword(creds[1]);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL: " + databaseUrl, e);
            }
        } else {
            ds.setJdbcUrl(defaultUrl);
            ds.setUsername(defaultUsername);
            ds.setPassword(defaultPassword);
        }

        return ds;
    }
}
