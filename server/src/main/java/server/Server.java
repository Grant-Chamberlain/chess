package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", ctx -> { clearService.clear(); ctx.result("{}"); });
        javalin.post("/user", ctx -> {
            RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            ctx.result(new Gson().toJson(result));
        });
        javalin.post("/session", ctx -> {
            LoginRequest request = new Gson().fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            ctx.result(new Gson().toJson(result));
        });
        javalin.delete("/session", ctx -> {
            String token = ctx.header("authorization");
            userService.logout(token);
            ctx.result("{}");
        });




        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });
        javalin.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });
        javalin.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error:" + e.getMessage())));
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
