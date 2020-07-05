package ru.iteco.training.kafkachat.entity;

import ru.iteco.training.kafkachat.enums.SettingKey;

import javax.persistence.*;

@MappedSuperclass
public class Settings {

    /**
     * Ключ настройки
     */
    @Id
    @Column(name = "key", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettingKey key;

    /**
     * Значение настройки
     */
    @Column(name = "value")
    private String value;

    public SettingKey getKey() {
        return key;
    }

    public void setKey(SettingKey key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
