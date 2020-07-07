package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.ChatUser;
import ru.iteco.training.kafkachat.entity.ChatUserId;

import java.util.List;
import java.util.UUID;

public interface ChatUserRepository extends CrudRepository<ChatUser, ChatUserId> {

    List<ChatUser> findByChatId(UUID chatId);
}
