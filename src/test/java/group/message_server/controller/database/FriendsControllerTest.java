package group.message_server.controller.database;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FriendsControllerTest {
    @Test
    // TODO fix this test, make the user first before adding as friend
    void testAddFriend() {
        FriendsController fc = new FriendsController();
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        fc.addFriend(userId, friendId);
        List<String> friends = fc.getPendingRequests(friendId);
        assertEquals(1, friends.size());
    }

}