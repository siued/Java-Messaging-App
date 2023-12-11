package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Represents a user of the application.
 */
public record User(ObjectId id,
                   String username,
                   String password_hash,
                   Date created_at,
                   Date last_login_at) {

    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password_hash == null || password_hash.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }

    /**
     * Constructs a User object with the provided username, password, creation date and last login date.
     * The id is set to null.
     *
     * @param username the username of the user.
     * @param password_hash the hashed password of the user.
     * @param created_at the creation date of the user account.
     * @param last_login_at the last login date of the user.
     */
    public User(String username, String password_hash, Date created_at, Date last_login_at) {
        this(null, username, password_hash, created_at, last_login_at);
    }

    /**
     * Constructs a User object from a MongoDB Document.
     *
     * @param document the MongoDB Document containing user data. The Document must contain the following fields:
     *                 "_id" - the ObjectId of the user,
     *                 "username" - the username of the user,
     *                 "password_hash" - the hashed password of the user,
     *                 "created_at" - the creation date of the user account,
     *                 "last_login_at" - the last login date of the user.
     */
    public User(Document document) {
        this(document.getObjectId("_id"),
                document.getString("username"),
                document.getString("password_hash"),
                document.getDate("created_at"),
                document.getDate("last_login_at"));
    }

    /**
     * Constructs a User object with the provided username and password.
     * The creation date and last login date are set to the current date and time.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this(username, password, new Date(), new Date());
    }

    /**
     * Converts the User object into a MongoDB Document.
     *
     * The resulting Document contains the following fields:
     * "_id" - the ObjectId of the user (only if the id is not null),
     * "username" - the username of the user,
     * "password_hash" - the hashed password of the user,
     * "created_at" - the creation date of the user account,
     * "last_login_at" - the last login date of the user.
     *
     * @return a Document representing the User object.
     */
    public Document toDocument() {
        Document document = new Document("username", username)
                .append("password_hash", password_hash)
                .append("created_at", created_at)
                .append("last_login_at", last_login_at);
        if (id != null) document.append("_id", id);
        return document;
    }
}
