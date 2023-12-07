package group.message_server.controller.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import model.User;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserController {
    private static final String USERS_COLLECTION_NAME = "users";

    public void addUser(User user) {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);

        //check unique username
        if (collection.find(Filters.eq("username", user.username())).first() != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        Document doc = new Document("username", user.username())
                .append("password_hash", user.password_hash())
                .append("created_at", user.created_at())
                .append("last_login_at", user.last_login_at());
        collection.insertOne(doc);
    }

    public boolean loginUser(String username, String password) {
        // TODO implement proper security
        try {
            User user = getUser(username);
            if (password.equals(user.password_hash())) {
                //update last login
                updateField(username, "last_login_at", new Date());
                return true;
            };
        } catch (IllegalArgumentException ignored) {}
        return false;
    }

    public User getUser(String username) {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        Document user = collection.find(Filters.eq("username", username)).first();
        if (user == null) throw new IllegalArgumentException("Username doesn't exist");
        return new User(user);
    }

    public boolean updateField(String username, String fieldName, Object value) {

        //check if field name is valid
        Field[] userFields = User.class.getDeclaredFields();
        if (Arrays.stream(userFields).noneMatch(field -> field.getName().equals(fieldName))) {
            throw new IllegalArgumentException("Invalid field name!");
        }

        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);

        UpdateResult result = collection.updateOne(Filters.eq("username", username),
                                                    Updates.set(fieldName, value));

        return result.getModifiedCount() > 0;
    }

    public List<User> getUsers() {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(new User(doc));
        }
        return users;
    }

    public void deleteUser(String username) {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteOne(Filters.eq("username", username));
    }

    public void deleteAllUsers() {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteMany(new Document());
    }
}
