package ru.iteco.training.kafkachat.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ChatUserId implements Serializable {
    private UUID chatId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUserId that = (ChatUserId) o;
        return chatId.equals(that.chatId) &&
                userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }
}
