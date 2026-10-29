package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO{
    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, data.authToken());
            statement.setString(2, data.username());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create auth", e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, authToken);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(
                            resultSet.getString("authToken"),
                            resultSet.getString("username"));
                }
                return null;

            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get auth", e);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
             statement.setString(1, authToken);
             statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to delete auth", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE auth";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear auth", e);
        }

    }
}
