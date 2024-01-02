package group.message_server.model;

import model.FriendRecord;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRecordTests {
    private final String user = "TEST_USER";
    private final String friend = "TEST_USER2";

    @Test
    public void friendRecordConstructorCreatesValidFriendRecord() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        assertEquals(user, friendRecord.getUser());
        assertEquals(friend, friendRecord.getFriend());
        assertTrue(friendRecord.isPending());
    }

    @Test
    public void acceptChangesStatusToAccepted() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.accept();
        assertTrue(friendRecord.isAccepted());
    }

    @Test
    public void rejectChangesStatusToDeclined() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.reject();
        assertTrue(friendRecord.isRejected());
    }

    @Test
    public void acceptThrowsExceptionWhenNotPending() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenNotPending() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }

    @Test
    public void otherReturnsCorrectUserId() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        assertEquals(friend, friendRecord.other(user));
        assertEquals(user, friendRecord.other(friend));
    }

    @Test
    public void otherThrowsExceptionWhenUserIdNotInRecord() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        assertThrows(IllegalArgumentException.class, () -> friendRecord.other("INVALID_USER"));
    }
    @Test
    public void friendRecordConstructorThrowsExceptionWhenUserIdIsNull() {
        assertThrows(NullPointerException.class, () -> new FriendRecord(null, friend));
    }

    @Test
    public void friendRecordConstructorThrowsExceptionWhenfriendIsNull() {
        assertThrows(NullPointerException.class, () -> new FriendRecord(user, null));
    }

    @Test
    public void toDocumentReturnsCorrectDocument() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        Document document = friendRecord.toDocument();

        assertEquals(user, document.getString("user"));
        assertEquals(friend, document.getString("friend"));
        assertEquals("PENDING", document.getString("status"));
    }

    @Test
    public void containsReturnsTrueWhenUserIdIsInRecord() {
        FriendRecord friendRecord = new FriendRecord(user, friend);

        assertTrue(friendRecord.contains(user));
        assertTrue(friendRecord.contains(friend));
    }

    @Test
    public void containsReturnsFalseWhenUserIdIsNotInRecord() {
        FriendRecord friendRecord = new FriendRecord(user, friend);

        assertFalse(friendRecord.contains("INVALID_USER"));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenUserIdIsNull() {
        Document document = new Document("friend", new ObjectId()).append("status", "PENDING");
        assertThrows(Exception.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenfriendIsNull() {
        Document document = new Document("user", new ObjectId()).append("status", "PENDING");
        assertThrows(Exception.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenStatusIsNull() {
        Document document = new Document("user", new ObjectId()).append("friend", new ObjectId());
        assertThrows(Exception.class, () -> new FriendRecord(document));
    }

    @Test
    public void friendRecordConstructorFromDocumentThrowsExceptionWhenStatusIsInvalid() {
        Document document = new Document("user", new ObjectId()).append("friend", new ObjectId()).append("status", "INVALID");
        assertThrows(Exception.class, () -> new FriendRecord(document));
    }

    @Test
    public void acceptThrowsExceptionWhenAlreadyAccepted() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenAlreadyRejected() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }

    @Test
    public void acceptThrowsExceptionWhenAlreadyRejected() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.reject();
        assertThrows(IllegalStateException.class, friendRecord::accept);
    }

    @Test
    public void rejectThrowsExceptionWhenAlreadyAccepted() {
        FriendRecord friendRecord = new FriendRecord(user, friend);
        friendRecord.accept();
        assertThrows(IllegalStateException.class, friendRecord::reject);
    }
}
