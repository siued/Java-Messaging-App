package group.message_server.controller.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseController {
    private static final String DATABASE_NAME = "messageDatabase";
    private static DatabaseController instance = null;
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final UserController userController;


    private DatabaseController() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase(DATABASE_NAME);
        userController = new UserController();
    }

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public UserController getUserController() {
        return userController;
    }
}
