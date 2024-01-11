package message_client.controller;

import message_client.observer_pattern.Listenable;
import message_client.observer_pattern.Listener;
import model.Message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageController extends Listenable {
    private final APIController apiController = new APIController();

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

    public List<Message> getMessagesTo(String friendName) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(getReceivedMessages());
        messages.addAll(getSentMessages());

        messages.removeIf(message -> !message.sender().equals(friendName) && !message.recipient().equals(friendName));

        messages.sort(Comparator.comparing(Message::createdAt));
        return messages;
    }

    public boolean hasNewMessages() {
        return apiController.hasNewMessages(UserController.getUsername());
    }
}
