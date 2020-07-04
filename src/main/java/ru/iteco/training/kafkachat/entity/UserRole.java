package ru.iteco.training.kafkachat.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Перечень ролей пользователя.
 */
@Entity
@Table(name = "user_role")
@IdClass(UserRoleId.class)
public class UserRole {
    /**
     * Идентификатор пользователя
     */
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    /**
     * Роль пользователя
     */
    @Id
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ru.iteco.training.kafkachat.enums.UserRole role = ru.iteco.training.kafkachat.enums.UserRole.USER;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ru.iteco.training.kafkachat.enums.UserRole getRole() {
        return role;
    }

    public void setRole(ru.iteco.training.kafkachat.enums.UserRole role) {
        this.role = role;
    }
}
