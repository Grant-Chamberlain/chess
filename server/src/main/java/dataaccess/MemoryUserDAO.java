package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private Map<String, UserData> users = new HashMap<>();




    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }


}
