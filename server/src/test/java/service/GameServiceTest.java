package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameDAO gameDAO;
    private GameService gameService;
    private final String token = "test";

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
        authDAO.createAuth(new AuthData(token, "grant"));
    }
    @Test
    void createGameSuccess() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);

        assertNotNull(gameDAO.getGame(result.gameID()));
    }
    @Test
    void createGameFailure() throws DataAccessException {


        assertThrows(UnauthorizedException.class, () -> gameService.createGame(new CreateGameRequest("my game"), "bad_token"));
    }
    @Test
    void gamenamenull() throws DataAccessException {
        assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateGameRequest(null), token));
    }
    @Test
    void listgamessuccess() throws DataAccessException {
        CreateGameResult result1 = gameService.createGame(new CreateGameRequest("game1"), token);
        CreateGameResult result2 = gameService.createGame(new CreateGameRequest("game2"), token);
        ListGamesResult result = gameService.listGames(token);
        assertEquals(2, result.games().size());
    }
    @Test
    void listgamesfailure() throws DataAccessException {
        CreateGameResult result1 = gameService.createGame(new CreateGameRequest("game1"), token);
        CreateGameResult result2 = gameService.createGame(new CreateGameRequest("game2"), token);
        assertThrows(UnauthorizedException.class, ()-> gameService.listGames("bad_token"));
    }
    @Test
    void joingamesuccesswhite() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);
        JoinGameRequest request = new JoinGameRequest("WHITE", result.gameID());
        gameService.joinGame(request, token);

        assertEquals("grant", gameDAO.getGame(request.gameID()).whiteUsername());
    }
    @Test
    void joingamesuccessblack() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);
        JoinGameRequest request = new JoinGameRequest("BLACK", result.gameID());
        gameService.joinGame(request, token);

        assertEquals("grant", gameDAO.getGame(request.gameID()).blackUsername());
    }
    @Test
    void joingamecolortaken() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);
        JoinGameRequest request = new JoinGameRequest("WHITE", result.gameID());
        gameService.joinGame(request, token);

        assertThrows(AlreadyTakenException.class, ()-> gameService.joinGame(request, token));
    }
    @Test
    void joingamebadcolor() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);
        JoinGameRequest request = new JoinGameRequest("Green", result.gameID());

        assertThrows(BadRequestException.class, ()-> gameService.joinGame(request, token));
    }
    @Test
    void joingamenogame() throws DataAccessException {
        CreateGameResult result = gameService.createGame(new CreateGameRequest("my game"), token);
        JoinGameRequest request = new JoinGameRequest("WHITE", 1234);

        assertThrows(BadRequestException.class, ()-> gameService.joinGame(request, token));
    }


}
