package message_client.controller;

import message_client.observer_pattern.Listenable;
import message_client.observer_pattern.Listener;
import model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageController implements Listenable {
    private final APIController apiController = new APIController();
    private final List<Listener> listeners = new ArrayList<>();

    public void sendMessage(String recipientId, String body) {
        apiController.sendMessage(UserController.getUsername(), recipientId, body);
    }

    public List<Message> getUnreadMessages() {
        return apiController.getUnreadMessages(UserController.getUsername());
    }

    public List<Message> getReceivedMessages() {
        return apiController.getReceivedMessages(UserController.getUsername());
    }

    public List<Message> getSentMessages() {
        return apiController.getSentMessages(UserController.getUsername());
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void update() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }
}
