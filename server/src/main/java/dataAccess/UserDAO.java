package dataAccess;

import model.UserData;

public interface UserDAO {

    /**
     * Takes information for a user and creates a new user.
     * @param username
     * @param password
     * @param email
     */
    public void createUser(String username, String password, String email);

    /**
     * Gets the userData based on username.
     * @param username
     * @return UserData record
     */
    public UserData getUser(String username);

    /**
     * Clears all user data
     */
    public void clear();


}
