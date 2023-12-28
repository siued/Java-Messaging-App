package message_client.controller;

import model.Message;

import java.util.List;

public class MessageController {
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
}
