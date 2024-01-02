package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.Date;

public record Message(ObjectId id,
                      String body,
                      String sender,
                      String recipient,
                      Date createdAt,
                      Date deliveredAt) {

    public Message {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body cannot be null or blank");
        }

        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Cannot send message to self");
        }
    }

    public Message(JSONObject jsonObject) throws JSONException {
        this(null,
                jsonObject.getString("body"),
                jsonObject.getString("sender"),
                jsonObject.getString("recipient"),
                Date.from(ZonedDateTime.parse(jsonObject.getString("createdAt")).toInstant()),
                jsonObject.getBoolean("delivered") ?
                        Date.from(ZonedDateTime.parse(jsonObject.getString("deliveredAt")).toInstant())
                        : null);
    }

    public boolean isDelivered() {
        return deliveredAt != null;
    }

    public Message(Document document) {
        this(document.getObjectId("_id"),
                document.getString("body"),
                document.getString("sender"),
                document.getString("recipient"),
                document.getDate("createdAt"),
                document.getDate("deliveredAt"));
    }

    public Document toDocument() {
        Document document = new Document("body", body)
                .append("sender", sender)
                .append("recipient", recipient)
                .append("createdAt", createdAt)
                .append("deliveredAt", deliveredAt);
        if (id != null) {
            document.append("_id", id);
        }
        return document;
    }

    public String toString() {
        return "Message{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", createdAt=" + createdAt +
                ", deliveredAt=" + deliveredAt +
                '}';
    }
}
