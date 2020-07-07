package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.ChatGroup;
import ru.iteco.training.kafkachat.entity.ChatGroupId;

public interface ChatGroupRepository extends CrudRepository<ChatGroup, ChatGroupId> {

}
