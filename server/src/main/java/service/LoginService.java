package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import service.helper.AuthString;
import service.helper.HashPassword;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LoginService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(){
        userDAO = new LocalUserDAO();
        authDAO = new LocalAuthDAO();
    }
    //for phase3
    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Logs user in and returns authData
     * @param username
     * @param password
     * @throws DataAccessException
     */
    public AuthData login(String username, String password) throws DataAccessException {
        try{
            AuthData token = null;
            //check if user exists
            UserData user = userDAO.getUser(username);
            if(user == null){
                throw new DataAccessException("Error user does not exit");
            }

            //check passwordHash
            String passwordHash = HashPassword.hashPassword(password);
            String userPass = user.password();
            if (Arrays.equals(userPass.getBytes(StandardCharsets.UTF_8), passwordHash.getBytes(StandardCharsets.UTF_8))){
                String authToken = AuthString.createAuthToken();
                 token = new AuthData(authToken, username);
                authDAO.createAuth(token.authToken(),token.username());

            }
            else{
                throw new DataAccessException("Error Incorrect password");
            }


            return token;

        }catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
    //set the following to private after testing
    public UserDAO getUserDAO() {
        return userDAO;
    }

    public AuthDAO getAuthDAO() {
        return authDAO;
    }
}
