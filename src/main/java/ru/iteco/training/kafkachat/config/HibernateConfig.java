package ru.iteco.training.kafkachat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Конфигурация Hibernate
 */
@Configuration
public class HibernateConfig {

    @Autowired
    private AbstractEnvironment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.iteco.training.kafkachat.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        environment.getPropertySources().iterator().forEachRemaining(propertySource -> {
            if (propertySource instanceof MapPropertySource) {
                properties.putAll(((MapPropertySource) propertySource).getSource());
            }
            if (propertySource instanceof  CompositePropertySource){
                properties.putAll(getPropertiesInCompositePropertySource((CompositePropertySource) propertySource));
            }
        });
        Map<String, String> map = properties.entrySet().stream()
                .filter(e -> e.getKey().toString().startsWith("hibernate."))
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()));

        Properties hibernateProperties = new Properties();
        hibernateProperties.putAll(map);
        return hibernateProperties;
    }

    private Properties getPropertiesInCompositePropertySource(CompositePropertySource compositePropertySource){
        final Properties properties = new Properties();
        compositePropertySource.getPropertySources().forEach(propertySource -> {
            if (propertySource instanceof MapPropertySource) {
                properties.putAll(((MapPropertySource) propertySource).getSource());
            }
            if (propertySource instanceof CompositePropertySource)
                properties.putAll(getPropertiesInCompositePropertySource((CompositePropertySource) propertySource));
        });
        return properties;
    }
}
