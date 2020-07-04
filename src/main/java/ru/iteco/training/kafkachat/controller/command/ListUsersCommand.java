package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.model.ChatRooms;
import ru.iteco.training.kafkachat.service.ChatRoomService;
import ru.iteco.training.kafkachat.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ListUsersCommand implements ICommand {
    private static final String command = "list-users";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN));

    @Autowired
    private UserService userService;
    @Autowired
    private ConsoleCommandReader reader;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return "Show all users";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@listUsersCommand.command())" +
            " and @userService.hasRoles(@listUsersCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        List<String> logins = userService.getAllUsersLogins();
        System.out.println("users:");
        for (String login : logins) {
            System.out.println("\t" + login);
        }
    }
}
