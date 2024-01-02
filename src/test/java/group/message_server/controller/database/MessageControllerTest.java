package group.message_server.controller.database;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {
    @Test
    public void testSendMessage() {
        MessageController mc = new MessageController();
        UserController uc = new UserController();
        FriendsController fc = new FriendsController();

        String sender = "siued";
        String recipient = "s";

        ObjectId senderId = uc.getUserId(sender);
        ObjectId recipientId = uc.getUserId(recipient);

        assertTrue(fc.areFriends(senderId, recipientId));

        String message = "hello";
        mc.sendMessage(sender, recipient, message);

        assertEquals(1, mc.getReceivedMessages(recipient).size());
    }
}