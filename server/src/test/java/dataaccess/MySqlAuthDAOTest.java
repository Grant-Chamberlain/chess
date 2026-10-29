package dataaccess;

import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MySqlAuthDAOTest {
    private MySqlUserDAO userDAO;
    private MySqlAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseInitializer.initialize();
        userDAO = new MySqlUserDAO();
        userDAO.clear();
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail.com"));
        authDAO.createAuth(new AuthData("token", "grant"));

        assert(authDAO.getAuth("token") != null && authDAO.getAuth("token").username().equals("grant"));
    }
    @Test
    public void createAuthDuplicateFails() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "grant"));

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(new AuthData("token", "grant")));
    }
    @Test
    public void getAuthSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "grant"));

        assertEquals("token", authDAO.getAuth("token").authToken());
        assertEquals("grant", authDAO.getAuth("token").username());
    }
    @Test
    public void deleteAuthSucces() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "grant"));
        authDAO.deleteAuth("token");
        assert(authDAO.getAuth("token") == null);
    }
    @Test
    public void getAuthNotFound() throws DataAccessException {
        assert(authDAO.getAuth("badtoken") == null);
    }
    @Test
    public void deleteAuthButNotAll() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "grant"));
        authDAO.createAuth(new AuthData("token2", "john"));
        authDAO.deleteAuth("token");

        assert(authDAO.getAuth("token2") != null);

    }
    @Test
    public void clearSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("token", "grant"));
        authDAO.clear();
        assert(authDAO.getAuth("token") == null);
    }
}
