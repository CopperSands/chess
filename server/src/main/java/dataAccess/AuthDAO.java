package dataAccess;

import model.AuthData;

public interface AuthDAO {

    /**
     * Create an authtoken
     * @param authToken
     * @param username
     */
    public void createAuth(String authToken,String username);


    /**
     * Get the authToken from storage
     * @param authToken
     * @return AuthData object
     */
    public AuthData getAuth(AuthData authToken);

    /**
     * Delete an auth Token given an authToken
     * @param authToken
     */
    public void deleteAuth(AuthData authToken);


}
