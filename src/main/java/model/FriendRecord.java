package model;

import lombok.Getter;
import lombok.NonNull;
import org.bson.Document;

/**
 * Represents a friend request between two users.
 */
public class FriendRecord {
    @Getter
    private final String user;
    @Getter
    private final String friend;
    private Status status;

    private enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    /**
     * Constructs a new FriendRecord object.
     *
     * @param user    the user sending the friend request.
     * @param friend  the user receiving the friend request.
     * @param status  the status of the friend request.
     */
    private FriendRecord(@NonNull String user, @NonNull String friend, @NonNull Status status) {
        if (user.equals(friend)) {
            throw new IllegalArgumentException("Cannot send friend request to self");
        }

        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    /**
     * Constructs a new pending FriendRecord object.
     *
     * @param user   the user sending the friend request.
     * @param friend the user receiving the friend request.
     */
    public FriendRecord(String user, String friend) {
        this(user, friend, Status.PENDING);
    }

    /**
     * Constructs a new FriendRecord object from a MongoDB document.
     *
     * @param document the MongoDB document to construct a FriendRecord object from.
     */
    public FriendRecord(Document document) {
        this(document.getString("user"),
                document.getString("friend"),
                Status.valueOf(document.getString("status")));
    }

    /**
     * Convert this record to a MongoDB document.
     *
     * @return a MongoDB document representing this record
     */
    public Document toDocument() {
        return new Document("user", user)
                .append("friend", friend)
                .append("status", status.toString());
    }

    /**
     * Returns true if the provided user id is in this friend record.
     *
     * @param username the username to check.
     * @return true if the provided user id is in this friend record.
     */
    public boolean contains(String username) {
        return this.user.equals(username) || this.friend.equals(username);
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
     * Returns true if the friend request is pending.
     *
     * @return true if the friend request is pending.
     */
    public boolean isPending() {
        return status == Status.PENDING;
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
     * @param username the username of one of the users in this friend record.
     * @return the id of the other user in this friend record.
     * @throws IllegalArgumentException if the provided user id is not in this friend record.
     */
    public String other(String username) {
        if (this.user.equals(username)) {
            return this.friend;
        } else if (this.friend.equals(username)) {
            return this.user;
        } else {
            throw new IllegalArgumentException("Username " + username + " is not in this friend record");
        }
    }
}
