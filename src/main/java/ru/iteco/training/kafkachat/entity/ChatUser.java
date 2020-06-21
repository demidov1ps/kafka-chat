package ru.iteco.training.kafkachat.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Связь пользователя с чатом.
 */
@Entity
@Table(name = "chat_user")
@IdClass(ChatUserId.class)
public class ChatUser {
    @Id
    @Column(name = "chat_id", updatable = false, nullable = false)
    private UUID chatId;

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
