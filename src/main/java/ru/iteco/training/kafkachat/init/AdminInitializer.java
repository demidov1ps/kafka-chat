package ru.iteco.training.kafkachat.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.UserService;

@Component
public class AdminInitializer {
    @Autowired
    private UserService userService;
    @Value("${admin.login}")
    private String adminLogin;

    @Order(0)
    @EventListener(ContextRefreshedEvent.class)
    public void addDefaultAdminIfNotExists() {
        if (!userService.userExists(adminLogin)) {
            userService.createUser(adminLogin, "Администратор", UserRole.ADMIN, UserRole.USER);
        }
    }
}
