package service;

import dataAccess.*;
import model.UserData;

public class LoginService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public LoginService(){
        userDAO = new LocalUserDAO();
        authDAO = new LocalAuthDAO();
    }

    public void login(String username, String password) throws DataAccessException {
        try{
            UserData user = userDAO.getUser(username);

        }catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
