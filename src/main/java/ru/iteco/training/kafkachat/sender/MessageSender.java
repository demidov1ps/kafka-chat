package ru.iteco.training.kafkachat.sender;

import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.model.ChatMessage;

public interface MessageSender {

    void send(ChatMessage chatMessage, ChatRoom chat);
}
