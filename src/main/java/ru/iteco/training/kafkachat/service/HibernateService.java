package ru.iteco.training.kafkachat.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HibernateService {
    @Autowired
    private SessionFactory sessionFactory;

    public <T> T withSession(SessionCallback<T> callback) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return callback.execute(session);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public <T> T withTransaction(SessionCallback<T> callback) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            T result = callback.execute(session);
            session.getTransaction().commit();
            return result;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
