package ru.iteco.training.kafkachat.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.iteco.training.kafkachat.entity.ApplicationSettings;
import ru.iteco.training.kafkachat.entity.ChatRoom;
import ru.iteco.training.kafkachat.enums.SettingKey;
import ru.iteco.training.kafkachat.model.ChatMessage;
import ru.iteco.training.kafkachat.service.ChatUserService;
import ru.iteco.training.kafkachat.service.SettingsService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class SocketMessageSender implements MessageSender {
    @Autowired
    private ChatUserService chatUserService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SettingsService settingsService;
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        executorService = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void send(ChatMessage chatMessage, ChatRoom chat) {
        List<ApplicationSettings> settings = settingsService.getApplicationSettingsByKeys(
                Arrays.asList(SettingKey.APP_SOCKET_HOST, SettingKey.APP_SOCKET_PORT));;
        if (chat.getPrivateChat()) {
            Set<UUID> userIds = chatUserService.getChatUsersIds(chat.getId());
            settings = settings.stream().filter(s -> {
                return userIds.contains(s.getUserId());
            }).collect(Collectors.toList());
        }
        try {
            String data = objectMapper.writeValueAsString(chatMessage);
            Map<UUID, SendTask> userIdToTask = new HashMap<>();
            for (ApplicationSettings setting : settings) {
                SendTask task = userIdToTask.getOrDefault(setting.getUserId(), new SendTask());
                task.setData(data);
                switch (setting.getKey()) {
                    case APP_SOCKET_HOST:
                        task.setHost(setting.getValue());
                        break;
                    case APP_SOCKET_PORT:
                        task.setPort(Integer.valueOf(setting.getValue()));
                        break;
                    default:
                        break;
                }
                userIdToTask.put(setting.getUserId(), task);
            }

            executorService.invokeAll(userIdToTask.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SendTask implements Callable<Void> {
        private String host;
        private int port;
        private String data;

        @Override
        public Void call() throws Exception {
            try(Socket socket = new Socket(host, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());)
            {
                out.writeUTF(data);
                out.flush();
            }
            return null;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
