package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private Map<Integer, GameData> games = new HashMap<>();
    private int counter = 0;

    @Override
    public int createGame(String gameName) throws DataAccessException {
        counter += 1;
        games.put(counter, new GameData(counter,null, null, gameName, new ChessGame()));
        return counter;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.replace(game.gameID(), game);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
