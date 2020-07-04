package ru.iteco.training.kafkachat.repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends AbstractRepository<User, UUID> {

    public User findByLogin(Session session, String login) {
        Query query = session.createQuery("FROM User u WHERE u.login = :login");
        query.setParameter("login", login);
        List<User> result = query.list();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<User> findByIds(Session session, List<UUID> ids) {
        Query query = session.createQuery("FROM User u WHERE u.id IN :ids");
        query.setParameterList("ids", ids);
        return query.list();
    }
}
