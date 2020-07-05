package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.Settings;
import ru.iteco.training.kafkachat.enums.SettingKey;
import ru.iteco.training.kafkachat.repository.SettingsRepository;

import java.util.List;

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

    public void deleteSetting(Settings setting) {
        hibernateService.withTransaction(session -> {
            settingsRepository.delete(session, setting);
            return null;
        });
    }

    public List<ApplicationSettings> getApplicationSettingsByKeys(List<SettingKey> keys) {
        return hibernateService.withSession(session -> {
            return settingsRepository.findApplicationSettingsByKeys(session, keys);
        });
    }
}
