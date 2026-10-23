package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearServiceTest {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    public void clearremovesdata() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail"));
        authDAO.createAuth(new AuthData("1234", "grant"));
        gameDAO.createGame("testGame");

        assertNotNull(userDAO.getUser("grant"));
        assertNotNull(authDAO.getAuth("1234"));
        assertEquals(1, gameDAO.listGames().size());

        clearService.clear();

        assertNull(userDAO.getUser("grant"));
        assertNull(authDAO.getAuth("1234"));
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void clearonempty() throws DataAccessException {
        clearService.clear();

        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void clearresetgame() throws DataAccessException {
        gameDAO.createGame("firstGame");
        clearService.clear();

        assertEquals(1, gameDAO.createGame("newGame"));
    }
}