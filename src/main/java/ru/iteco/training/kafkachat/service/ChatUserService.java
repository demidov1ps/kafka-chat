package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.entity.ChatUser;
import ru.iteco.training.kafkachat.entity.User;
import ru.iteco.training.kafkachat.repository.ChatUserRepository;
import ru.iteco.training.kafkachat.repository.UserRepository;

@Service
public class ChatUserService {
    @Autowired
    private HibernateService hibernateService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;

    public void addChatUser(String chatName, String userLogin) {
        ChatRoom chatRoom = chatRoomService.getChatIfAvailable(chatName);
        if (chatRoom == null) {
            return;
        }

        hibernateService.withTransaction(session -> {
            User user = userRepository.findByLogin(session, userLogin);
            if (user == null) {
                System.out.println("User not exists");
                return null;
            }

            ChatUser chatUser = new ChatUser();
            chatUser.setChatId(chatRoom.getId());
            chatUser.setUserId(user.getId());
            chatUserRepository.save(session, chatUser);

            return null;
        });
    }
}
