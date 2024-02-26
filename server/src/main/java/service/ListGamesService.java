package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class ListGamesService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGamesService(){
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();

    }
    //for phase3
    public ListGamesService(AuthDAO authDAO,GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(AuthData authToken) throws DataAccessException {
        //check authentication
        Collection<GameData> list;
        try{
            AuthData foundToken = authDAO.getAuth(authToken.authToken());
            if (foundToken == null){
                throw new DataAccessException("Error unauthorized");
            }
            if(foundToken.authToken() != authToken.authToken() || foundToken.username() != authToken.username()){
                throw new DataAccessException("Error unauthorized");
            }
            list = gameDAO.listGames();

        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
        return list;
    }
//change the following to private after testing
    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }
}
