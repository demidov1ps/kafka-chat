package ru.iteco.training.kafkachat.controller;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleCommandReader {
    private final Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }
}
