package group.message_server.controller.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import model.FriendRecord;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FriendsController class is responsible for managing FriendRecord objects in the MongoDB database.
 * It provides methods for adding, retrieving, updating, and deleting friends.
 */
public class FriendsController {
    // name of friends collection in database
    private static final String FRIENDS_COLLECTION_NAME = "friends";
    private static final DatabaseController databaseController = DatabaseController.getInstance();


    /**
     * Returns true if the two users are friends, false otherwise.
     *
     * @param userId first user to check
     * @param user2Id second user to check
     * @return true if the two users are friends, false otherwise.
     */
    public boolean areFriends(ObjectId userId, ObjectId user2Id) {
        FriendRecord record = getFriendRecord(userId, user2Id);
        return record != null && record.isAccepted();
    }

    /**
     * Adds a friend request from userId to friendId.
     *
     * @param userId the user sending the friend request.
     * @param friendId the user receiving the friend request.
     * @throws IllegalArgumentException if the friend request has already been sent, accepted, or rejected.
     */
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

    /**
     * Accepts a friend request from userId to friendId.
     *
     * @param record the friend request to accept.
     */
    private void acceptFriendRequest(FriendRecord record) {
        record.accept();
        Document filter = new Document("userId", record.getUserId())
                .append("friendId", record.getFriendId());
        Document update = new Document("$set", record.toDocument());
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    /**
     * Rejects a friend request from userId to friendId.
     *
     * @param userId the user sending the friend request.
     * @param friendId the user receiving the friend request.
     */
    public void rejectFriendRequest(ObjectId userId, ObjectId friendId) {
        FriendRecord record = getFriendRecord(userId, friendId);
        if (record == null) {
            throw new IllegalArgumentException("Friend request does not exist");
        }
        if (record.isAccepted()) {
            throw new IllegalArgumentException("Friend request already accepted");
        }
        if (record.isRejected()) {
            throw new IllegalArgumentException("Friend request already rejected");
        }
        if (record.getUserId().equals(friendId)) {
            record.reject();
        }
        // TODO extract update method
        Document filter = new Document("userId", record.getUserId())
                .append("friendId", record.getFriendId());
        Document update = new Document("$set", record.toDocument());
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    /**
     * Creates a friend request from userId to friendId.
     *
     * @param userId the user sending the friend request.
     * @param friendId the user receiving the friend request.
     */
    private void createFriendRecord(ObjectId userId, ObjectId friendId) {
        Document friendRecord = new FriendRecord(userId, friendId).toDocument();
        InsertOneResult result = databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .insertOne(friendRecord);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Could not add friend");
        }
    }

    /**
     * Returns the FriendRecord object for the two users if one exists, null otherwise
     *
     * @param userId the id of one of the users
     * @param user2Id the id of the other user
     * @return the FriendRecord object for the two users if one exists, null otherwise
     */
    private FriendRecord getFriendRecord(ObjectId userId, ObjectId user2Id) {
        List<FriendRecord> records = getFriendRecords(userId);
        for (FriendRecord record : records) {
            if (record.contains(user2Id)) {
                return record;
            }
        }
        return null;
    }

    /**
     * Returns a list of all Users that are friends with the user with the provided userId.
     *
     * @param userId the id of the user whose friends are to be retrieved.
     * @return a list of User objects that are friends with the user with the provided userId.
     */
    public List<User> getFriends(ObjectId userId) {
        UserController uc = new UserController();
        List<FriendRecord> records = getFriendRecords(userId);

        return records.stream()
            .filter(FriendRecord::isAccepted)
            .map(record -> uc.getUser(record.other(userId)))
            .collect(Collectors.toList());
    }

    /**
     * Returns all friend requests containing the user with the given userId
     *
     * @param userId the id of the user whose friend requests are to be retrieved.
     * @return a list of FriendRecord objects containing the user with the given userId
     */
    private List<FriendRecord> getFriendRecords(ObjectId userId) {
        return databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .find(Filters.or(
                        Filters.eq("userId", userId),
                        Filters.eq("friendId", userId)
                )).map(FriendRecord::new).into(new ArrayList<>());
    }

    public List<String> getPendingRequests(ObjectId userId) {
        UserController uc = new UserController();
        List<FriendRecord> records = getFriendRecords(userId);
        List<String> pendingRequests = new ArrayList<>();
        for (FriendRecord record : records) {
            if (record.isPending()) {
                pendingRequests.add(uc.getUsername(record.other(userId)));
            }
        }
        return pendingRequests;
    }
}
