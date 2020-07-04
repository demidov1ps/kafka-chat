package ru.iteco.training.kafkachat.repository;

import org.hibernate.Session;
import ru.iteco.training.kafkachat.entity.User;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Базовый репозиторий. Реализует CRUD операции.
 * @param <T> тип сущности
 * @param <I> тип идентификатора сущности
 */
public abstract class AbstractRepository<T, I extends Serializable> {

    private final Class<T> entityClass;

    protected AbstractRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T findOne(Session session, I id) {
        return (T) session.get(entityClass, id);
    }

    public List<T> findAll(Session session) {
        return session.createCriteria(entityClass).list();
    }

    public T save(Session session, T entity) {
        return (T) session.merge(entity);
    }

    public void delete(Session session, T entity) {
        session.delete(entity);
    }
}
