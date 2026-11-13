package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.LoginRequest;
import service.LoginResult;
import service.RegisterRequest;
import service.RegisterResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
        LoginRequest request = new LoginRequest("grant", token);
        LoginResult result = facade.login(request);
        assert(token.equals(result.authToken()));
    }

    @Test
    public void loginFailure() throws ResponseException {
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
