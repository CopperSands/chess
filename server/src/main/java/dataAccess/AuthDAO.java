package dataAccess;

import model.AuthData;

public interface AuthDAO {

    /**
     * Create an authtoken
     * @param authToken
     * @param username
     */
    public void createAuth(String authToken,String username) throws DataAccessException;


    /**
     * Get the authToken from storage
     * @param authToken
     * @return AuthData object
     */
    public AuthData getAuth(String authToken) throws DataAccessException;

    /**
     * Delete an auth Token given an authToken
     * @param authToken
     */
    public void deleteAuth(AuthData authToken) throws DataAccessException;

    /**
     * Clears all authTokens
     * @throws DataAccessException
     */
    public void clear()throws DataAccessException;


}
