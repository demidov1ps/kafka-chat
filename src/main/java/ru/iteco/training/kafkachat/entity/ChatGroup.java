package ru.iteco.training.kafkachat.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Связь группы пользователей с чатом.
 */
@Entity
@Table(name = "chat_group")
@IdClass(ChatGroupId.class)
public class ChatGroup {
    @Id
    @Column(name = "chat_id", updatable = false, nullable = false)
    private UUID chatId;

    @Id
    @Column(name = "group_id", updatable = false, nullable = false)
    private UUID groupId;

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }
}
