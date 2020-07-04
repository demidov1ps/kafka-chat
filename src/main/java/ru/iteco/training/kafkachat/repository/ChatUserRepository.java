package ru.iteco.training.kafkachat.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.ChatUser;
import ru.iteco.training.kafkachat.entity.ChatUserId;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatUserRepository extends AbstractRepository<ChatUser, ChatUserId> {

    public List<ChatUser> findByChatId(Session session, UUID chatId) {
        Criteria criteria = session.createCriteria(ChatUser.class);
        criteria.add(Restrictions.eq("chatId", chatId));
        return criteria.list();
    }
}
