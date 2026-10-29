package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
