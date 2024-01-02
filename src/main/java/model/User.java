package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Represents a user of the application.
 */
public record User(ObjectId id,
                   String username,
                   String passwordHash,
                   Date createdAt,
                   Date lastLoginAt) {

    /**
     * Constructs a User object with the provided id, username, password, creation date and last login date.
     *
     * @param id the id of the user.
     * @param username the username of the user. Must not be null, empty, or contain forbidden characters
     * @param passwordHash the hashed password of the user. Must not be null or empty.
     * @param createdAt the creation date of the user account.
     * @param lastLoginAt the last login date of the user.
     */
    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (username.matches(".*[/% ].*")) {
            throw new IllegalArgumentException("Username cannot contain spaces, %, or /");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }

    /**
     * Constructs a User object with the provided username, password, creation date and last login date.
     * The id is set to null.
     *
     * @param username the username of the user.
     * @param passwordHash the hashed password of the user.
     * @param createdAt the creation date of the user account.
     * @param lastLoginAt the last login date of the user.
     */
    public User(String username, String passwordHash, Date createdAt, Date lastLoginAt) {
        this(null, username, passwordHash, createdAt, lastLoginAt);
    }

    /**
     * Constructs a User object from a MongoDB Document.
     *
     * @param document the MongoDB Document containing user data. The Document must contain the following fields:
     *                 "_id" - the ObjectId of the user,
     *                 "username" - the username of the user,
     *                 "passwordHash" - the hashed password of the user,
     *                 "createdAt" - the creation date of the user account,
     *                 "lastLoginAt" - the last login date of the user.
     */
    public User(Document document) {
        this(document.getObjectId("_id"),
                document.getString("username"),
                document.getString("passwordHash"),
                document.getDate("createdAt"),
                document.getDate("lastLoginAt"));
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
     * The resulting Document contains the following fields:
     *      "_id" - the ObjectId of the user (only if the id is not null),
     *      "username" - the username of the user,
     *      "passwordHash" - the hashed password of the user,
     *      "createdAt" - the creation date of the user account,
     *      "lastLoginAt" - the last login date of the user.
     *
     * @return a Document representing the User object.
     */
    public Document toDocument() {
        Document document = new Document("username", username)
                .append("passwordHash", passwordHash)
                .append("createdAt", createdAt)
                .append("lastLoginAt", lastLoginAt);
        if (id != null) document.append("_id", id);
        return document;
    }
}
