package ru.iteco.training.kafkachat.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.enums.MessageTransport;
import ru.iteco.training.kafkachat.model.ChatMessage;

@Primary
@Component
public class SwitchMessageSender implements MessageSender {
    @Value("${message.transport}")
    private MessageTransport transport;
    @Autowired
    private KafkaMessageSender kafkaMessageSender;
    @Autowired
    private SocketMessageSender socketMessageSender;

    @Override
    public void send(ChatMessage chatMessage, ChatRoom chat) {
        switch (transport) {
            case KAFKA:
                kafkaMessageSender.send(chatMessage, chat);
                break;
            case SOCKET:
                socketMessageSender.send(chatMessage, chat);
                break;
        }
    }
}
