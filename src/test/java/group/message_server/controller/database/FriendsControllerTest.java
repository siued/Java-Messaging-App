package group.message_server.controller.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.User;

import static org.junit.jupiter.api.Assertions.*;

class FriendsControllerTest {
    private static final String username = "TEST_USER";
    private static final String friendName = "TEST_USER2";
    private static final User user1 = new User(username, "password");
    private static final User user2 = new User(friendName, "password");
    private static final UserController uc = new UserController();
    private static final FriendsController fc = new FriendsController();


    @BeforeAll
    public static void setUp() {
        uc.addUser(user1);
        uc.addUser(user2);
    }

    @Test
    void testAddFriend() {
        try {
            fc.addFriend(username, friendName);
            fc.addFriend(friendName, username);
            assertTrue(fc.areFriends(username, friendName));
            fc.removeFriend(username, friendName);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void rejectFriendRequestChangesStatus() {
        fc.addFriend(username, friendName);
        fc.rejectFriendRequest(username, friendName);
        assertFalse(fc.areFriends(username, friendName));
        fc.deleteFriendRecord(username, friendName);
    }

    @Test
    public void newFriendRequestIsPending() {
        fc.addFriend(username, friendName);
        assertFalse(fc.areFriends(username, friendName));
        List<String> requests = fc.getPendingRequests(friendName);
        assertEquals(1, requests.size());
        assertEquals(username, requests.getFirst());
    }

    @Test
    void rejectFriendRequestThrowsExceptionWhenRequestDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> fc.rejectFriendRequest(username, friendName));
    }

    @Test
    void getFriendsReturnsEmptyListWhenNoFriends() {
        List<String> friends = fc.getFriends(username);
        assertTrue(friends.isEmpty());
    }

    @AfterAll
    public static void tearDown() {
        try {
            fc.removeFriend(username, friendName);
        } catch (Exception ignored) {}
        fc.deleteFriendRecord(username, friendName);
        uc.deleteUser(username);
        uc.deleteUser(friendName);
    }
}