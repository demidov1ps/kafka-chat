package ru.iteco.training.kafkachat.listener;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.enums.SettingKey;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.service.SettingsService;
import ru.iteco.training.kafkachat.service.UserService;

import javax.annotation.PreDestroy;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketListener {
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    @Order(3)
    @EventListener(ContextRefreshedEvent.class)
    public void start() throws IOException {
        serverSocket = new ServerSocket(0);

        ApplicationSettings host = new ApplicationSettings();
        host.setUserId(userService.getCurrentUser().getId());
        host.setKey(SettingKey.APP_SOCKET_HOST);
        host.setValue(InetAddress.getLocalHost().getHostAddress());
        settingsService.updateSetting(host);

        ApplicationSettings port = new ApplicationSettings();
        port.setUserId(userService.getCurrentUser().getId());
        port.setKey(SettingKey.APP_SOCKET_PORT);
        port.setValue(String.valueOf(serverSocket.getLocalPort()));
        settingsService.updateSetting(port);

        executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> acceptClient(socket));
            }
        });

    }

    @PreDestroy
    public void stop() {
        ApplicationSettings host = new ApplicationSettings();
        host.setUserId(userService.getCurrentUser().getId());
        host.setKey(SettingKey.APP_SOCKET_HOST);
        settingsService.deleteSetting(host);

        ApplicationSettings port = new ApplicationSettings();
        port.setUserId(userService.getCurrentUser().getId());
        port.setKey(SettingKey.APP_SOCKET_PORT);
        settingsService.deleteSetting(port);

        try {
            serverSocket.close();
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptClient(Socket client) {
        try(DataInputStream in = new DataInputStream(client.getInputStream())) {
            String data = in.readUTF();
            try (JsonParser parser = objectMapper.createParser(data)) {
                ChatMessage message = parser.readValueAs(ChatMessage.class);
                System.out.println(
                        String.format("Received message in chat %s from %s: %s",
                                message.getChat(), message.getAuthor(), message.getText()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
