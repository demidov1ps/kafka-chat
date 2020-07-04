package ru.iteco.training.kafkachat.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.UserRole;
import ru.iteco.training.kafkachat.entity.UserRoleId;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRoleRepository extends AbstractRepository<UserRole, UserRoleId> {

    public List<UserRole> getRolesForUser(Session session, UUID userId) {
        Criteria criteria = session.createCriteria(UserRole.class);
        criteria.add(Restrictions.eq("userId", userId));
        return criteria.list();
    }
}
