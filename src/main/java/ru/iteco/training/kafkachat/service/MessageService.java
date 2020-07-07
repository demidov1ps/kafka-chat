package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.kafkachat.entity.*;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.repository.ChatUserRepository;
import ru.iteco.training.kafkachat.repository.MessageRepository;
import ru.iteco.training.kafkachat.repository.UserRepository;
import ru.iteco.training.kafkachat.sender.MessageSender;

import java.util.*;

@Service
public class MessageService {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatUserRepository chatUserRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSender messageSender;

    @Transactional
    public void sendMessage(String chatName, String text) {
        ChatMessage message = new ChatMessage();
        message.setChat(chatName);
        message.setAuthor(userService.getCurrentUser().getLogin());
        message.setText(text);

        ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
        if (chatRoom == null) {
            return;
        }

        Message dbMessage = new Message();
        dbMessage.setAuthorUserId(userService.getCurrentUser().getId());
        dbMessage.setChatRoomId(chatRoom.getId());
        dbMessage.setCreationTimestamp(new Date());
        dbMessage.setText(text);
        messageRepository.save(dbMessage);

        messageSender.send(message, chatRoom);
    }

    public List<ChatMessage> getMessagesForChat(String chatName) {
        ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
        if (chatRoom == null) {
            return new ArrayList<>();
        }
        List<Message> messages = messageRepository.findByChatRoomIdOrderByCreationTimestamp(chatRoom.getId());
        List<ChatMessage> result = new ArrayList<>();
        for (Message message : messages) {
            ChatMessage cm = new ChatMessage();
            cm.setChat(chatName);
            cm.setText(message.getText());
            cm.setCreationTimestamp(message.getCreationTimestamp());
            userRepository.findById(message.getAuthorUserId()).ifPresent(user -> cm.setAuthor(user.getLogin()));
            result.add(cm);
        }

        return result;
    }
}
