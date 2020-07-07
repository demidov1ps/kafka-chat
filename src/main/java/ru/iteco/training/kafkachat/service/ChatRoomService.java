package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private UserService userService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;

    @Transactional
    public void createChatRoom(String name, boolean isPrivate) {
        User currentUser = userService.getCurrentUser();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setPrivateChat(isPrivate);
        chatRoom.setCreationTimestamp(new Date());

        ChatUser chatUser = new ChatUser();
        chatUser.setUserId(currentUser.getId());

        ChatRoom saved = chatRoomRepository.save(chatRoom);
        chatUser.setChatId(saved.getId());
        chatUserRepository.save(chatUser);
    }

    public ChatRooms getAvailableChatRooms() {
        User currentUser = userService.getCurrentUser();
        List<ChatRoom> privateChats = chatRoomRepository.findPrivateChatRoomsForUser(currentUser.getId());
        List<ChatRoom> publicChats = chatRoomRepository.findByPrivateChatFalse();
        ChatRooms result = new ChatRooms();
        result.setPrivateChats(privateChats.stream().map(ChatRoom::getName).collect(Collectors.toList()));
        result.setPublicChats(publicChats.stream().map(ChatRoom::getName).collect(Collectors.toList()));
        return result;
    }

    public ChatRoom getChatIfAvailable(String chatName) {
        ChatRoom chatRoom = chatRoomRepository.findByName(chatName);
        if (chatRoom == null) {
            System.out.println("Chat not found");
            return null;
        }
        if (chatRoom.getPrivateChat()) {
            ChatUserId chatUserId = new ChatUserId();
            chatUserId.setChatId(chatRoom.getId());
            chatUserId.setUserId(userService.getCurrentUser().getId());
            // TODO проверить группу пользователей.
            if (!chatUserRepository.existsById(chatUserId)) {
                System.out.println("Chat not available");
                return null;
            }
        }

        return chatRoom;
    }
}
