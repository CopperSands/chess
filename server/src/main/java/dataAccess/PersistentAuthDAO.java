package dataAccess;

import model.AuthData;

public class PersistentAuthDAO implements AuthDAO{
    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
