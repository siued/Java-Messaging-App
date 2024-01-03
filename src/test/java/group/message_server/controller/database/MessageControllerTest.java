package group.message_server.controller.database;

import model.Message;
import model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageControllerTest {

    private static final String username = "TEST_USER";
    private static final String friendName = "TEST_USER2";
    private static final User user1 = new User(username, "password");
    private static final User user2 = new User(friendName, "password");
    private static final UserController uc = new UserController();
    private static final FriendsController fc = new FriendsController();
    private static final MessageController mc = new MessageController();

    @BeforeAll
    public static void setUp() {
        uc.addUser(user1);
        uc.addUser(user2);
        fc.addFriend(username, friendName);
        fc.addFriend(friendName, username);
        assertTrue(fc.areFriends(username, friendName));
        assertTrue(uc.userExists(username));
        assertTrue(uc.userExists(friendName));
        for (Message message : mc.getReceivedMessages(friendName)) {
            mc.deleteMessage(message.id());
        }
    }

    @Test
    public void testSendMessage() {
        try {
            mc.sendMessage(username, friendName, "test message");
            List<Message> messages = mc.getReceivedMessages(friendName);
            assertEquals(1, messages.size());
            Message message = messages.get(0);
            assertEquals(username, message.sender());
            assertEquals(friendName, message.recipient());
            assertEquals("test message", message.body());
            assertNotNull(message.id());
            assertNotNull(message.createdAt());
            mc.deleteMessage(message.id());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // enable when setting messages to delivered after they are retrieved is implemented
//    @Test
//    public void testGetUnreadReceivedMessages() {
//        try {
//            mc.sendMessage(username, friendName, "test message");
//            var messages = mc.getUnreadReceivedMessages(friendName);
//            assertEquals(1, messages.size());
//            Message message = messages.get(0);
//            assertEquals(username, message.sender());
//            assertEquals(friendName, message.recipient());
//            assertEquals("test message", message.body());
//            assertNotNull(message.id());
//            assertNotNull(message.createdAt());
//
//            messages = mc.getUnreadReceivedMessages(friendName);
//            assertEquals(0, messages.size());
//
//            mc.deleteMessage(message.id());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void testGetReceivedMessages() {
        try {
            mc.sendMessage(username, friendName, "test message");
            mc.sendMessage(username, friendName, "test message 2");
            List<Message> messages = mc.getReceivedMessages(friendName);
            assertEquals(2, messages.size());

            mc.deleteMessage(messages.get(0).id());
            mc.deleteMessage(messages.get(1).id());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSentMessages() {
        try {
            mc.sendMessage(username, friendName, "test message");
            mc.sendMessage(username, friendName, "test message 2");
            List<Message> messages = mc.getSentMessages(username);
            assertEquals(2, messages.size());

            List<Message> messages2 = mc.getSentMessages(friendName);
            assertEquals(0, messages2.size());

            mc.deleteMessage(messages.get(0).id());
            mc.deleteMessage(messages.get(1).id());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteMessage() {
        try {
            mc.sendMessage(username, friendName, "test message");
            List<Message> messages = mc.getReceivedMessages(friendName);
            assertEquals(1, messages.size());
            Message message = messages.get(0);
            assertEquals(username, message.sender());
            assertEquals(friendName, message.recipient());
            assertEquals("test message", message.body());
            assertNotNull(message.id());
            assertNotNull(message.createdAt());

            mc.deleteMessage(message.id());

            messages = mc.getReceivedMessages(friendName);
            assertEquals(0, messages.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSetDelivered() {
        try {
            mc.sendMessage(username, friendName, "test message");
            List<Message> messages = mc.getReceivedMessages(friendName);
            assertEquals(1, messages.size());
            Message message = messages.get(0);
            assertNull(message.deliveredAt());

            mc.setDelivered(messages);

            messages = mc.getReceivedMessages(friendName);
            assertEquals(1, messages.size());
            message = messages.get(0);
            assertNotNull(message.deliveredAt());

            mc.deleteMessage(message.id());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        for (Message message : mc.getReceivedMessages(friendName)) {
            mc.deleteMessage(message.id());
        }
        try {
            fc.removeFriend(username, friendName);
        } catch (Exception ignored) {}
        fc.deleteFriendRecord(username, friendName);
        uc.deleteUser(username);
        uc.deleteUser(friendName);
    }
}