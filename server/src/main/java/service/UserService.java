package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    private String createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(token, username));
        return token;
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        if (request.password() == null || request.username() == null){
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(request.username());

        if (user == null || !BCrypt.checkpw(request.password(), user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
       String token = createAuth(request.username());
       return new LoginResult(request.username(), token);
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        UserData user = new UserData(request.username(), hashedPassword, request.email());
        String token = createAuth(request.username());
        userDAO.createUser(user);
        return new RegisterResult(request.username(), token);


    }
    public void logout(String token) throws DataAccessException {
        AuthData auth = authDAO.getAuth(token);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(token);
    }
}
