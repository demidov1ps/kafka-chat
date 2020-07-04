package ru.iteco.training.kafkachat.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.entity.ChatUser;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatRoomRepository extends AbstractRepository<ChatRoom, UUID> {

    public List<ChatRoom> getPrivateChatRoomsForUser(Session session, UUID userId) {
        DetachedCriteria subCriteria = DetachedCriteria.forClass(ChatUser.class);
        subCriteria.add(Restrictions.eq("userId", userId));
        subCriteria.setProjection(Projections.property("chatId"));

        Criteria criteria = session.createCriteria(ChatRoom.class);
        criteria.add(Restrictions.eq("privateChat", true));
        criteria.add(Subqueries.propertyIn("id", subCriteria));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    public List<ChatRoom> getPublicChatRooms(Session session) {
        Criteria criteria = session.createCriteria(ChatRoom.class);
        criteria.add(Restrictions.eq("privateChat", false));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    public ChatRoom findByName(Session session, String name) {
        Criteria criteria = session.createCriteria(ChatRoom.class);
        criteria.add(Restrictions.eq("name", name));
        List<ChatRoom> result = criteria.list();
        return result.isEmpty() ? null : result.get(0);
    }
}
