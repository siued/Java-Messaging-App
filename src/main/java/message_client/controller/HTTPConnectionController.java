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

/**
 * Controller for HTTP connections.

 */
public class HTTPConnectionController {
    // Server URL
    private static final String BASE_URL = "http://34.148.224.88:8081";
    private final Logger LOGGER = Logger.getLogger(HTTPConnectionController.class.getName());

    /**
     * Constructor for the HTTPConnectionController.
     */
    public HTTPConnectionController() {
        ConsoleHandler handler = new ConsoleHandler();
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Check if the server is available.
     * @return True if the server is available, false otherwise.
     */
    public boolean checkConnection() {
        try {
            URL url = new URI(BASE_URL + "/ping").toURL();
            url.openConnection().connect();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Send a GET request to the server.
     * @param endpoint Endpoint to send the request to.
     * @return Response body of the request.
     */
    public String get(String endpoint) {
        try {
            // Cannot use try-with-resources here because of compatibility with Java 14
            HttpClient httpClient = HttpClient.newHttpClient();
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

    /**
     * Send a PUT request to the server.
     * @param endpoint Endpoint to send the request to.
     * @param requestBody Request body of the request.
     * @return Response body of the request.
     */
    public String put(String endpoint, Object requestBody) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
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

    /**
     * Create a POST request with a string body
     * @param endpoint Endpoint to send the request to
     * @param requestBody Request body of the request
     * @return The request object
     */
    private HttpRequest makeStringRequest(String endpoint, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
    }

    /**
     * Create a POST request with an Object body
     * @param endpoint Endpoint to send the request to
     * @param requestBody Request body of the request
     * @return The request object
     * @throws JsonProcessingException if the request body is not a valid JSON object
     */
    private HttpRequest makeJSONRequest(String endpoint, Object requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(requestBody);

        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    /**
     * Send a POST request to the server.
     * @param endpoint Endpoint to send the request to.
     * @param requestBody Request body of the request.
     * @return Response body
     */
    public String post(String endpoint, Object requestBody) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
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