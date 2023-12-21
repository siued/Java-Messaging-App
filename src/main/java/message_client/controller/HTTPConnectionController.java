package message_client.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class HTTPConnectionController {
    private static final String BASE_URL = "http://localhost:8080";

    public boolean checkConnection() {
        // make a request to the server
        // if the server responds with a 200 status code, return true
        try {
            URL url = new URI(BASE_URL + "/ping").toURL();
            url.openConnection().connect();
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    // DOESNT WORK
    public JSONObject makeHttpRequest(String endpoint, String method, JSONObject requestBody) {
        try {
            URI uri = new URI(BASE_URL + endpoint);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .method(method, HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<?> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body().getClass());
            return new JSONObject(response.body().toString());

        } catch (Exception e) {
            // TODO proper logging
            e.printStackTrace();
        }
        return null;
    }
}
