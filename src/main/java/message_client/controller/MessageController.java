package message_client.controller;

import java.util.logging.Logger;

public class MessageController {
    private final APIController apiController = new APIController();

    public void sendMessage(String recipientId, String body) {
        apiController.sendMessage(UserController.getUsername(), recipientId, body);
    }

    public void getUnreadMessages() {
        // TODO
    }

    public void getReceivedMessages() {
        // TODO
    }

    public void getSentMessages() {
        // TODO
    }
}
