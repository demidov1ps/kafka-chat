package ru.iteco.training.kafkachat.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.iteco.training.kafkachat.entity.*;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.repository.ChatUserRepository;
import ru.iteco.training.kafkachat.repository.MessageRepository;
import ru.iteco.training.kafkachat.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private HibernateService hibernateService;
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
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String chatName, String text) {
        ChatMessage message = new ChatMessage();
        message.setChat(chatName);
        message.setAuthor(userService.getCurrentUser().getLogin());
        message.setText(text);

        hibernateService.withTransaction(session -> {
            ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
            if (chatRoom == null) {
                return null;
            }

            Message dbMessage = new Message();
            dbMessage.setAuthorUserId(userService.getCurrentUser().getId());
            dbMessage.setChatRoomId(chatRoom.getId());
            dbMessage.setCreationTimestamp(new Date());
            dbMessage.setText(text);
            messageRepository.save(session, dbMessage);

            if (chatRoom.getPrivateChat()) {
                Set<String> receivers = getChatUsers(session, chatRoom.getId());
                for (String receiver : receivers) {
                    kafkaTemplate.send("topic.private." + receiver, message);
                }
            } else {
                kafkaTemplate.send("topic.broadcast", message);
            }

            return null;
        });
    }

    public List<ChatMessage> getMessagesForChat(String chatName) {
        return hibernateService.withSession(session -> {
           ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
           if (chatRoom == null) {
               return new ArrayList<>();
           }
           List<Message> messages = messageRepository.findByChatRoomId(session, chatRoom.getId());
           List<ChatMessage> result = new ArrayList<>();
           for (Message message : messages) {
               ChatMessage cm = new ChatMessage();
               cm.setChat(chatName);
               cm.setText(message.getText());
               cm.setCreationTimestamp(message.getCreationTimestamp());
               cm.setAuthor(userRepository.findOne(session, message.getAuthorUserId()).getLogin());
               result.add(cm);
           }

           return result;
        });
    }

    private Set<String> getChatUsers(Session session, UUID chatId) {
        List<ChatUser> chatUsers = chatUserRepository.findByChatId(session, chatId);
        List<UUID> userIds = chatUsers.stream().map(ChatUser::getUserId).collect(Collectors.toList());
        List<User> users = userRepository.findByIds(session, userIds);
        // TODO добавить пользователей из группы
        return users.stream().map(User::getLogin).collect(Collectors.toSet());
    }
}
