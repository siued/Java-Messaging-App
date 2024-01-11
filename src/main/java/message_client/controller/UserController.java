package message_client.controller;

import message_client.observer_pattern.Listenable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * User controller class
 */
public class UserController extends Listenable {
    private static String username = null;
    private final APIController apiController = new APIController();

    /**
     * Login a user.
     * @param username Username of the user.
     * @param password Password of the user.
     * @return True if the login was successful, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        String token = apiController.loginUser(username, password);
        // TODO use token
        if (token != null) {
            UserController.username = username;
            this.update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the username of the logged-in user.
     * @return Username of the logged-in user.
     */
    public static String getUsername() {
        if (username == null) {
            throw new IllegalStateException("User is not logged in");
        }
        return username;
    }

    /**
     * Check if a user is logged in.
     * @return True if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return username != null;
    }

    /**
     * Register a new user.
     * @param username Username of the new user.
     * @param password Password of the new user.
     * @throws JSONException
     */
    public void registerUser(String username, String password) {
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException ignored) {
        }

        // TODO handle response
        apiController.registerUser(username, password);
    }

    /**
     * Add a friend.
     * @param friendName Username of the friend.
     */
    public void addFriend(String friendName) {
        apiController.addFriend(username, friendName);
    }

    /**
     * Get a list of pending friend requests.
     * @return List of pending friend requests.
     */
    public List<String> getFriendRequests() {
        return apiController.getPendingFriendRequests(username);
    }

    /**
     * Accept a friend request.
     * @param friendName Username of the friend.
     */
    public void acceptFriend(String friendName) {
        apiController.acceptFriend(username, friendName);
    }

    /**
     * Get a list of friends.
     * @return List of friends.
     */
    public List<String> getFriends() {
        return apiController.getFriends(username);
    }
}
