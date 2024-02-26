package service;

import dataAccess.*;
import org.eclipse.jetty.server.Authentication;

public class ClearAppService {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    public ClearAppService(){
        authDAO = new LocalAuthDAO();
        userDAO = new LocalUserDAO();
        gameDAO = new LocalGameDAO();
    }
    //for phase3
    public ClearAppService(AuthDAO authDAO, UserDAO userDAO,GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException{
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
    }
}
