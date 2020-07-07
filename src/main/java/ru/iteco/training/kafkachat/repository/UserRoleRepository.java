package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.UserRole;
import ru.iteco.training.kafkachat.entity.UserRoleId;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {

    List<UserRole> findByUserId(UUID userId);
}
