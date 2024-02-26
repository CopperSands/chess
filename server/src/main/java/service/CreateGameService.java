package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;

public class CreateGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public CreateGameService(){
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();

    }
    //for phase3
    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int CreateGame(AuthData authToken, String gameName) throws  DataAccessException{
        int gameID;
        //check authToken
        try{
            AuthData returned = authDAO.getAuth(authToken.authToken());

            if(returned == null){
                throw new DataAccessException("Error unauthorized");
            }
            if (returned.authToken() != authToken.authToken() || returned.username() != authToken.username()){
                throw new DataAccessException("Error unauthorized");
            }
            ChessGame game = new ChessGame();
            gameID = gameDAO.createGame(null,null,gameName,game);

        }catch(DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }

        return gameID;
    }
//change the following to private after testing
    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }
}
