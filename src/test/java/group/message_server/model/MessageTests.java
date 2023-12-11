package group.message_server.model;

import model.Message;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTests {

    @Test
    public void messageConstructorSetsCorrectFields() {
        ObjectId id = new ObjectId();
        ObjectId senderId = new ObjectId();
        ObjectId recipientId = new ObjectId();
        Date createdAt = new Date();
        Date deliveredAt = new Date();

        Message message = new Message(id, "Hello, World!", senderId, recipientId, createdAt, deliveredAt);

        assertEquals(id, message.id());
        assertEquals("Hello, World!", message.body());
        assertEquals(senderId, message.senderId());
        assertEquals(recipientId, message.recipientId());
        assertEquals(createdAt, message.createdAt());
        assertEquals(deliveredAt, message.deliveredAt());
    }

    @Test
    public void messageConstructorThrowsExceptionWhenBodyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), null, new ObjectId(), new ObjectId(), new Date(), new Date()));
    }

    @Test
    public void messageConstructorThrowsExceptionWhenBodyIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Message(new ObjectId(), "", new ObjectId(), new ObjectId(), new Date(), new Date()));
    }

    @Test
    public void isDeliveredReturnsTrueWhenDeliveredAtIsNotNull() {
        Message message = new Message(new ObjectId(), "Hello, World!", new ObjectId(), new ObjectId(), new Date(), new Date());
        assertTrue(message.isDelivered());
    }

    @Test
    public void isDeliveredReturnsFalseWhenDeliveredAtIsNull() {
        Message message = new Message(new ObjectId(), "Hello, World!", new ObjectId(), new ObjectId(), new Date(), null);
        assertFalse(message.isDelivered());
    }

    @Test
    public void cannotSendMessageToSelf() {
        ObjectId id = new ObjectId();
        assertThrows(IllegalArgumentException.class, () -> new Message(null, "Hello, World!", id, id, new Date(), new Date()));
    }
}
