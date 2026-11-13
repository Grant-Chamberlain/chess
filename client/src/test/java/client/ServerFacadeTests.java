package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.*;

import javax.naming.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }
    public String registerUser(String username) throws ResponseException{
        RegisterRequest request = new RegisterRequest(username, "grant", "grant");
        RegisterResult result = facade.register(request);
        return result.authToken();
    }

    @BeforeEach
    public void setUp() throws ResponseException{
        facade.clear();
    }

    @Test
    public void registerSuccess() throws ResponseException {
        RegisterRequest request = new RegisterRequest("grant", "grant", "grant");
        RegisterResult result = facade.register(request);
        assert(result != null);
        assert(result.username().equals("grant"));
        assertNotNull(result.authToken());
    }

    @Test
    public void registerFailure() throws ResponseException {
        RegisterRequest request = new RegisterRequest("grant", null, "grant");
        assertThrows(ResponseException.class, () -> facade.register(request));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        String token = registerUser("grant");
        LoginRequest request = new LoginRequest("grant", "grant");
        LoginResult result = facade.login(request);
        assert(token.equals(result.authToken()));
    }

    @Test
    public void loginFailure() throws ResponseException {
        String token = registerUser("grant");
        LoginRequest request = new LoginRequest("grant", "wrongpassword");
        assertThrows(AuthenticationException.class, () -> facade.login(request));

    }

    @Test
    public void logoutSuccess() throws ResponseException {
        String token = registerUser("grant");
        facade.logout(token);
        assertThrows(ResponseException.class, () -> facade.listGames(token));
    }

    @Test
    public void logoutFailure() throws ResponseException {
        assertThrows(ResponseException.class, ()-> facade.logout("badtoken"));
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        String token = registerUser("grant");
        CreateGameRequest request = new CreateGameRequest("grantgame");
        CreateGameResult gameID = facade.createGame(request, token);
        assert(gameID.gameID() >= 1);
    }

    @Test
    public void createGameFailure() throws ResponseException {
        CreateGameRequest request = new CreateGameRequest("yourmom");
        assertThrows(ResponseException.class, () -> facade.createGame(request, "badtoken"));
    }

    @Test
    public void listGameSuccess() throws ResponseException {
        String token = registerUser("grant");
        CreateGameRequest request1 = new CreateGameRequest("game1");
        CreateGameRequest request2 = new CreateGameRequest("game2");
        facade.createGame(request1, token);
        facade.createGame(request2, token);
        assertEquals(2, facade.listGames(token).games().size());
    }
    @Test
    public void listGameFailure() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.listGames("badtoken"));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        String token = registerUser("grant");
        CreateGameRequest game = new CreateGameRequest("game");
        facade.createGame(game, token);
        JoinGameRequest request = new JoinGameRequest("WHITE", 1)
        facade.joinGame(request, token);
        ListGamesResult result = facade.listGames(token);
        List<GameSummary> games = new ArrayList<>(result.games());

        assertEquals("grant", games.getFirst().whiteUsername());

    }
    @Test
    public void clearSuccess() throws ResponseException {
        String token = registerUser("grant");

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
