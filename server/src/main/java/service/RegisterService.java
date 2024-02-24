package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

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

    public AuthData register(String username, String password, String email) throws DataAccessException{
        //check if user exits
        try {

            if(isUsernameTaken(username)){
                throw new DataAccessException("Error username is taken");
            }

            userDAO.createUser(username, password, email);

            //create new authToken and insert into database
            authToken = createAuthToken();
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

    /**
     * Creates a unique token string
     * @return
     */
    private String createAuthToken(){
        //use
        StringBuilder token = new StringBuilder();

        LocalDateTime time = LocalDateTime.now();

        //add 7 random chars to the string
        Random random = new Random();
        for (int i = 0; i < 8; i++){
            // num is an ascii char in decimal starting at 0 ending at z
            int num = random.nextInt(74) + 48;
            //choose a new char if \ or ; or `
            if (num == 92 || num == 59 || num == 96){
                i--;
            }
            else{
                token.append(num);
            }
        }
        token.append(time);
        //for future hash the string before returning for more security

        return token.toString();
    }

}
