package ru.iteco.training.kafkachat.repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.Settings;
import ru.iteco.training.kafkachat.enums.SettingKey;

import java.util.List;
import java.util.UUID;

@Repository
public class SettingsRepository extends AbstractRepository<Settings, UUID> {

    public List<ApplicationSettings> findApplicationSettingsByKeys(Session session, List<SettingKey> keys) {
        Query query = session.createQuery("FROM ApplicationSettings s WHERE s.key IN :keys");
        query.setParameterList("keys", keys);
        return query.list();
    }
}
