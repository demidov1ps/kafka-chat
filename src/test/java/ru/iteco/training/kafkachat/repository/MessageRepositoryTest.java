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
import ru.iteco.training.kafkachat.entity.Message;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, MessageRepository.class})
public class MessageRepositoryTest {
    @Autowired
    private MessageRepository subj;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testFindOne() {
        Message message = insertMessage();

        Session session = sessionFactory.openSession();
        assertThat(subj.findOne(session, message.getId()), message(message));
        session.close();
    }

    @Test
    public void testFindAll() {
        Message message1 = insertMessage();
        Message message2 = insertMessage();
        Message message3 = insertMessage();

        Session session = sessionFactory.openSession();
        assertThat(subj.findAll(session), containsInAnyOrder(
                message(message1),
                message(message2),
                message(message3)));
        session.close();
    }

    @Test
    public void testSave() {
        Message message = new Message();
        message.setText("test text");
        message.setCreationTimestamp(new Date());
        message.setChatRoomId(UUID.randomUUID());
        message.setAuthorUserId(UUID.randomUUID());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Message saved = subj.save(session, message);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertThat((Message) session.get(Message.class, saved.getId()), message(message));
        session.close();
    }

    @Test
    public void testDelete() {
        Message message = insertMessage();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.delete(session, message);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertNull(session.get(Message.class, message.getId()));
        session.close();
    }

    @Test
    public void testFindByChatRoomId() {
        Date startDate = new Date();
        UUID chatId = UUID.randomUUID();

        Message m1 = insertMessage(chatId, startDate);
        Message m2 = insertMessage(chatId, new Date(startDate.getTime() + 100));
        Message m3 = insertMessage(chatId, new Date(startDate.getTime() - 100));

        Session session = sessionFactory.openSession();
        assertThat(subj.findByChatRoomId(session, chatId), contains(message(m3), message(m1), message(m2)));
        session.close();
    }

    private Message insertMessage() {
        return insertMessage(UUID.randomUUID(), new Date());
    }

    private Message insertMessage(UUID chatRoomId, Date creationTimestamp) {
        Message message = new Message();
        message.setText("test text");
        message.setCreationTimestamp(creationTimestamp);
        message.setChatRoomId(chatRoomId);
        message.setAuthorUserId(UUID.randomUUID());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(message);
        session.getTransaction().commit();
        session.close();

        return message;
    }

    private Matcher<Message> message(Message origin) {
        return new BaseMatcher<Message>() {
            @Override
            public boolean matches(Object actual) {
                Message message = (Message) actual;
                return origin.getAuthorUserId().equals(message.getAuthorUserId())
                        && origin.getCreationTimestamp().equals(message.getCreationTimestamp())
                        && origin.getText().equals(message.getText())
                        && origin.getChatRoomId().equals(message.getChatRoomId());
            }

            @Override
            public void describeTo(Description description) {
                // TODO
            }
        };
    }
}
