package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.ChatRoomService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CreatePublicChatCommand implements ICommand {
    private static final String command = "create-public-chat";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN));

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ConsoleCommandReader reader;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return "Create new public chat";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@createPublicChatCommand.command())" +
            " and @userService.hasRoles(@createPublicChatCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        System.out.print("Chat name: ");
        String name = reader.nextLine();

        chatRoomService.createChatRoom(name, false);
    }
}
