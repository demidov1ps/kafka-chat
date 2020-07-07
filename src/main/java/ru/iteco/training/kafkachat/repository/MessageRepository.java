package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findByChatRoomIdOrderByCreationTimestamp(UUID chatRoomId);
}
