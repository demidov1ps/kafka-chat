package ru.iteco.training.kafkachat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.iteco.training.kafkachat.entity.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, UUID> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.privateChat = true "
            + " and cr.id IN (SELECT chatId FROM ChatUser cu WHERE cu.userId = :userId) "
            + " ORDER BY cr.name")
    List<ChatRoom> findPrivateChatRoomsForUser(@Param("userId") UUID userId);

    List<ChatRoom> findByPrivateChatFalse();

    ChatRoom findByName(String name);
}
