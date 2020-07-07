package ru.iteco.training.kafkachat.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.ApplicationSettingsId;
import ru.iteco.training.kafkachat.enums.SettingKey;

public interface ApplicationSettingsRepository extends CrudRepository<ApplicationSettings, ApplicationSettingsId> {

    List<ApplicationSettings> findByKeyIn(List<SettingKey> keys);
}
