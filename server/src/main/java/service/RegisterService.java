package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import service.helper.AuthString;
import service.helper.HashPassword;

import java.time.LocalDateTime;
import java.util.Random;

public class RegisterService {


    private String authToken;
    private UserDAO userDAO;
    private AuthDAO authDAO;


    public RegisterService(){
        userDAO = new LocalUserDAO();
        authDAO = new LocalAuthDAO();
    }
    //for phase3
    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Registers user and returns authData
     * @param username
     * @param password
     * @param email
     * @return
     * @throws DataAccessException
     */
    public AuthData register(String username, String password, String email) throws DataAccessException{
        //check if user exits
        try {
            if (username == null || password == null || email == null){
                throw new DataAccessException("Error bad request");
            }

            if(isUsernameTaken(username)){
                throw new DataAccessException("Error username is taken");
            }

            String passwordhash = HashPassword.hashPassword(password);

            userDAO.createUser(username, passwordhash , email);

            //create new authToken and insert into database
            authToken = AuthString.createAuthToken();
            AuthData token = new AuthData(authToken, username);
            authDAO.createAuth(token.authToken(),token.username());

            return token;
        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    /**
     * Checks if the username is already taken
     * @param username
     * @return
     * @throws DataAccessException
     */
    private boolean isUsernameTaken(String username) throws DataAccessException {
        try{
            boolean isTaken = false;
            UserData user = userDAO.getUser(username);
            if(user != null){
                isTaken = true;
            }
            return isTaken;

        } catch (DataAccessException e){
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
