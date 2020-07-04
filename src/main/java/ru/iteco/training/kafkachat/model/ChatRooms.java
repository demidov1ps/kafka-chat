package ru.iteco.training.kafkachat.model;

import java.util.List;

public class ChatRooms {
    private List<String> privateChats;
    private List<String> publicChats;

    public List<String> getPrivateChats() {
        return privateChats;
    }

    public void setPrivateChats(List<String> privateChats) {
        this.privateChats = privateChats;
    }

    public List<String> getPublicChats() {
        return publicChats;
    }

    public void setPublicChats(List<String> publicChats) {
        this.publicChats = publicChats;
    }
}
