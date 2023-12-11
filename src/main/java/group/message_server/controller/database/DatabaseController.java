package group.message_server.controller.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

/**
 * DatabaseController class is responsible for managing the MongoDB database.
 * It is a singleton class.
 * It provides methods for retrieving the database.
 */
public class DatabaseController {
    private static final String DATABASE_NAME = "messageDatabase";
    private static DatabaseController instance = null;
    private final MongoClient mongoClient;
    @Getter
    private final MongoDatabase database;

    /**
     * Constructs a new DatabaseController object.
     */
    private DatabaseController() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * Returns the singleton instance of DatabaseController.
     *
     * @return the singleton instance of DatabaseController.
     */
    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }
}
