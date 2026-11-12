package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import service.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private static final int HTTP_OK = 200;
    private static final int HTTP_SERVER_ERROR = 500;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        return send("POST", "/user", request, null, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        return send("POST", "/session", request, null, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        send("DELETE", "/session", null, authToken, null);
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken)
            throws ResponseException {
        return send("POST", "/game", request, authToken, CreateGameResult.class);
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        return send("GET", "/game", null, authToken, ListGamesResult.class);
    }

    public void joinGame(JoinGameRequest request, String authToken) throws ResponseException {
        send("PUT", "/game", request, authToken, null);
    }

    public void clear() throws ResponseException {
        send("DELETE", "/db", null, null, null);
    }


    private <T> T send(String method, String path, Object body, String authToken,
                       Class<T> responseClass) throws ResponseException {

        HttpRequest request = buildRequest(method, path, body, authToken);
        HttpResponse<String> response = execute(request);
        return handleResponse(response, responseClass);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, bodyPublisher(body));
        if (body != null) {
            builder.header("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.header("authorization", authToken);
        }
        return builder.build();
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }

    private HttpResponse<String> execute(HttpRequest request) throws ResponseException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ResponseException(HTTP_SERVER_ERROR, "Could not reach the server");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseException(HTTP_SERVER_ERROR, "Request was interrupted");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass)
            throws ResponseException {
        if (response.statusCode() != HTTP_OK) {
            throw new ResponseException(response.statusCode(), errorMessage(response));
        }
        if (responseClass == null) {
            return null;
        }
        return gson.fromJson(response.body(), responseClass);
    }

    private String errorMessage(HttpResponse<String> response) {
        try {
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            return json.get("message").getAsString();
        } catch (RuntimeException e) {
            return "Request failed with status " + response.statusCode();
        }
    }
}
