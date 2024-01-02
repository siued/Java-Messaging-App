package group.message_server.model;

import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    public void userConstructorWithUsernameAndPasswordSetsCorrectFields() {
        User user = new User("test", "test");
        assertEquals("test", user.username());
        assertEquals("test", user.passwordHash());
        assertNotNull(user.createdAt());
        assertNotNull(user.lastLoginAt());
        assertNull(user.id());
    }

    @Test
    public void userConstructorWithDocumentSetsCorrectFields() {
        ObjectId id = new ObjectId();
        Date date1 = new Date();
        Date date2 = new Date();
        Document document = new Document("_id", id)
                .append("username", "test")
                .append("passwordHash", "test")
                .append("createdAt", date1)
                .append("lastLoginAt", date2);

        User user = new User(document);

        assertEquals(id, user.id());
        assertEquals("test", user.username());
        assertEquals("test", user.passwordHash());
        assertEquals(date1, user.createdAt());
        assertEquals(date2, user.lastLoginAt());
    }

    @Test
    public void userConstructorWithAllFieldsSetsCorrectFields() {
        ObjectId id = new ObjectId();
        Date createdAt = new Date();
        Date lastLoginAt = new Date();

        User user = new User(id, "test", "test", createdAt, lastLoginAt);

        assertEquals(id, user.id());
        assertEquals("test", user.username());
        assertEquals("test", user.passwordHash());
        assertEquals(createdAt, user.createdAt());
        assertEquals(lastLoginAt, user.lastLoginAt());
    }

    @Test
    public void toDocumentReturnsCorrectDocument() {
        ObjectId id = new ObjectId();
        Date createdAt = new Date();
        Date lastLoginAt = new Date();

        User user = new User(id, "test", "test", createdAt, lastLoginAt);
        Document document = user.toDocument();

        assertEquals(id, document.getObjectId("_id"));
        assertEquals("test", document.getString("username"));
        assertEquals("test", document.getString("passwordHash"));
        assertEquals(createdAt, document.getDate("createdAt"));
        assertEquals(lastLoginAt, document.getDate("lastLoginAt"));
    }

    @Test
    public void usernameAndPasswordGetValidated() {
        assertThrows(IllegalArgumentException.class, () -> new User("test", ""));
        assertThrows(IllegalArgumentException.class, () -> new User("test", null));
        assertThrows(IllegalArgumentException.class, () -> new User("", "test"));
        assertThrows(IllegalArgumentException.class, () -> new User(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> new User("test test test", "test"));
        assertThrows(IllegalArgumentException.class, () -> new User("test/", "test"));
        assertThrows(IllegalArgumentException.class, () -> new User("test%", "test"));
    }
}
