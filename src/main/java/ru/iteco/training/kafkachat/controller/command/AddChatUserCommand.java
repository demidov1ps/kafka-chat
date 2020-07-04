package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.ChatUserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AddChatUserCommand implements ICommand {
    private static final String command = "add-chat-user";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.USER));

    @Autowired
    private ChatUserService chatUserService;
    @Autowired
    private ConsoleCommandReader reader;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return "Adds user to chat";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@addChatUserCommand.command())" +
            " and @userService.hasRoles(@addChatUserCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        System.out.print("Chat name: ");
        String chatName = reader.nextLine();

        System.out.print("Login: ");
        String login = reader.nextLine();

        chatUserService.addChatUser(chatName, login);
    }
}
