package ru.iteco.training.kafkachat.repository;

import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.ChatGroup;
import ru.iteco.training.kafkachat.entity.ChatGroupId;

@Repository
public class ChatGroupRepository extends AbstractRepository<ChatGroup, ChatGroupId> {

}
