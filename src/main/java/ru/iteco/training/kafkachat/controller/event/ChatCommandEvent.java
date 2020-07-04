package ru.iteco.training.kafkachat.controller.event;

public class ChatCommandEvent {
    private final String command;

    public ChatCommandEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
