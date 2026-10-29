package dataaccess;

import chess.ChessGame;
import dataaccess.sql.MySqlGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySqlGameDAOTest {
    private MySqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException{
        DatabaseInitializer.initialize();
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        assert(gameID > 0);
        assert(gameDAO.getGame(gameID) != null && gameDAO.getGame(gameID).gameName().equals("game1"));
    }
    @Test
    public void createGameNullName() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }
    @Test
    public void getGameSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameID);

        assert(game.gameID() == gameID && game.gameName().equals("game1") && game.whiteUsername() == null);
    }
    @Test
    public void listGameSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        gameDAO.createGame("game2");

        assert(gameDAO.listGames().size() == 2);
    }
    @Test
    public void listGameempty() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();

        assertTrue(games.isEmpty());
    }
    @Test
    public void clearSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        gameDAO.clear();

        assert(gameDAO.listGames().isEmpty());
    }
    @Test
    public void updateGameSuccess() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        GameData gameData = new GameData(gameID, "grant", null, "game1", new ChessGame());
        gameDAO.updateGame(gameData);

        assert(gameDAO.getGame(gameID).whiteUsername().equals("grant"));
    }
}
