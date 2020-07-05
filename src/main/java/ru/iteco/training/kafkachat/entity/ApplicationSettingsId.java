package ru.iteco.training.kafkachat.entity;

import ru.iteco.training.kafkachat.enums.SettingKey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ApplicationSettingsId implements Serializable {
    private SettingKey key;
    private UUID userId;

    public SettingKey getKey() {
        return key;
    }

    public void setKey(SettingKey key) {
        this.key = key;
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
        ApplicationSettingsId that = (ApplicationSettingsId) o;
        return key == that.key &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, userId);
    }
}
