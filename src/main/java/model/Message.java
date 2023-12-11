package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public record Message(ObjectId id,
                      String body,
                      ObjectId senderId,
                      ObjectId recipientId,
                      Date createdAt,
                      Date deliveredAt) {

    public Message {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body cannot be null or blank");
        }
    }

    public boolean isDelivered() {
        return deliveredAt != null;
    }

    public Message(Document document) {
        this(document.getObjectId("_id"),
                document.getString("body"),
                document.getObjectId("senderId"),
                document.getObjectId("recipientId"),
                document.getDate("createdAt"),
                document.getDate("deliveredAt"));
    }

    public Document toDocument() {
        Document document = new Document("body", body)
                .append("senderId", senderId)
                .append("recipientId", recipientId)
                .append("createdAt", createdAt)
                .append("deliveredAt", deliveredAt);
        if (id != null) {
            document.append("_id", id);
        }
        return document;
    }
}
