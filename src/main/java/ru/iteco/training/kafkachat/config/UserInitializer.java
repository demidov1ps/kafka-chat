package ru.iteco.training.kafkachat.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;
import java.util.Scanner;

public class UserInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter login: ");
        String login = scanner.nextLine();

        Properties properties = new Properties();
        properties.put("user.login", login);
        applicationContext.getEnvironment().getPropertySources()
                .addFirst(new PropertiesPropertySource("initProps", properties));
    }
}
