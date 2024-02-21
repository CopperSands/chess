package dataAccess;

import model.UserData;

public interface UserDAO {

    /**
     * Takes information for a user and creates a new user.
     * @param username
     * @param password
     * @param email
     */
    public void createUser(String username, String password, String email) throws DataAccessException;

    /**
     * Gets the userData based on username.
     * @param username
     * @return UserData record
     */
    public UserData getUser(String username) throws DataAccessException;

    /**
     * Clears all user data
     */
    public void clear() throws DataAccessException;


}
