package ru.iteco.training.kafkachat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Конфигурация подключения к БД.
 */
@Configuration
public class DatabaseConfig {
    @Value(("${database.driver}"))
    String driver;
    @Value("${database.url}")
    private String url;
    @Value("${database.user}")
    private String user;
    @Value("${database.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
