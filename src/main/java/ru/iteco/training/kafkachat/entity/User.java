package ru.iteco.training.kafkachat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Модель сущности "Пользователь".
 */
@Entity
@Table(name = "app_user")
public class User {
    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Логин пользователя
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Имя пользователя
     */
    @Column(name = "name")
    private String name;

    /**
     * Дата и время создания пользователя
     */
    @Column(name = "creation_timestamp", updatable = false, nullable = false)
    private Date creationTimestamp;

    /**
     * Признак актуальности пользователя.
     * Если active=false - пользователь был удален.
     *
     * Создание и удаление пользователей доступно только роли ADMIN.
     */
    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
