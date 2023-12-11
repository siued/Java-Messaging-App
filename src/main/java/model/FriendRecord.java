package model;

import lombok.Getter;
import org.bson.Document;
import org.bson.types.ObjectId;

public class FriendRecord {
    @Getter
    private ObjectId userId;
    @Getter
    private ObjectId friendId;
    private Status status;

    private enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    private FriendRecord(ObjectId userId, ObjectId friendId, Status status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
    }

    public FriendRecord(ObjectId userId, ObjectId friendId) {
        this(userId, friendId, Status.PENDING);
    }

    public FriendRecord(Document document) {
        this(document.getObjectId("userId"),
                document.getObjectId("friendId"),
                Status.valueOf(document.getString("status")));
    }

    public Document toDocument() {
        return new Document("userId", userId)
                .append("friendId", friendId)
                .append("status", status.toString());
    }

    public boolean contains(ObjectId userId) {
        return this.userId.equals(userId) || friendId.equals(userId);
    }

    public boolean isAccepted() {
        return status == Status.ACCEPTED;
    }

    public boolean isRejected() {
        return status == Status.DECLINED;
    }

    public void accept() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }
        status = Status.ACCEPTED;
    }

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
