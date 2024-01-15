package group.message_server.controller.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import model.FriendRecord;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param user1 first user to check
     * @param user2 second user to check
     * @return true if the two users are friends, false otherwise.
     */
    public boolean areFriends(String user1, String user2) {
        FriendRecord record = getFriendRecord(user1, user2);
        return record != null && record.isAccepted();
    }

    /**
     * Adds a friend request from userId to friendId.
     *
     * @param user the user sending the friend request.
     * @param friend the user receiving the friend request.
     * @throws IllegalArgumentException if the friend request has already been sent, accepted, or rejected.
     */
    public void addFriend(String user, String friend) throws IllegalArgumentException {
        FriendRecord record = getFriendRecord(user, friend);
        if (record == null) {
            createFriendRecord(user, friend);
            return;
        }
        if (record.isAccepted()) {
            throw new IllegalArgumentException("Friend request already accepted");
        }
        if (record.isRejected()) {
            throw new IllegalArgumentException("Friend request rejected");
        }
        if (record.getUser().equals(user)) {
            throw new IllegalArgumentException("Friend request already sent");
        }
        if (record.other(user).equals(friend)) {
            acceptFriendRequest(record);
        }
    }

    public void removeFriend(String user, String friend) {
        if (!areFriends(user, friend)) {
            throw new IllegalArgumentException("Users are not friends");
        }
        deleteFriendRecord(user, friend);
    }

    /**
     * Accepts a friend request from userId to friendId.
     *
     * @param record the friend request to accept.
     */
    private void acceptFriendRequest(@NotNull FriendRecord record) {
        record.accept();
        Document filter = new Document("user", record.getUser())
                .append("friend", record.getFriend());
        Document update = new Document("$set", record.toDocument());
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    /**
     * Rejects a friend request from userId to friendId.
     *
     * @param user1 the user sending the friend request.
     * @param user2 the user receiving the friend request.
     */
    public void rejectFriendRequest(String user1, String user2) {
        FriendRecord record = getFriendRecord(user1, user2);
        if (record == null) {
            throw new IllegalArgumentException("Friend request does not exist");
        }
        if (record.isAccepted()) {
            throw new IllegalArgumentException("Friend request already accepted");
        }
        if (record.isRejected()) {
            throw new IllegalArgumentException("Friend request already rejected");
        }
        if (record.getUser().equals(user2)) {
            record.reject();
        }
        Document filter = new Document("user", record.getUser())
                .append("friend", record.getFriend());
        Document update = new Document("$set", record.toDocument());
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .updateOne(filter, update);
    }

    /**
     * Creates a friend request from userId to friendId.
     *
     * @param user the user sending the friend request.
     * @param friend the user receiving the friend request.
     */
    private void createFriendRecord(String user, String friend) {
        Document friendRecord = new FriendRecord(user, friend).toDocument();
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
     * @param user1 the username of one of the users
     * @param user2 the username of the other user
     * @return the FriendRecord object for the two users if one exists, null otherwise
     */
    private @Nullable FriendRecord getFriendRecord(String user1, String user2) {
        List<FriendRecord> records = getFriendRecords(user1);
        for (FriendRecord record : records) {
            if (record.contains(user2)) {
                return record;
            }
        }
        return null;
    }

    /**
     * Returns a list of all Users that are friends with the user with the provided userId.
     *
     * @param user the username of the user whose friends are to be retrieved.
     * @return a list of User objects that are friends with the user with the provided userId.
     */
    public List<String> getFriends(String user) {
        List<FriendRecord> records = getFriendRecords(user);

        return records.stream()
                .filter(FriendRecord::isAccepted)
                .map(record -> record.other(user))
                .collect(Collectors.toList());
    }

    /**
     * Returns all friend requests containing the user with the given userId
     *
     * @param user the username of the user whose friend requests are to be retrieved.
     * @return a list of FriendRecord objects containing the user with the given userId
     */
    private @NotNull List<FriendRecord> getFriendRecords(String user) {
        return databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .find(Filters.or(
                        Filters.eq("user", user),
                        Filters.eq("friend", user)
                )).map(FriendRecord::new).into(new ArrayList<>());
    }

    /**
     * Returns a list of all pending friend requests sent to the user with the given userId
     *
     * @param user the username of the user whose friend requests are to be retrieved.
     * @return a list of String objects representing all other users who have sent a friend request to the user with the given userId
     */
    public List<String> getPendingRequests(String user) {
        List<FriendRecord> records = getFriendRecords(user);
        List<String> pendingRequests = new ArrayList<>();
        for (FriendRecord record : records) {
            if (record.isPending()) {
                pendingRequests.add(record.other(user));
            }
        }
        return pendingRequests;
    }

    /**
     * Deletes the friend record for the two users if one exists.
     *
     * @param user1 the username of one of the users
     * @param user2 the username of the other user
     */
    public void deleteFriendRecord(String user1, String user2) {
        Bson filter = Filters.and(
                Filters.or(
                        Filters.and(
                                Filters.eq("user", user1),
                                Filters.eq("friend", user2)
                        ),
                        Filters.and(
                                Filters.eq("user", user2),
                                Filters.eq("friend", user1)
                        )
                )
        );

        // deleteMany to ensure that if there are multiple records they all get deleted
        databaseController.getDatabase()
                .getCollection(FRIENDS_COLLECTION_NAME)
                .deleteMany(filter);
    }
}
