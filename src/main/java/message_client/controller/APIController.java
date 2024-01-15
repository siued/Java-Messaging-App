package message_client.controller;

import model.Message;
import model.UserCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API controller class to communicate with the server.
 */
public class APIController {
    private final HTTPConnectionController httpConnectionController = new HTTPConnectionController();

    /**
     * Check if the server is available.
     * @return True if the server is available, false otherwise.
     */
    public boolean checkConnection() {
        return httpConnectionController.checkConnection();
    }

    /**
     * Register a new user.
     * @param username Username of the new user.
     * @param password Password of the new user.
     */
    public void registerUser(String username, String password) {
        httpConnectionController.post("/user/new", new UserCredentials(username, password));
    }

    /**
     * Login a user.
     * @param username Username of the user.
     * @param password Password of the user.
     * @return server response, TODO return api token
     */
    public String loginUser(String username, String password) {
        return httpConnectionController.post("/user/login", new UserCredentials(username, password));
    }

    /**
     * Add a friend
     * @param username Username of the user.
     * @param friendName Username of the friend.
     */
    public void addFriend(String username, String friendName) {
        httpConnectionController.put("/user/addfriend" + "?username=" + username + "&friendName=" + friendName, null);
    }

    /**
     * Get a list of pending friend requests.
     * @param username Username of the user.
     * @return List of pending friend requests.
     */
    public List<String> getPendingFriendRequests(String username) {
        try {
            String response = httpConnectionController.get("/user/pending-requests" + "?username=" + username);
            List<String> friendRequestsStrings = getStringsFromJSONStringList(response);
            if (friendRequestsStrings != null) return friendRequestsStrings;
        } catch (Exception ignored) {
            // assuming response is well-formed
        }
        return null;
    }

    /**
     * Accept a friend request.
     * @param username Username of the user.
     * @param friendName Username of the friend.
     */
    public void acceptFriend(String username, String friendName) {
        addFriend(username, friendName);
    }

    /**
     * Get a list of friends.
     * @param username Username of the user.
     * @return List of friend names
     */
    public List<String> getFriends(String username) {
        try {
            String response = httpConnectionController.get("/user/friends" + "?username=" + username);
            List<String> friendsStrings = getStringsFromJSONStringList(response);
            if (friendsStrings != null) return friendsStrings;
        } catch (Exception ignored) {
            // assuming response is well-formed
        }
        return null;
    }

    /**
     * Parse a JSON string list into a list of strings.
     * @param response JSON list of strings as a string
     * @return List of strings
     * @throws JSONException if response is not a valid JSON string
     */
    private List<String> getStringsFromJSONStringList(String response) throws JSONException {
        if (response != null) {
            JSONArray friends = new JSONArray(response);
            List<String> friendsStrings = new ArrayList<>();
            for (int i = 0; i < friends.length(); i++) {
                friendsStrings.add(friends.getString(i));
            }
            return friendsStrings;
        }
        return null;
    }

    /**
     * Send a message.
     * @param username Username of the user.
     * @param recipient Username of the recipient.
     * @param body Body of the message.
     */
    public void sendMessage(String username, String recipient, String body) {
        httpConnectionController.post("/message/send" + "?sender=" + username + "&recipient=" + recipient, body);
    }

    /**
     * Get a list of messages.
     * @param username Username of the user.
     * @param type Type of messages to get. Can be "unread", "sent", or "received".
     * @return List of messages.
     */
    public List<Message> getMessages(String username, String type) {
        // check if type is unread, sent, received
        List<String> allowedTypes = List.of("unread", "sent", "received");
        if (!allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid message type in APIController.getMessages(): " + type + "\nAllowed types: " + Arrays.toString(allowedTypes.toArray()));
        }
        try {
            String response = httpConnectionController.get("/message/" + type + "?username=" + username);
            List<Message> messages = new ArrayList<>();
            for (String jsonMessage : getStringsFromJSONStringList(response)) {
                //parse json into message object
                messages.add(new Message(new JSONObject(jsonMessage)));
            }
            return messages;
        } catch (Exception ignored) {
            // assuming response is well-formed
        }
        return null;
    }

    /**
     * Get a list of unread messages.
     * @param username Username of the user.
     * @return List of unread messages.
     */
    public List<Message> getUnreadMessages(String username) {
        return getMessages(username, "unread");
    }

    /**
     * Get a list of received messages.
     * @param username Username of the user.
     * @return List of received messages.
     */
    public List<Message> getReceivedMessages(String username) {
        return getMessages(username, "received");
    }

    /**
     * Get a list of sent messages.
     * @param username Username of the user.
     * @return List of sent messages.
     */
    public List<Message> getSentMessages(String username) {
        return getMessages(username, "sent");
    }

    /**
     * Check if the user has new messages.
     * @param username Username of the user.
     * @return True if the user has new messages, false otherwise.
     */
    public boolean hasNewMessages(String username) {
        try {
            return Boolean.parseBoolean(httpConnectionController.get("/user/has-unread" + "?username=" + username));
        } catch (Exception ignored) {
            // assuming response is well-formed
        }
        return false;
    }
}
