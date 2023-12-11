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
        assertEquals("test", user.password_hash());
        assertNotNull(user.created_at());
        assertNotNull(user.last_login_at());
        assertNull(user.id());
    }

    @Test
    public void userConstructorWithDocumentSetsCorrectFields() {
        ObjectId id = new ObjectId();
        Document document = new Document("_id", id)
                .append("username", "test")
                .append("password_hash", "test")
                .append("created_at", new Date())
                .append("last_login_at", new Date());

        User user = new User(document);

        assertEquals(id, user.id());
        assertEquals("test", user.username());
        assertEquals("test", user.password_hash());
        assertNotNull(user.created_at());
        assertNotNull(user.last_login_at());
    }

    @Test
    public void userConstructorWithAllFieldsSetsCorrectFields() {
        ObjectId id = new ObjectId();
        Date createdAt = new Date();
        Date lastLoginAt = new Date();

        User user = new User(id, "test", "test", createdAt, lastLoginAt);

        assertEquals(id, user.id());
        assertEquals("test", user.username());
        assertEquals("test", user.password_hash());
        assertEquals(createdAt, user.created_at());
        assertEquals(lastLoginAt, user.last_login_at());
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
        assertEquals("test", document.getString("password_hash"));
        assertEquals(createdAt, document.getDate("created_at"));
        assertEquals(lastLoginAt, document.getDate("last_login_at"));
    }
}
