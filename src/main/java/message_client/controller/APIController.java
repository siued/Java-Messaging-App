package message_client.controller;

import model.UserCredentials;
import org.json.JSONArray;

import java.util.ArrayList;
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
            if (response != null) {
                JSONArray friendRequests = new JSONArray(response);
                List<String> friendRequestsStrings = new ArrayList<>();
                for (int i = 0; i < friendRequests.length(); i++) {
                    friendRequestsStrings.add(friendRequests.getString(i));
                }
                return friendRequestsStrings;
            }
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
            // TODO fix duplicated code
            if (response != null) {
                JSONArray friends = new JSONArray(response);
                List<String> friendsStrings = new ArrayList<>();
                for (int i = 0; i < friends.length(); i++) {
                    friendsStrings.add(friends.getString(i));
                }
                return friendsStrings;
            }
        } catch (Exception ignored) {
            // assuming response is well-formed
        }
        return null;
    }

    public void sendMessage(String username, String recipientId, String body) {
        httpConnectionController.post("/message/send" + "?sender=" + username + "&recipient=" + recipientId, body);
    }
}
