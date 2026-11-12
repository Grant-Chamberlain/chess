package client;

import com.google.gson.Gson;
import service.RegisterRequest;
import service.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

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
        return send("Post", "/user", request, null, RegisterResult.class);
    }


    private <T> T send(String method, String path, Object body, String authtoken,
                         Class<T> responseClass) throws ResponseException {
        HttpRequest request = b
    }






    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, bodyPublisher(body));
        if (body != null) {
            builder.header("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.header("authorization" , authToken);
        }
        return builder.build();
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }
}
