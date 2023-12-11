package group.message_server.controller.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MessageController class is responsible for managing Message objects in the MongoDB database.
 * It provides methods for adding, retrieving, updating, and deleting messages.
 */
public class MessageController {
    // name of message collection in database
    private static final String MESSAGES_COLLECTION_NAME = "messages";
    private static final DatabaseController databaseController = DatabaseController.getInstance();

    /**
     * Send a message from senderId to recipientId with the given body.
     *
     * @param senderId id of the sender
     * @param recipientId id of the recipient
     * @param body body of the message
     */
    public void sendMessage(ObjectId senderId, ObjectId recipientId, String body) throws IllegalArgumentException {
        if (senderId.equals(recipientId)) {
            throw new IllegalArgumentException("Cannot send message to self");
        }

        Date date = new Date();

        Document message = new Message(
                null,
                body,
                senderId,
                recipientId,
                date,
                null
        ).toDocument();

        databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .insertOne(message);
    }

    /**
     * Returns a list of all unread messages for the given recipient.
     *
     * @param recipientId id of the recipient
     * @return a list of all unread messages for the given recipient
     */
    public List<Message> getUnreadReceivedMessages(ObjectId recipientId) {
        return getReceivedMessages(recipientId, false);
    }

    /**
     * Returns a list of all messages for the given recipient.
     *
     * @param recipientId id of the recipient
     * @return a list of all messages for the given recipient
     */
    public List<Message> getReceivedMessages(ObjectId recipientId) {
        return getReceivedMessages(recipientId, true);
    }

    /**
     * Sets the deliveredAt field of all messages in the given list to the current date.
     *
     * @param messages list of messages to set deliveredAt field for
     */
    public void setDelivered(List<Message> messages) {
        for (Message message : messages) {
            if (!message.isDelivered()) {
                setDelivered(message.id());
            }
        }
    }

    /**
     * Sets the deliveredAt field of the message with the given id to the current date.
     *
     * @param messageId id of the message to set deliveredAt field for
     */
    private void setDelivered(ObjectId messageId) {
        Bson filter = Filters.eq("_id", messageId);
        Bson update = Updates.set("deliveredAt", new Date());

        databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    /**
     * Returns a list of all messages for the given recipient.
     *
     * @param recipientId id of the recipient
     * @param delivered whether to return all or only undelivered messages
     * @return a list of all messages for the given recipient
     */
    private List<Message> getReceivedMessages(ObjectId recipientId, boolean delivered) {
        Bson filter;
        if (delivered) {
            filter = Filters.and(
                    Filters.eq("recipientId", recipientId),
                    Filters.eq("deliveredAt", null));
        } else {
            filter = Filters.eq("recipientId", recipientId);
        }

        return databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .find(filter)
                .map(Message::new).into(new ArrayList<>());
    }

    /**
     * Returns a list of all messages sent by the given sender.
     *
     * @param senderId id of the sender
     * @return a list of all messages sent by the given sender
     */
    public List<Message> getSentMessages(ObjectId senderId) {
        Bson filter = Filters.eq("senderId", senderId);

        return databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .find(filter)
                .map(Message::new).into(new ArrayList<>());
    }
}
