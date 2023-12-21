package message_client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.UserCredentials;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class APIController {
    private final HTTPConnectionController httpConnectionController = new HTTPConnectionController();

    public boolean checkConnection() {
        return httpConnectionController.checkConnection();
    }

    public JSONObject get(String endpoint, JSONObject requestBody) {
        return httpConnectionController.makeHttpRequest(endpoint, "GET", requestBody);
    }

    public JSONObject post(String endpoint, JSONObject requestBody) {
        return httpConnectionController.makeHttpRequest(endpoint, "POST", requestBody);
    }

    // WORKS
    public void registerUser(String endpoint, String username, String password) {
        String BASE_URL = "http://localhost:8080";
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            UserCredentials userCredentials = new UserCredentials(username, password);

            // convert to json string
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(userCredentials);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 201) {
                System.out.println("User created successfully: " + responseBody);
            } else {
                System.out.println("Failed to create user. HTTP status code: " + statusCode);
                System.out.println("Response body: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
