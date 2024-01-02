package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Represents a message sent from one user to another.
 */
public record Message(ObjectId id,
                      String body,
                      String sender,
                      String recipient,
                      Date createdAt,
                      Date deliveredAt) {

    /**
     * Constructs a Message object with the provided id, body, sender, recipient, creation date, and delivery date.
     *
     * @param id the id of the message
     * @param body the body of the message. Must not be null or empty.
     * @param sender the username of the sender. Must not be null or empty.
     * @param recipient the username of the recipient. Must not be null or empty.
     * @param createdAt the creation date of the message. Must not be null or empty.
     * @param deliveredAt the delivery date of the message. Is null if the message has not been delivered.
     */
    public Message {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body cannot be null or blank");
        }
        if (sender == null || sender.isBlank()) {
            throw new IllegalArgumentException("Sender cannot be null or blank");
        }
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Cannot send message to self");
        }
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be null or blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
    }

    /**
     * Converts a JSON object to a Message object
     *
     * @param jsonObject the JSON object to convert
     * @throws JSONException if the JSON object is malformed
     */
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

    /**
     * Check whether the message has been delivered
     *
     * @return true if the message has been delivered
     */
    public boolean isDelivered() {
        return deliveredAt != null;
    }

    /**
     * Constructs a message from the given document
     *
     * @param document the document to construct the message from. Must contain the following fields:
     *                 "_id" - the ObjectId of the message,
     *                 "body" - the body of the message,
     *                 "sender" - the username of the sender,
     *                 "recipient" - the username of the recipient,
     *                 "createdAt" - the creation date of the message,
     *                 "deliveredAt" - the delivery date of the message.
     */
    public Message(Document document) {
        this(document.getObjectId("_id"),
                document.getString("body"),
                document.getString("sender"),
                document.getString("recipient"),
                document.getDate("createdAt"),
                document.getDate("deliveredAt"));
    }

    /**
     * Converts the message to a MongoDB Document
     *
     * @return the message as a MongoDB Document
     */
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

    /**
     * Converts the message to a String
     *
     * @return the message as a String
     */
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
