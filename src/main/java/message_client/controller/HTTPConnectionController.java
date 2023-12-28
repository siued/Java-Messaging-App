package message_client.controller;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPConnectionController {
    private static final String BASE_URL = "http://localhost:8080";
    private final Logger LOGGER = Logger.getLogger(HTTPConnectionController.class.getName());

    public HTTPConnectionController() {
        ConsoleHandler handler = new ConsoleHandler();
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
    }

    public boolean checkConnection() {
        try {
            URL url = new URI(BASE_URL + "/ping").toURL();
            url.openConnection().connect();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public String get(String endpoint) {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 201 || statusCode == 200) {
                return responseBody;
            } else {
                LOGGER.warning("GET request to " + endpoint + " failed\n" + "Status code: " + statusCode + "\n" + "Response body: " + responseBody);
            }
        } catch (Exception e) {
            LOGGER.warning("GET request to " + endpoint + " failed\n" + "Exception: " + e.getMessage());
        }
        return null;
    }

    public String put(String endpoint, Object requestBody) {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            if (requestBody == null) {
                requestBody = "";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 201 || statusCode == 200) {
                return responseBody;
            } else {
                LOGGER.warning("PUT request to " + endpoint + " failed\n" + "Status code: " + statusCode + "\n" + "Response body: " + responseBody);
            }
        } catch (Exception e) {
            LOGGER.warning("PUT request to " + endpoint + " failed\n" + "Exception: " + e.getMessage());
        }
        return null;
    }

    private HttpRequest makeStringRequest(String endpoint, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
    }

    private HttpRequest makeJSONRequest(String endpoint, Object requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(requestBody);

        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    public String post(String endpoint, Object requestBody) {
        System.out.println(endpoint);
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request;

            if (requestBody instanceof String) {
                request = makeStringRequest(endpoint, (String) requestBody);
            } else {
                request = makeJSONRequest(endpoint, requestBody);
            }

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 201 || statusCode == 200) {
                return responseBody;
            } else {
                LOGGER.warning("POST request to " + endpoint + " failed\n" + "Status code: " + statusCode + "\n" + "Response body: " + responseBody);
                return null;
            }
        } catch (Exception e) {
            LOGGER.warning("POST request to " + endpoint + " failed\n" + "Exception: " + e.getMessage());
        }
        return null;
    }
}