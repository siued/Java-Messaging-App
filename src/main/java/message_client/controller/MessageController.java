package message_client.controller;

import message_client.observer_pattern.Listenable;
import model.Message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Message controller class
 */
public class MessageController extends Listenable {
    private final APIController apiController = new APIController();

    /**
     * Send a message to a recipient.
     * @param recipient Recipient of the message.
     * @param body Body of the message.
     */
    public void sendMessage(String recipient, String body) {
        apiController.sendMessage(UserController.getUsername(), recipient, body);
    }

    /**
     * Get a list of unread messages.
     * @return List of unread messages.
     */
    public List<Message> getUnreadMessages() {
        return apiController.getUnreadMessages(UserController.getUsername());
    }

    /**
     * Get a list of received messages.
     * @return List of received messages.
     */
    public List<Message> getReceivedMessages() {
        return apiController.getReceivedMessages(UserController.getUsername());
    }

    /**
     * Get a list of sent messages.
     * @return List of sent messages.
     */
    public List<Message> getSentMessages() {
        return apiController.getSentMessages(UserController.getUsername());
    }

    /**
     * Get a list of messages to a user.
     * @param friendName Name of the user.
     * @return List of messages to the user.
     */
    public List<Message> getMessagesTo(String friendName) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(getReceivedMessages());
        messages.addAll(getSentMessages());

        messages.removeIf(message -> !message.sender().equals(friendName) && !message.recipient().equals(friendName));

        messages.sort(Comparator.comparing(Message::createdAt));
        return messages;
    }

    /**
     * Check if there are new messages.
     * @return True if there are new messages, false otherwise.
     */
    public boolean hasNewMessages() {
        return apiController.hasNewMessages(UserController.getUsername());
    }
}
