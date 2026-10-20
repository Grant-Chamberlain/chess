package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    private AuthData verifyToken(String token) throws DataAccessException {
        AuthData auth = authDAO.getAuth(token);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

    public ListGamesResult listGames(String token) throws DataAccessException {
        verifyToken(token);
        Collection<GameData> games = gameDAO.listGames();
        Collection<GameSummary> game_sum = new ArrayList<>();
        for (GameData game : games) {
            GameSummary sum = new GameSummary(game.gameID(),game.whiteUsername(), game.blackUsername(), game.gameName());
            game_sum.add(sum);
        }
        return new ListGamesResult(game_sum);
    }

    public CreateGameResult createGame(CreateGameRequest request, String token) throws DataAccessException {
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");

        }
        verifyToken(token);
        int gameID = gameDAO.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinGameRequest request, String token) throws DataAccessException {
        if (request.playerColor() == null || (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK"))) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData auth = verifyToken(token);
        GameData game = gameDAO.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (request.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                GameData newGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                gameDAO.updateGame(newGame);
            }
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                GameData newGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                gameDAO.updateGame(newGame);
            }
        }
    }

}
