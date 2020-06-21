package ru.iteco.training.kafkachat.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Принадлежность пользователя группе.
 */
@Entity
@Table(name = "user_group")
@IdClass(UserGroupId.class)
public class UserGroup {
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Id
    @Column(name = "group_id", updatable = false, nullable = false)
    private UUID groupId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }
}
