package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iteco.training.kafkachat.entity.Settings;
import ru.iteco.training.kafkachat.repository.SettingsRepository;

@Service
public class SettingsService {
    @Autowired
    private HibernateService hibernateService;
    @Autowired
    private SettingsRepository settingsRepository;

    public void updateSetting(Settings setting) {
        hibernateService.withTransaction(session -> {
            settingsRepository.save(session, setting);
            return null;
        });
    }
}
