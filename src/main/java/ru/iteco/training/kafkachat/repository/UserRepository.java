package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByLogin(String login);
}
