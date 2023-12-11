package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public record Message(ObjectId id,
                      String body,
                      ObjectId sender_id,
                      ObjectId recipient_id,
                      Date created_at,
                      Date delivered_at) {

    public Message {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body cannot be null or blank");
        }
    }

    public boolean isDelivered() {
        return delivered_at != null;
    }

    public Message(Document document) {
        this(document.getObjectId("_id"),
                document.getString("body"),
                document.getObjectId("sender_id"),
                document.getObjectId("recipient_id"),
                document.getDate("created_at"),
                document.getDate("delivered_at"));
    }

    public Document toDocument() {
        Document document = new Document("body", body)
                .append("sender_id", sender_id)
                .append("recipient_id", recipient_id)
                .append("created_at", created_at)
                .append("delivered_at", delivered_at);
        if (id != null) {
            document.append("_id", id);
        }
        return document;
    }
}
