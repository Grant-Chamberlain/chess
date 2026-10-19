package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private Map<String, UserData> users = new HashMap<>();




    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData d = users.get(username);
        return d;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        users.put(data.username(), data);

    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }


}
