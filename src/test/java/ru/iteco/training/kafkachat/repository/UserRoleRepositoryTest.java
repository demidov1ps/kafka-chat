package ru.iteco.training.kafkachat.repository;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.iteco.training.kafkachat.config.HibernateConfig;
import ru.iteco.training.kafkachat.entity.UserRole;
import ru.iteco.training.kafkachat.entity.UserRoleId;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, UserRoleRepository.class})
public class UserRoleRepositoryTest {
    @Autowired
    private UserRoleRepository subj;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testFindOne() {
        UserRole userRole = insertUserRole();
        UserRoleId id = newUserRoleId(userRole.getUserId(), userRole.getRole());

        Session session = sessionFactory.openSession();
        assertThat(subj.findOne(session, id), userRole(userRole));
        session.close();
    }

    @Test
    public void testFindAll() {
        UserRole userRole1 = insertUserRole();
        UserRole userRole2 = insertUserRole();
        UserRole userRole3 = insertUserRole();

        Session session = sessionFactory.openSession();
        assertThat(subj.findAll(session), containsInAnyOrder(
                userRole(userRole1),
                userRole(userRole2),
                userRole(userRole3)));
        session.close();
    }

    @Test
    public void testSave() {
        UserRole userRole = new UserRole();
        userRole.setUserId(UUID.randomUUID());
        userRole.setRole(ru.iteco.training.kafkachat.enums.UserRole.ADMIN);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.save(session, userRole);
        session.getTransaction().commit();
        session.close();

        UserRoleId id = newUserRoleId(userRole.getUserId(), userRole.getRole());
        session = sessionFactory.openSession();
        assertThat((UserRole) session.get(UserRole.class, id), userRole(userRole));
        session.close();
    }

    @Test
    public void testDelete() {
        UserRole userRole = insertUserRole();
        UserRoleId id = newUserRoleId(userRole.getUserId(), userRole.getRole());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.delete(session, userRole);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertNull(session.get(UserRole.class, id));
        session.close();
    }

    @Test
    public void testGetRolesForUser() {
        UUID userId = UUID.randomUUID();
        UserRole role1 = insertUserRole(userId, ru.iteco.training.kafkachat.enums.UserRole.USER);
        UserRole role2 = insertUserRole(userId, ru.iteco.training.kafkachat.enums.UserRole.ADMIN);

        Session session = sessionFactory.openSession();
        assertThat(subj.getRolesForUser(session, userId), containsInAnyOrder(userRole(role1), userRole(role2)));
        session.close();
    }

    private UserRoleId newUserRoleId(UUID userId, ru.iteco.training.kafkachat.enums.UserRole role) {
        UserRoleId id = new UserRoleId();
        id.setUserId(userId);
        id.setRole(role);
        return id;
    }

    private UserRole insertUserRole() {
        return insertUserRole(UUID.randomUUID(), ru.iteco.training.kafkachat.enums.UserRole.USER);
    }

    private UserRole insertUserRole(UUID userId, ru.iteco.training.kafkachat.enums.UserRole role) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRole(role);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(userRole);
        session.getTransaction().commit();
        session.close();

        return userRole;
    }

    private Matcher<UserRole> userRole(UserRole origin) {
        return new BaseMatcher<UserRole>() {
            @Override
            public boolean matches(Object actual) {
                UserRole userRole = (UserRole) actual;
                return origin.getRole().equals(userRole.getRole())
                        && origin.getUserId().equals(userRole.getUserId());
            }

            @Override
            public void describeTo(Description description) {
                // TODO
            }
        };
    }
}
