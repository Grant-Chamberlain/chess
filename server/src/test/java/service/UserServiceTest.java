package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    }

    @Test
    void registerSuccess() throws DataAccessException {
        RegisterResult result = userService.register(new RegisterRequest("grant", "password", "grant@gmail"));

        assertEquals("grant", result.username());
        assertNotNull(result.authToken());
    }
    @Test
    void registerDuplicateUsername() throws DataAccessException {
        userService.register(new RegisterRequest("grant", "password", "grant@gmail.com"));

        assertThrows(AlreadyTakenException.class, () -> userService.register(new RegisterRequest("grant", "password", "grant@gmail.com")));
    }
    @Test
    void loginsuccess() throws DataAccessException {
        userService.register(new RegisterRequest("grant", "password", "grant@gmail"));
        LoginRequest request = new LoginRequest("grant", "password");
        LoginResult login = userService.login(request);

        assertEquals("grant", login.username());
        assertNotNull(login.authToken());
    }
    @Test
    void wrongpasswordlogin() throws DataAccessException {
        userService.register(new RegisterRequest("grant", "password", "grant@gmail"));
        LoginRequest request = new LoginRequest("grant", "wrongpassword");


        assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }
    @Test
    void logoutsuccess() throws DataAccessException {
        userService.register(new RegisterRequest("grant", "password", "grant@gmail"));
        LoginRequest request = new LoginRequest("grant", "password");
        LoginResult login = userService.login(request);
        userService.logout(login.authToken());

        assertThrows(UnauthorizedException.class, () -> userService.logout(login.authToken()));
    }
    @Test
    void logoutbadtoken() throws DataAccessException {
        userService.register(new RegisterRequest("grant", "password", "grant@gmail"));
        LoginRequest request = new LoginRequest("grant", "password");
        LoginResult login = userService.login(request);

        assertThrows(UnauthorizedException.class, () -> userService.logout("badtoken"));
    }

}
