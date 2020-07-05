package ru.iteco.training.kafkachat.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.service.ChatUserService;

import java.util.Set;

@Component
public class KafkaMessageSender implements MessageSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ChatUserService chatUserService;

    @Override
    public void send(ChatMessage chatMessage, ChatRoom chat) {
        if (chat.getPrivateChat()) {
            Set<String> receivers = chatUserService.getChatUsersLogins(chat.getId());
            for (String receiver : receivers) {
                kafkaTemplate.send("topic.private." + receiver, chatMessage);
            }
        } else {
            kafkaTemplate.send("topic.broadcast", chatMessage);
        }
    }
}
