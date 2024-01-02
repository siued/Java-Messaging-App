package group.message_server.controller.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

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
    // Name of user collection in MongoDB database
    private static final String USERS_COLLECTION_NAME = "users";
    private static final DatabaseController dbController = DatabaseController.getInstance();

    /**
     * Adds a new User to the MongoDB database.
     *
     * @param user the User object to be added to the database.
     * @throws IllegalArgumentException if a user with the same username already exists.
     */
    public void addUser(User user) throws IllegalArgumentException {
        MongoDatabase database = dbController.getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);

        //check unique username
        if (collection.find(Filters.eq("username", user.username())).first() != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        Document doc = user.toDocument();
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
            if (password.equals(user.passwordHash())) {
                //update last login
                updateField(username, "lastLoginAt", new Date());
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
        try {
            return getUser("username", username);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User with that user name doesn't exist");
        }
    }

    /**
     * Retrieves a User from the MongoDB database by user id.
     *
     * @param userId the user id of the user.
     * @return the User object with the provided user id.
     * @throws IllegalArgumentException if no user with the provided user id exists.
     */
    public User getUser(ObjectId userId) throws IllegalArgumentException {
        try {
            return getUser("_id", userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User with that user id doesn't exist");
        }
    }

    /**
     * Retrieves a User from the MongoDB database by a field name and value.
     *
     * @param fieldName the name of the field to search by.
     * @param value the value of the field to search by.
     * @return the User object with the provided field name and value.
     * @throws IllegalArgumentException if no user with the provided field name and value exists.
     */
    private User getUser(String fieldName, Object value) throws IllegalArgumentException {
        MongoDatabase database = dbController.getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        Document user = collection.find(Filters.eq(fieldName, value)).first();
        if (user == null) throw new IllegalArgumentException("User doesn't exist");
        return new User(user);
    }

    public boolean userExists(String username) {
        try {
            return getUser(username) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves a User's id from the MongoDB database by username.
     *
     * @param username the username of the user.
     * @return the user id of the user with the provided username.
     * @throws IllegalArgumentException if no user with the provided username exists.
     */
    public ObjectId getUserId(String username) throws IllegalArgumentException {
        return getUser(username).id();
    }

    /**
     * Retrieves a User's username from the MongoDB database by user id.
     *
     * @param userId the user id of the user.
     * @return the username of the user with the provided user id.
     * @throws IllegalArgumentException if no user with the provided user id exists.
     */
    public String getUsername(ObjectId userId) throws IllegalArgumentException {
        return getUser(userId).username();
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

        MongoDatabase database = dbController.getDatabase();
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
        MongoDatabase database = dbController.getDatabase();
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
        MongoDatabase database = dbController.getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteOne(Filters.eq("username", username));
    }

    /**
     * Deletes all Users from the MongoDB database.
     */
    public void deleteAllUsers() {
        MongoDatabase database = dbController.getDatabase();
        MongoCollection<Document> collection = database.getCollection(USERS_COLLECTION_NAME);
        collection.deleteMany(new Document());
    }
}
