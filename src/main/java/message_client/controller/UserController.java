package message_client.controller;

import org.json.JSONException;
import org.json.JSONObject;

public class UserController {
    private final APIController apiController = new APIController();
    public void loginUser(String username, String password) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        JSONObject response = apiController.post("/user/login", requestBody);

        // TODO handle response without printing
        System.out.println(response);
    }

    public void registerUser(String username, String password) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

//        JSONObject response = apiController.post("/user/new", requestBody);
        // TODO handle response without printing

//        System.out.println(response);
        apiController.registerUser("/user/new", username, password);
    }

    public void addFriend(String friendName) {
        // TODO
    }

    public void getFriendRequests() {
        // TODO
    }

    public void acceptFriend(String friendName) {
        // TODO
    }
}
