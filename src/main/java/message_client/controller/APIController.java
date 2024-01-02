package message_client.controller;

import model.Message;
import model.UserCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIController {
    private final HTTPConnectionController httpConnectionController = new HTTPConnectionController();

    public boolean checkConnection() {
        return httpConnectionController.checkConnection();
    }

    // WORKS
    public void registerUser(String username, String password) {
        httpConnectionController.post("/user/new", new UserCredentials(username, password));
    }

    // TODO return api token
    public String loginUser(String username, String password) {
        return httpConnectionController.post("/user/login", new UserCredentials(username, password));
    }

    public void addFriend(String username, String friendName) {
        httpConnectionController.put("/user/addfriend" + "?username=" + username + "&friendName=" + friendName, null);
    }

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

    public void acceptFriend(String username, String friendName) {
        addFriend(username, friendName);
    }

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

    public void sendMessage(String username, String recipientId, String body) {
        httpConnectionController.post("/message/send" + "?sender=" + username + "&recipient=" + recipientId, body);
    }

    public List<Message> getMessages(String username, String type) {
        // check if type is unread, sent, received
        List<String> allowedTypes = List.of("unread", "sent", "received");
        if (!allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Invalid type");
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

    public List<Message> getUnreadMessages(String username) {
        return getMessages(username, "unread");
    }

    public List<Message> getReceivedMessages(String username) {
        return getMessages(username, "received");
    }

    public List<Message> getSentMessages(String username) {
        return getMessages(username, "sent");
    }
}
