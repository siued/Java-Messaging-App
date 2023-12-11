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

public class MessageController {
    // name of message collection in database
    private static final String MESSAGES_COLLECTION_NAME = "messages";
    private static final DatabaseController databaseController = DatabaseController.getInstance();

    public void sendMessage(ObjectId senderId, ObjectId recipientId, String body) {
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

    public List<Message> getUnreadMessages(ObjectId recipientId) {
        return getMessages(recipientId, false);
    }

    public List<Message> getMessages(ObjectId recipientId) {
        return getMessages(recipientId, true);
    }

    public void setDelivered(List<Message> messages) {
        for (Message message : messages) {
            setDelivered(message.id());
        }
    }

    private void setDelivered(ObjectId messageId) {
        Bson filter = Filters.eq("_id", messageId);
        Bson update = Updates.set("delivered_at", new Date());

        databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    private List<Message> getMessages(ObjectId recipientId, boolean delivered) {
        Bson filter;
        if (delivered) {
            filter = Filters.and(
                    Filters.eq("recipient_id", recipientId),
                    Filters.eq("delivered_at", null));
        } else {
            filter = Filters.eq("recipient_id", recipientId);
        }

        return databaseController.getDatabase()
                .getCollection(MESSAGES_COLLECTION_NAME)
                .find(filter)
                .map(Message::new).into(new ArrayList<>());
    }
}
