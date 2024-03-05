package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean isEqual = encoder.matches(password,user.password());
            if (isEqual){
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
}
