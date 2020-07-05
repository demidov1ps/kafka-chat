package ru.iteco.training.kafkachat.entity;

import ru.iteco.training.kafkachat.enums.SettingKey;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_settings")
@IdClass(ApplicationSettingsId.class)
public class ApplicationSettings extends Settings {
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    /**
     * Ключ настройки
     */
    @Id
    @AttributeOverride(name = "key", column = @Column(name = "key"))
    @Enumerated(EnumType.STRING)
    private SettingKey key;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public SettingKey getKey() {
        return key;
    }

    @Override
    public void setKey(SettingKey key) {
        this.key = key;
    }
}
