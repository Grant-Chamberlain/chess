package dataaccess;

import dataaccess.sql.MySqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTest {
    private MySqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseInitializer.initialize();
        userDAO = new MySqlUserDAO();
        userDAO.clear();
    }

    @Test
    public void createusersuccess() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail.com"));

        UserData found = userDAO.getUser("grant");

        assertNotNull(found);
        assertEquals("grant", found.username());
        assertEquals("grant@gmail.com", found.email());
    }

    @Test
    public void creatuserfail() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail.com"));

        UserData found = userDAO.getUser("grant");

        assertNotNull(found);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("grant", "password2", "grant@gmail.com")));
    }
    @Test
    public void getUserSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail.com"));

        assert(userDAO.getUser("grant").password().equals("password"));
        assert(userDAO.getUser("grant").email().equals("grant@gmail.com"));
    }
    @Test
    public void getUserNotFound() throws DataAccessException {
        assert(userDAO.getUser("nobody") == null);
    }
    @Test
    public void clearsuccess() throws DataAccessException {
        userDAO.createUser(new UserData("grant", "password", "grant@gmail.com"));
        userDAO.clear();
        assert(userDAO.getUser("grant") == null);
    }
}
