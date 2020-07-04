package ru.iteco.training.kafkachat.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.entity.ChatUser;
import ru.iteco.training.kafkachat.entity.ChatUserId;
import ru.iteco.training.kafkachat.entity.User;
import ru.iteco.training.kafkachat.model.ChatRooms;
import ru.iteco.training.kafkachat.repository.ChatRoomRepository;
import ru.iteco.training.kafkachat.repository.ChatUserRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    @Autowired
    private HibernateService hibernateService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;

    public void createChatRoom(String name, boolean isPrivate) {
        User currentUser = userService.getCurrentUser();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setPrivateChat(isPrivate);
        chatRoom.setCreationTimestamp(new Date());

        ChatUser chatUser = new ChatUser();
        chatUser.setUserId(currentUser.getId());

        hibernateService.withTransaction(session -> {
            ChatRoom saved = chatRoomRepository.save(session, chatRoom);
            chatUser.setChatId(saved.getId());
            chatUserRepository.save(session, chatUser);
            return null;
        });
    }

    public ChatRooms getAvailableChatRooms() {
        User currentUser = userService.getCurrentUser();
        return hibernateService.withSession(session -> {
            List<ChatRoom> privateChats = chatRoomRepository.getPrivateChatRoomsForUser(session, currentUser.getId());
            List<ChatRoom> publicChats = chatRoomRepository.getPublicChatRooms(session);
            ChatRooms result = new ChatRooms();
            result.setPrivateChats(privateChats.stream().map(ChatRoom::getName).collect(Collectors.toList()));
            result.setPublicChats(publicChats.stream().map(ChatRoom::getName).collect(Collectors.toList()));
            return result;
        });
    }

    public ChatRoom getChatIfAvailable(String chatName) {
        return hibernateService.withSession(session -> {
            ChatRoom chatRoom = chatRoomRepository.findByName(session, chatName);
            if (chatRoom == null) {
                System.out.println("Chat not found");
                return null;
            }
            if (chatRoom.getPrivateChat()) {
                ChatUserId chatUserId = new ChatUserId();
                chatUserId.setChatId(chatRoom.getId());
                chatUserId.setUserId(userService.getCurrentUser().getId());
                ChatUser chatUser = chatUserRepository.findOne(session, chatUserId);
                // TODO проверить группу пользователей.
                if (chatUser == null) {
                    System.out.println("Chat not available");
                    return null;
                }
            }

            return chatRoom;
        });
    }
}
