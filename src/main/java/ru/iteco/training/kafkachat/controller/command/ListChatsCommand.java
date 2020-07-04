package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.controller.ConsoleCommandReader;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.model.ChatRooms;
import ru.iteco.training.kafkachat.service.ChatRoomService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ListChatsCommand implements ICommand {
    private static final String command = "list-chats";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.USER));

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
        return "Show available chats";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@listChatsCommand.command())" +
            " and @userService.hasRoles(@listChatsCommand.requiredRoles())")
    public void execute(ChatCommandEvent event) {
        ChatRooms chatRooms = chatRoomService.getAvailableChatRooms();
        System.out.println("private:");
        for (String name : chatRooms.getPrivateChats()) {
            System.out.println("\t" + name);
        }
        System.out.println("public:");
        for (String name : chatRooms.getPublicChats()) {
            System.out.println("\t" + name);
        }
    }
}
