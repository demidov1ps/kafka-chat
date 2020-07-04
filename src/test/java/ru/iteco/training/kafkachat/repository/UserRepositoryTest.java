package ru.iteco.training.kafkachat.repository;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.iteco.training.kafkachat.config.HibernateConfig;
import ru.iteco.training.kafkachat.entity.User;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, UserRepository.class})
public class UserRepositoryTest {
    @Autowired
    private UserRepository subj;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testFindOne() {
        User user = insertUser();

        Session session = sessionFactory.openSession();
        assertThat(subj.findOne(session, user.getId()), user(user));
        session.close();
    }

    @Test
    public void testFindAll() {
        User user1 = insertUser();
        User user2 = insertUser();
        User user3 = insertUser();

        Session session = sessionFactory.openSession();
        assertThat(subj.findAll(session), containsInAnyOrder(
                user(user1),
                user(user2),
                user(user3)));
        session.close();
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setName("Test User");
        user.setLogin("test");
        user.setCreationTimestamp(new Date());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User saved = subj.save(session, user);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertThat((User) session.get(User.class, saved.getId()), user(user));
        session.close();
    }

    @Test
    public void testDelete() {
        User user = insertUser();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.delete(session, user);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertNull(session.get(User.class, user.getId()));
        session.close();
    }

    @Test
    public void testFindByLogin() {
        User user1 = insertUser();
        User user2 = insertUser();
        User user3 = insertUser();

        Session session = sessionFactory.openSession();
        assertThat(subj.findByLogin(session, user2.getLogin()), user(user2));
        session.close();
    }

    @Test
    public void testFindByIds() {
        User user1 = insertUser();
        User user2 = insertUser();
        User user3 = insertUser();

        Session session = sessionFactory.openSession();
        assertThat(subj.findByIds(session, Arrays.asList(user1.getId(), user3.getId())),
                containsInAnyOrder(user(user1), user(user3)));
        session.close();
    }

    private User insertUser() {
        User user = new User();
        user.setCreationTimestamp(new Date());
        user.setLogin("test-" + UUID.randomUUID().toString());
        user.setName("Test user");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();

        return user;
    }

    private Matcher<User> user(User origin) {
        return new BaseMatcher<User>() {
            @Override
            public boolean matches(Object actual) {
                User user = (User) actual;
                return origin.getActive().equals(user.getActive())
                        && origin.getLogin().equals(user.getLogin())
                        && origin.getCreationTimestamp().equals(user.getCreationTimestamp())
                        && origin.getName().equals(user.getName());
            }

            @Override
            public void describeTo(Description description) {
                // TODO
            }
        };
    }
}