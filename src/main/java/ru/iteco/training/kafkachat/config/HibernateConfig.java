package ru.iteco.training.kafkachat.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import ru.iteco.training.kafkachat.entity.*;

import java.io.IOException;

/**
 * Конфигурация Hibernate
 */
@Configuration
public class HibernateConfig {

    @Autowired
    private AbstractEnvironment environment;

    @Bean
    public SessionFactory sessionFactory() throws IOException {
        ServiceRegistry serviceRegistry =  new StandardServiceRegistryBuilder().build();

        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
        cfg.addAnnotatedClass(ChatGroup.class);
        cfg.addAnnotatedClass(ChatRoom.class);
        cfg.addAnnotatedClass(ChatUser.class);
        cfg.addAnnotatedClass(Group.class);
        cfg.addAnnotatedClass(Message.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserGroup.class);
        cfg.addAnnotatedClass(UserRole.class);
        return cfg.buildSessionFactory(serviceRegistry);
    }
}
