package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.LocalAuthDAO;
import model.AuthData;

public class LogoutService {

    private AuthDAO authDAO;

    public LogoutService(){
        authDAO = new LocalAuthDAO();
    }

    //this is for phase3
    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void logout(AuthData authToken) throws DataAccessException{
        try{
            AuthData result = authDAO.getAuth(authToken.authToken());
            //check if authToken is valid
            if (result == null){
                throw new DataAccessException("Error unauthorized");
            }
            authDAO.deleteAuth(authToken);

        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
//change to private after testing
    public AuthDAO getAuthDAO() {
        return authDAO;
    }
}
