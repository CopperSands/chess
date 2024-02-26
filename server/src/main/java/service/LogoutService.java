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

    public void logout(String authToken) throws DataAccessException{
        try{
            AuthData foundToken = authDAO.getAuth(authToken);
            //check if authToken is valid
            if (foundToken == null){
                throw new DataAccessException("Error unauthorized");
            }
            authDAO.deleteAuth(foundToken);

        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
//change to private after testing
    public AuthDAO getAuthDAO() {
        return authDAO;
    }
}
