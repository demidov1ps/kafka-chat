package ru.iteco.training.kafkachat.repository;

import org.springframework.stereotype.Repository;
import ru.iteco.training.kafkachat.entity.Settings;

import java.util.UUID;

@Repository
public class SettingsRepository extends AbstractRepository<Settings, UUID> {
}
