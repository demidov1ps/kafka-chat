package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.service.MessageService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OpenChatCommand implements ICommand {
    private static final String command = "open-chat";
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
        return "Show all messages in existing chat";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@openChatCommand.command())" +
            " and @userService.hasRoles(@openChatCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        System.out.print("Chat name: ");
        String chatName = reader.nextLine();

        List<ChatMessage> messages = messageService.getMessagesForChat(chatName);
        for (ChatMessage message : messages) {
            System.out.println(String.format("[%tc] %s: %s",
                    message.getCreationTimestamp(), message.getAuthor(), message.getText()));
        }
    }
}
