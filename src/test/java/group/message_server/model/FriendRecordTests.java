package group.message_server.model;

import model.FriendRecord;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRecordTests {

    @Test
    public void friendRecordConstructorCreatesValidFriendRecord() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        assertEquals(userId, friendRecord.getUserId());
        assertEquals(friendId, friendRecord.getFriendId());
        assertTrue(friendRecord.isPending());
    }

    @Test
    public void acceptChangesStatusToAccepted() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.accept();
        assertTrue(friendRecord.isAccepted());
    }

    @Test
    public void rejectChangesStatusToDeclined() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.reject();
        assertTrue(friendRecord.isRejected());
    }

    @Test
    public void acceptThrowsExceptionWhenNotPending() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenNotPending() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }

    @Test
    public void otherReturnsCorrectUserId() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        assertEquals(friendId, friendRecord.other(userId));
        assertEquals(userId, friendRecord.other(friendId));
    }

    @Test
    public void otherThrowsExceptionWhenUserIdNotInRecord() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        assertThrows(IllegalArgumentException.class, () -> friendRecord.other(new ObjectId()));
    }
    @Test
    public void friendRecordConstructorThrowsExceptionWhenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> new FriendRecord(null, new ObjectId()));
    }

    @Test
    public void friendRecordConstructorThrowsExceptionWhenFriendIdIsNull() {
        assertThrows(NullPointerException.class, () -> new FriendRecord(new ObjectId(), null));
    }

    @Test
    public void toDocumentReturnsCorrectDocument() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        Document document = friendRecord.toDocument();

        assertEquals(userId, document.getObjectId("userId"));
        assertEquals(friendId, document.getObjectId("friendId"));
        assertEquals("PENDING", document.getString("status"));
    }

    @Test
    public void containsReturnsTrueWhenUserIdIsInRecord() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);

        assertTrue(friendRecord.contains(userId));
        assertTrue(friendRecord.contains(friendId));
    }

    @Test
    public void containsReturnsFalseWhenUserIdIsNotInRecord() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);

        assertFalse(friendRecord.contains(new ObjectId()));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenUserIdIsNull() {
        Document document = new Document("friendId", new ObjectId()).append("status", "PENDING");
        assertThrows(NullPointerException.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenFriendIdIsNull() {
        Document document = new Document("userId", new ObjectId()).append("status", "PENDING");
        assertThrows(NullPointerException.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenStatusIsNull() {
        Document document = new Document("userId", new ObjectId()).append("friendId", new ObjectId());
        assertThrows(NullPointerException.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenStatusIsInvalid() {
        Document document = new Document("userId", new ObjectId()).append("friendId", new ObjectId()).append("status", "INVALID");
        assertThrows(IllegalArgumentException.class, () -> new FriendRecord(document));
    }

    @Test
    public void acceptThrowsExceptionWhenAlreadyAccepted() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenAlreadyRejected() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }

    @Test
    public void acceptThrowsExceptionWhenAlreadyRejected() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenAlreadyAccepted() {
        ObjectId userId = new ObjectId();
        ObjectId friendId = new ObjectId();
        FriendRecord friendRecord = new FriendRecord(userId, friendId);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }
}
