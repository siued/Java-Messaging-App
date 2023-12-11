package group.message_server.controller.database;

import com.mongodb.client.model.Filters;
import model.FriendRecord;
import org.bson.Document;
import org.bson.types.ObjectId;

public class FriendsController {
    private static final String FRIENDS_COLLECTION_NAME = "friends";
    private static final DatabaseController databaseController = DatabaseController.getInstance();


    public boolean areFriends(ObjectId userId, ObjectId user2Id) {
        FriendRecord record = getFriendRecord(userId, user2Id);
        return record != null && record.isAccepted();
    }

    public void addFriend(ObjectId userId, ObjectId friendId) throws IllegalArgumentException {
        FriendRecord record = getFriendRecord(userId, friendId);
        if (record == null) {
            createFriendRecord(userId, friendId);
            return;
        }
        if (record.isAccepted()) {
            throw new IllegalArgumentException("Friend request already accepted");
        }
        if (record.isRejected()) {
            throw new IllegalArgumentException("Friend request rejected");
        }
        if (record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Friend request already sent");
        }
        if (record.getUserId().equals(friendId)) {
            acceptFriendRequest(record);
        }
    }

    private void acceptFriendRequest(FriendRecord record) {
        record.accept();
        Document filter = new Document("userId", record.getUserId())
                .append("friendId", record.getFriendId());
        Document update = record.toDocument();
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    private void createFriendRecord(ObjectId userId, ObjectId friendId) {
        Document friendRecord = new FriendRecord(userId, friendId).toDocument();
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .insertOne(friendRecord);
    }


    private FriendRecord getFriendRecord(ObjectId userId, ObjectId friendId) {
        Document document = databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .find(
                        Filters.or(
                                Filters.and(
                                        Filters.eq("user_id", userId),
                                        Filters.eq("friend_id", friendId)
                                ),
                                Filters.and(
                                        Filters.eq("user_id", friendId),
                                        Filters.eq("friend_id", userId)
                                )
                        )
                ).first();
        if (document == null) {
            return null;
        }
        return new FriendRecord(document);
    }
}
