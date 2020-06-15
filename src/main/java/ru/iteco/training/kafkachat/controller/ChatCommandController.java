package ru.iteco.training.kafkachat.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.model.ChatMessage;

import java.util.Scanner;

@Component
public class ChatCommandController {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${user.login}")
    private String user;

    @Async
    @EventListener(ContextRefreshedEvent.class)
    public void startChat() {
        System.out.println("Enter 'bm <message>' for broadcast message.");
        System.out.println("Enter 'pm <login> <message>' for private message.");
        System.out.println("Enter 'exit' for exit.");


        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                String command = StringUtils.substring(line, 0, line.indexOf(" "));
                line = StringUtils.substring(line, line.indexOf(" ") + 1, line.length());

                String destination;
                String text;
                if ("bm".equalsIgnoreCase(command)) {
                    destination = "topic.broadcast";
                    text = line;
                } else if ("pm".equalsIgnoreCase(command)) {
                    String receiver = StringUtils.substring(line, 0, line.indexOf(" "));
                    destination = "topic.private." + receiver;
                    text = StringUtils.substring(line, line.indexOf(" ") + 1, line.length());
                } else {
                    continue;
                }

                ChatMessage message = new ChatMessage();
                message.setAuthor(user);
                message.setText(text);
                kafkaTemplate.send(destination, message);
            }
        }

    }
}
