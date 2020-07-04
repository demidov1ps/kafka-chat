package ru.iteco.training.kafkachat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Модель сущности "Сообщение чата".
 */
@Entity
@Table(name = "chat_message")
public class Message {
    /**
     * Идентификатор сообщения
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Идентификатор чата
     */
    @Column(name = "chat_room_id", updatable = false, nullable = false)
    private UUID chatRoomId;

    /**
     * Идентификатор пользователя, автора сообщения
     */
    @Column(name = "author_user_id", updatable = false, nullable = false)
    private UUID authorUserId;

    /**
     * Дата и время создания сообщения
     */
    @Column(name = "creation_timestamp", updatable = false, nullable = false)
    private Date creationTimestamp;

    /**
     * Текст сообщения
     */
    @Column(name = "text", columnDefinition="TEXT")
    private String text;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(UUID chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public UUID getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(UUID authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
