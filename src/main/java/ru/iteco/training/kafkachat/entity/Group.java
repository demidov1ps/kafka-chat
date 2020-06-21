package ru.iteco.training.kafkachat.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Модель сущности "Группа пользователей".
 */
@Entity
@Table(name = "app_group")
public class Group {
    /**
     * Идентификатор группы
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Наименование группы
     */
    @Column(name = "name")
    private String name;

    /**
     * Дата и время создания группы
     */
    @Column(name = "creation_timestamp", updatable = false, nullable = false)
    private LocalDateTime creationTimestamp;

    /**
     * Признак актуальности группы.
     * Если active=false - группа была удален.
     *
     * Создание и удаление группы доступно только роли ADMIN.
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

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
