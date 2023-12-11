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

/**
 * UserController class is responsible for managing User objects in the MongoDB database.
 * It provides methods for adding, retrieving, updating, and deleting users.
 */
public class UserController {
    // Name of collection in MongoDB database
    private static final String USERS_COLLECTION_NAME = "users";

    /**
     * Adds a new User to the MongoDB database.
     *
     * @param user the User object to be added to the database.
     * @throws IllegalArgumentException if a user with the same username already exists.
     */
    public void addUser(User user) throws IllegalArgumentException {
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

    /**
     * Attempts to log in a user with the provided username and password.
     * If the username and password match an existing user, the user's last login date is updated and the method returns true.
     * If no matching user is found, the method returns false.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @return true if the login was successful, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        // TODO implement proper security
        try {
            User user = getUser(username);
            if (password.equals(user.password_hash())) {
                //update last login
                updateField(username, "last_login_at", new Date());
                return true;
            }
        } catch (IllegalArgumentException ignored) {
            // incorrect username, login failed
        }
        return false;
    }

    /**
     * Retrieves a User from the MongoDB database by username.
     *
     * @param username the username of the user.
     * @return the User object with the provided username.
     * @throws IllegalArgumentException if no user with the provided username exists.
     */
    public User getUser(String username) throws IllegalArgumentException {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        Document user = collection.find(Filters.eq("username", username)).first();
        if (user == null) throw new IllegalArgumentException("Username doesn't exist");
        return new User(user);
    }

    /**
     * Updates a field of a User in the MongoDB database.
     *
     * @param username the username of the user.
     * @param fieldName the name of the field to be updated.
     * @param value the new value for the field.
     * @return true if the update was successful, false otherwise.
     * @throws IllegalArgumentException if the provided field name does not exist in the User class.
     */
    public boolean updateField(String username, String fieldName, Object value) throws IllegalArgumentException {

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

    /**
     * Retrieves all Users from the MongoDB database.
     *
     * @return a List of User objects.
     */
    public List<User> getUsers() {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(new User(doc));
        }
        return users;
    }

    /**
     * Deletes a User from the MongoDB database by username.
     *
     * @param username the username of the user to be deleted.
     */
    public void deleteUser(String username) {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteOne(Filters.eq("username", username));
    }

    /**
     * Deletes all Users from the MongoDB database.
     */
    public void deleteAllUsers() {
        MongoDatabase database = DatabaseController.getInstance().getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteMany(new Document());
    }
}
