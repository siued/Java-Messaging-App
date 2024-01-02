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
     * Send a message from sender to recipient with the given body.
     *
     * @param sender username of the sender
     * @param recipient username of the recipient
     * @param body body of the message
     */
    public void sendMessage(String sender, String recipient, String body) throws IllegalArgumentException {
        Document message = new Message(
                null,
                body,
                sender,
                recipient,
                new Date(),
                null
        ).toDocument();

        System.out.println(message);

        databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .insertOne(message);
    }

    /**
     * Returns a list of all unread messages for the given recipient.
     *
     * @param recipient username of the recipient
     * @return a list of all unread messages for the given recipient
     */
    public List<Message> getUnreadReceivedMessages(String recipient) {
        return getReceivedMessages(recipient, false);
    }

    /**
     * Returns a list of all messages for the given recipient.
     *
     * @param recipient username of the recipient
     * @return a list of all messages for the given recipient
     */
    public List<Message> getReceivedMessages(String recipient) {
        return getReceivedMessages(recipient, true);
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
     * @param recipient username of the recipient
     * @param delivered whether to return all or only undelivered messages
     * @return a list of all messages for the given recipient
     */
    private List<Message> getReceivedMessages(String recipient, boolean delivered) {
        Bson filter;
        if (!delivered) {
            filter = Filters.and(
                    Filters.eq("recipient", recipient),
                    Filters.eq("deliveredAt", null));
        } else {
            filter = Filters.eq("recipient", recipient);
        }
//        Bson update = Updates.set("deliveredAt", new Date());
//
//        databaseController.getDatabase().getCollection(MESSAGES_COLLECTION_NAME).updateMany(filter, update);

        return databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .find(filter)
                .map(Message::new).into(new ArrayList<>());
    }

    /**
     * Returns a list of all messages sent by the given sender.
     *
     * @param sender username of the sender
     * @return a list of all messages sent by the given sender
     */
    public List<Message> getSentMessages(String sender) {
        Bson filter = Filters.eq("sender", sender);

        return databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .find(filter)
                .map(Message::new).into(new ArrayList<>());
    }

    public void deleteMessage(ObjectId messageId) {
        Bson filter = Filters.eq("_id", messageId);

        databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .deleteOne(filter);
    }
}
