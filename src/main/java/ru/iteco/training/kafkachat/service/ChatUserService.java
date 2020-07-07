package ru.iteco.training.kafkachat.service;

import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.entity.ChatUser;
import ru.iteco.training.kafkachat.entity.User;
import ru.iteco.training.kafkachat.repository.ChatUserRepository;
import ru.iteco.training.kafkachat.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatUserService {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;

    @Transactional
    public void addChatUser(String chatName, String userLogin) {
        ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
        if (chatRoom == null) {
            return;
        }

        User user = userRepository.findByLogin(userLogin);
        if (user == null) {
            System.out.println("User not exists");
            return;
        }

        ChatUser chatUser = new ChatUser();
        chatUser.setChatId(chatRoom.getId());
        chatUser.setUserId(user.getId());
        chatUserRepository.save(chatUser);
    }

    public Set<String> getChatUsersLogins(UUID chatId) {
        List<ChatUser> chatUsers = chatUserRepository.findByChatId(chatId);
        List<UUID> userIds = chatUsers.stream().map(ChatUser::getUserId).collect(Collectors.toList());
        Iterable<User> users = userRepository.findAllById(userIds);
        // TODO добавить пользователей из группы
        return StreamSupport.stream(users.spliterator(), false).map(User::getLogin).collect(Collectors.toSet());
    }

    public Set<UUID> getChatUsersIds(UUID chatId) {
        List<ChatUser> chatUsers = chatUserRepository.findByChatId(chatId);
        List<UUID> userIds = chatUsers.stream().map(ChatUser::getUserId).collect(Collectors.toList());
        Iterable<User> users = userRepository.findAllById(userIds);
        // TODO добавить пользователей из группы
        return StreamSupport.stream(users.spliterator(), false).map(User::getId).collect(Collectors.toSet());
    }
}
