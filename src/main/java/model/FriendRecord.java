package model;

import lombok.Getter;
import org.bson.Document;
import org.bson.types.ObjectId;

public class FriendRecord {
    @Getter
    private final ObjectId userId;
    @Getter
    private final ObjectId friendId;
    private Status status;

    private enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    /**
     * Constructs a new FriendRecord object.
     *
     * @param userId the id of the user sending the friend request.
     * @param friendId the id of the user receiving the friend request.
     * @param status the status of the friend request.
     */
    private FriendRecord(ObjectId userId, ObjectId friendId, Status status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
    }

    /**
     * Constructs a new pending FriendRecord object.
     *
     * @param userId the id of the user sending the friend request.
     * @param friendId the id of the user receiving the friend request.
     */
    public FriendRecord(ObjectId userId, ObjectId friendId) {
        this(userId, friendId, Status.PENDING);
    }

    /**
     * Constructs a new FriendRecord object from a MongoDB document.
     *
     * @param document the MongoDB document to construct a FriendRecord object from.
     */
    public FriendRecord(Document document) {
        this(document.getObjectId("userId"),
                document.getObjectId("friendId"),
                Status.valueOf(document.getString("status")));
    }

    /**
     * convert this record to a MongoDB document
     *
     * @return a MongoDB document representing this record
     */
    public Document toDocument() {
        return new Document("userId", userId)
                .append("friendId", friendId)
                .append("status", status.toString());
    }

    /**
     * Returns true if the provided user id is in this friend record.
     *
     * @param userId the user id to check.
     * @return true if the provided user id is in this friend record.
     */
    public boolean contains(ObjectId userId) {
        return this.userId.equals(userId) || friendId.equals(userId);
    }

    /**
     * Returns true if the friend request is pending.
     *
     * @return true if the friend request is pending.
     */
    public boolean isAccepted() {
        return status == Status.ACCEPTED;
    }

    /**
     * Returns true if the friend request has been rejected.
     *
     * @return true if the friend request has been rejected.
     */
    public boolean isRejected() {
        return status == Status.DECLINED;
    }

    /**
     * Accepts a friend request.
     *
     * @throws IllegalStateException if the friend request is not pending.
     */
    public void accept() throws IllegalStateException {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }
        status = Status.ACCEPTED;
    }

    /**
     * Rejects a friend request.
     *
     * @throws IllegalStateException if the friend request is not pending.
     */
    public void reject() throws IllegalStateException {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }
        status = Status.DECLINED;
    }

    /**
     * Returns the other user in this friend record.
     *
     * @param userId the id of one of the users in this friend record.
     * @return the id of the other user in this friend record.
     * @throws IllegalArgumentException if the provided user id is not in this friend record.
     */
    public ObjectId other(ObjectId userId) {
        if (this.userId == userId) {
            return friendId;
        } else if (friendId == userId) {
            return this.userId;
        } else {
            throw new IllegalArgumentException("User Id " + userId + " is not in this friend record");
        }
    }
}
