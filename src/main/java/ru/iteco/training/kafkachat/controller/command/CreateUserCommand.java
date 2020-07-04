package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CreateUserCommand implements ICommand {
    private static final String command = "create-user";
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
        return "Create new application user";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@createUserCommand.command())" +
            " and @userService.hasRoles(@createUserCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        System.out.print("Login: ");
        String login = reader.nextLine();

        System.out.print("Name: ");
        String name = reader.nextLine();

        System.out.print("Role [ADMIN, USER]: ");
        String role = reader.nextLine();

        userService.createUser(login, name, UserRole.valueOf(role));
    }
}
