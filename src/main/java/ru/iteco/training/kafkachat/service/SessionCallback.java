package ru.iteco.training.kafkachat.service;

import org.hibernate.Session;

public interface SessionCallback<T> {
    T execute(Session session);
}
