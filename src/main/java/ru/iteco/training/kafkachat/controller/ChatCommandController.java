package ru.iteco.training.kafkachat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;


@Component
public class ChatCommandController {
    @Autowired
    private ConsoleCommandReader reader;
    @Autowired
    private AbstractApplicationContext context;

    @Value("${user.login}")
    private String user;

    @Async
    @EventListener(ContextRefreshedEvent.class)
    public void startChat() {
        String command = "";
        while (!"exit".equals(command)) {
            command = reader.nextLine();
            context.publishEvent(new ChatCommandEvent(command));
        }

        context.stop();
        context.close();
    }
}
