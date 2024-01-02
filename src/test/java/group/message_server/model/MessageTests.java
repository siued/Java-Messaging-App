package group.message_server.model;

import model.Message;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTests {

    @Test
    public void messageConstructorSetsCorrectFields() {
        ObjectId id = new ObjectId();
        String sender = "sender";
        String recipient = "recipient";
        Date createdAt = new Date();
        Date deliveredAt = new Date();

        Message message = new Message(id, "Hello, World!", sender, recipient, createdAt, deliveredAt);

        assertEquals(id, message.id());
        assertEquals("Hello, World!", message.body());
        assertEquals(sender, message.sender());
        assertEquals(recipient, message.recipient());
        assertEquals(createdAt, message.createdAt());
        assertEquals(deliveredAt, message.deliveredAt());
    }

    @Test
    public void messageConstructorThrowsExceptionWhenBodyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), null, "sender", "recipient", new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenBodyIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "", "sender", "recipient", new Date(), new Date()));
    }

    @Test
    public void isDeliveredReturnsCorrectValues() {
        Message message = new Message(new ObjectId(), "Hello, World!", "sender", "recipient", new Date(), new Date());
        assertTrue(message.isDelivered());
        Message message2 = new Message(new ObjectId(), "Hello, World!", "sender", "recipient", new Date(), null);
        assertFalse(message2.isDelivered());
    }

    @Test
    public void cannotSendMessageToSelf() {
        assertThrows(IllegalArgumentException.class, () -> new Message(null, "Hello, World!", "sender", "sender", new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenSenderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "Hello, World!", null, "recipient", new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenSenderIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "Hello, World!", "", "recipient", new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenRecipientIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "Hello, World!", "sender", null, new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenRecipientIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "Hello, World!", "sender", "", new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenCreatedAtIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "Hello, World!", "sender", "recipient", null, new Date()));
    }

    @Test
    public void toDocumentReturnsCorrectDocument() {
        ObjectId id = new ObjectId();
        String sender = "sender";
        String recipient = "recipient";
        Date createdAt = new Date();
        Date deliveredAt = new Date();

        Message message = new Message(id, "Hello, World!", sender, recipient, createdAt, deliveredAt);
        Document document = message.toDocument();

        assertEquals(id, document.getObjectId("_id"));
        assertEquals("Hello, World!", document.getString("body"));
        assertEquals(sender, document.getString("sender"));
        assertEquals(recipient, document.getString("recipient"));
        assertEquals(createdAt, document.getDate("createdAt"));
        assertEquals(deliveredAt, document.getDate("deliveredAt"));
    }

    @Test
    public void toStringReturnsCorrectString() {
        ObjectId id = new ObjectId();
        String sender = "sender";
        String recipient = "recipient";
        Date createdAt = new Date();
        Date deliveredAt = new Date();

        Message message = new Message(id, "Hello, World!", sender, recipient, createdAt, deliveredAt);
        String expectedString = "Message{" +
                "id=" + id +
                ", body='Hello, World!'" +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", createdAt=" + createdAt +
                ", deliveredAt=" + deliveredAt +
                '}';

        assertEquals(expectedString, message.toString());
    }
}
