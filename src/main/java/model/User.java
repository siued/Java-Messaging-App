package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public record User(ObjectId id,
                   String username,
                   String password_hash,
                   Date created_at,
                   Date last_login_at) {

    public User(String username, String password_hash, Date created_at, Date last_login_at) {
        this(null, username, password_hash, created_at, last_login_at);
    }

    public User(Document user) {
        this(user.getObjectId("_id"),
                user.getString("username"),
                user.getString("password_hash"),
                user.getDate("created_at"),
                user.getDate("last_login_at"));
    }

    public User(String username, String password) {
        this(username, password, new Date(), new Date());
    }
}
