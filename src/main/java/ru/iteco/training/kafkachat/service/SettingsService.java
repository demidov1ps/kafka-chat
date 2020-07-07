package ru.iteco.training.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.Settings;
import ru.iteco.training.kafkachat.enums.SettingKey;
import ru.iteco.training.kafkachat.repository.ApplicationSettingsRepository;
import ru.iteco.training.kafkachat.repository.SettingsRepository;

import java.util.List;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ApplicationSettingsRepository applicationSettingsRepository;

    @Transactional
    public void updateSetting(Settings setting) {
        settingsRepository.save(setting);
    }

    @Transactional
    public void deleteSetting(Settings setting) {
        settingsRepository.delete(setting);
    }

    public List<ApplicationSettings> getApplicationSettingsByKeys(List<SettingKey> keys) {
        return applicationSettingsRepository.findByKeyIn(keys);
    }
}
