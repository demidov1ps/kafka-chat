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
import ru.iteco.training.kafkachat.entity.ChatGroup;
import ru.iteco.training.kafkachat.entity.ChatGroupId;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, ChatGroupRepository.class})
public class ChatGroupRepositoryTest {
    @Autowired
    private ChatGroupRepository subj;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testFindOne() {
        ChatGroup chatGroup = insertChatGroup();
        ChatGroupId id = newChatGroupId(chatGroup.getChatId(), chatGroup.getGroupId());

        Session session = sessionFactory.openSession();
        assertThat(subj.findOne(session, id), chatGroup(chatGroup));
        session.close();
    }

    @Test
    public void testFindAll() {
        ChatGroup chatGroup1 = insertChatGroup();
        ChatGroup chatGroup2 = insertChatGroup();
        ChatGroup chatGroup3 = insertChatGroup();

        Session session = sessionFactory.openSession();
        assertThat(subj.findAll(session), containsInAnyOrder(
                chatGroup(chatGroup1),
                chatGroup(chatGroup2),
                chatGroup(chatGroup3)));
        session.close();
    }

    @Test
    public void testSave() {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setChatId(UUID.randomUUID());
        chatGroup.setGroupId(UUID.randomUUID());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.save(session, chatGroup);
        session.getTransaction().commit();
        session.close();

        ChatGroupId id = newChatGroupId(chatGroup.getChatId(), chatGroup.getGroupId());
        session = sessionFactory.openSession();
        assertThat((ChatGroup) session.get(ChatGroup.class, id), chatGroup(chatGroup));
        session.close();
    }

    @Test
    public void testDelete() {
        ChatGroup chatGroup = insertChatGroup();
        ChatGroupId id = newChatGroupId(chatGroup.getChatId(), chatGroup.getGroupId());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        subj.delete(session, chatGroup);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        assertNull(session.get(ChatGroup.class, id));
        session.close();
    }

    private ChatGroupId newChatGroupId(UUID chatId, UUID groupId) {
        ChatGroupId id = new ChatGroupId();
        id.setChatId(chatId);
        id.setGroupId(groupId);
        return id;
    }

    private ChatGroup insertChatGroup() {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setChatId(UUID.randomUUID());
        chatGroup.setGroupId(UUID.randomUUID());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(chatGroup);
        session.getTransaction().commit();
        session.close();

        return chatGroup;
    }

    private Matcher<ChatGroup> chatGroup(ChatGroup origin) {
        return new BaseMatcher<ChatGroup>() {
            @Override
            public boolean matches(Object actual) {
                ChatGroup chatGroup = (ChatGroup) actual;
                return origin.getChatId().equals(chatGroup.getChatId())
                        && origin.getGroupId().equals(chatGroup.getGroupId());
            }

            @Override
            public void describeTo(Description description) {
                // TODO
            }
        };
    }
}
