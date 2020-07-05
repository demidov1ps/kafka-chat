package ru.iteco.training.kafkachat.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.DeveloperSettings;
import ru.iteco.training.kafkachat.entity.Settings;
import ru.iteco.training.kafkachat.enums.SettingKey;
import ru.iteco.training.kafkachat.service.SettingsService;
import ru.iteco.training.kafkachat.service.UserService;

@Component
public class SettingsInitializer {
    @Autowired
    private UserService userService;
    @Autowired
    private SettingsService settingsService;

    @Order(2)
    @EventListener(ContextRefreshedEvent.class)
    public void initSettings() {
        Settings email = new DeveloperSettings();
        email.setKey(SettingKey.DEV_EMAIL);
        email.setValue("dev@mail.com");
        settingsService.updateSetting(email);

        Settings name = new DeveloperSettings();
        email.setKey(SettingKey.DEV_NAME);
        email.setValue("Cool developer");
        settingsService.updateSetting(email);

        ApplicationSettings os = new ApplicationSettings();
        os.setUserId(userService.getCurrentUser().getId());
        os.setKey(SettingKey.APP_OS);
        os.setValue(System.getProperty("os.name"));
        settingsService.updateSetting(os);

        ApplicationSettings memory = new ApplicationSettings();
        memory.setUserId(userService.getCurrentUser().getId());
        memory.setKey(SettingKey.APP_MEMORY);
        memory.setValue(String.valueOf(Runtime.getRuntime().maxMemory()));
        settingsService.updateSetting(memory);
    }
}
