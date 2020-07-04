package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.MessageService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SendMessageCommand implements ICommand {
    private static final String command = "send-message";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.USER));


    @Autowired
    private MessageService messageService;
    @Autowired
    private ConsoleCommandReader reader;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return "Send message";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@sendMessageCommand.command())" +
            " and @userService.hasRoles(@sendMessageCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        System.out.print("Chat name: ");
        String chatName = reader.nextLine();

        System.out.print("Message text: ");
        String text = reader.nextLine();

        messageService.sendMessage(chatName, text);
    }
}
