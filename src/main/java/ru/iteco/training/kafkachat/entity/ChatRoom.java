package ru.iteco.training.kafkachat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Модель сущности "Чат".
 */
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    /**
     * Идентификатор чата
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Наименование чата
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Дата и время создания чата
     */
    @Column(name = "creation_timestamp", updatable = false, nullable = false)
    private Date creationTimestamp;

    /**
     * Признак приватного чата.
     * Добавление пользователей в приватный чат возможно только участниками чата.
     * Пользователь с ролью USER может создавать только приватные чаты.
     *
     * Пользователь с ролью ADMIN может создавать публичные чаты.
     * Пользователь с ролью USER может присоединиться к любому публичному чату.
     */
    @Column(name = "private_сhat", nullable = false)
    private Boolean privateChat = Boolean.FALSE;

    /**
     * Признак актуальности чата.
     * Если active=false - чат был удален.
     */
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(Boolean privateChat) {
        this.privateChat = privateChat;
    }
}
