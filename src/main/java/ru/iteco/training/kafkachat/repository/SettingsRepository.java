package ru.iteco.training.kafkachat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.iteco.training.kafkachat.entity.Settings;

import java.util.UUID;

public interface SettingsRepository extends CrudRepository<Settings, UUID> {

}
