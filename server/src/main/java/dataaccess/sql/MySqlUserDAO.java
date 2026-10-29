package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM user WHERE username = ?";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
             statement.setString(1, username);
             try (var resultSet = statement.executeQuery()) {
                 if (resultSet.next()) {
                     return new UserData(
                             resultSet.getString("username"),
                             resultSet.getString("password"),
                             resultSet.getString("email"));
                 }
                 return null;

             }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get user", e);
        }
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var connection = DatabaseManager.getConnection();
            var statement = connection.prepareStatement(sql)) {
            statement.setString(1, data.username());
            statement.setString(2, data.password());
            statement.setString(3, data.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE user";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear users", e);
        }
    }
}
