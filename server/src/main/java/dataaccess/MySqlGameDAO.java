package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String sql = "INSERT INTO game (gameName, game) VALUES (?, ?)";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, gameName);
            statement.setString(2, gson.toJson(new ChessGame()));
            statement.executeUpdate();
            try (var keys = statement.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create game", e);
        }
    }

    private GameData readGame(ResultSet resultSet) throws SQLException {
        ChessGame chessGame = gson.fromJson(resultSet.getString("game"), ChessGame.class);
        return new GameData(
                resultSet.getInt("gameID"),
                resultSet.getString("whiteUsername"),
                resultSet.getString("blackUsername"),
                resultSet.getString("gameName"),
                chessGame);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String sql = "SELECT * FROM game";
        ArrayList<GameData> games = new ArrayList<>();
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                games.add(readGame(resultSet));
            }
            return games;


        } catch (SQLException e) {
            throw new DataAccessException("failed to list games", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameID);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return readGame(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("failed to get game", e);
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, gson.toJson(game.game()));
            statement.setInt(5, game.gameID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to update game", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE game";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear game", e);
        }
    }


}
