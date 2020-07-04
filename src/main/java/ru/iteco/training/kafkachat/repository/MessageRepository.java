package ru.iteco.training.kafkachat.repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.Message;

import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepository extends AbstractRepository<Message, UUID> {

    public List<Message> findByChatRoomId(Session session, UUID chatRoomId) {
        Query query = session.createQuery("FROM Message m WHERE m.chatRoomId = :chatRoomId ORDER BY m.creationTimestamp");
        query.setParameter("chatRoomId", chatRoomId);
        return query.list();
    }
}
