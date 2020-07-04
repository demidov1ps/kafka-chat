package ru.iteco.training.kafkachat.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.iteco.training.kafkachat.controller.event.ChatCommandEvent;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class HelpCommand implements ICommand {
    private static final String command = "help";
    private static final Set<UserRole> requiredRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.USER));

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserService userService;

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return "List of commands";
    }

    @Override
    public Set<UserRole> requiredRoles() {
        return requiredRoles;
    }

    @EventListener(condition = "#event.command.equals(@helpCommand.command())")
    public void execute(ChatCommandEvent event) {
        Set<UserRole> currentUserRoles = userService.getCurrentUserRoles();
        Map<String, ICommand> commands = context.getBeansOfType(ICommand.class);
        commands.values().stream().filter(c -> CollectionUtils.containsAny(c.requiredRoles(), currentUserRoles)).forEach(c -> {
            System.out.print(c.command());
            System.out.print("\t\t- ");
            System.out.println(c.description());
        });
    }
}
