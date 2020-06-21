package ru.iteco.training.kafkachat.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ChatGroupId implements Serializable {
    private UUID chatId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatGroupId that = (ChatGroupId) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, groupId);
    }
}
