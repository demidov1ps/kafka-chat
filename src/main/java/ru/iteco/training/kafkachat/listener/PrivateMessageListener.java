package ru.iteco.training.kafkachat.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.model.ChatMessage;

@Component
public class PrivateMessageListener {

    @KafkaListener(topics = "topic.private.${user.login}")
    public void onMessage(ChatMessage message) {
        System.out.println(
                String.format("Received private message from %s: %s", message.getAuthor(), message.getText()));
    }
}
