package ru.iteco.training.kafkachat.controller.command;

import ru.iteco.training.kafkachat.enums.UserRole;

import java.util.Set;

public interface ICommand {
    String command();
    String description();
    Set<UserRole> requiredRoles();
}
