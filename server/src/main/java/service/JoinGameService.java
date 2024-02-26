package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;


public class JoinGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameData game;

    public JoinGameService(){
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();
    }

    public void joinGame(AuthData authToken, String clientColor, int gameID)throws DataAccessException {
        try{
            AuthData returned = authDAO.getAuth(authToken.authToken());
            if (returned == null){
                throw new DataAccessException("Error unauthorized");
            }
            if (returned.username() != authToken.username()){
                throw new DataAccessException("Error unauthorized");
            }
            game = gameDAO.getGame(gameID);
            if (game == null){
                throw new DataAccessException("Error game not found");
            }
            if (isTeamTaken(clientColor)){
                throw new DataAccessException("Error already taken");
            }
            updateGameData(authToken.username(), clientColor);
            gameDAO.updateGame(game.gameID(),game);



        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    private boolean isTeamTaken(String clientColor){
        boolean isTaken = false;
        if (clientColor == "WHITE"){
            if (game.whiteUsername() != null){
                isTaken = true;
            }
        }
        else if (clientColor == "BLACK"){
            if (game.blackUsername() != null){
                isTaken = true;
            }
        }

        return isTaken;
    }

    private void updateGameData(String username, String clientColor) throws DataAccessException{
        if (clientColor == "WHITE"){
            game = new GameData(game.gameID(),username, game.blackUsername(),game.gameName(),game.game());
        }
        else if (clientColor == "BLACK"){
            game = new GameData(game.gameID(),game.whiteUsername(),username,game.gameName(),game.game());
        }
        else{
            throw new DataAccessException("Error invalid team color");
        }
    }
// change the following to private after testing
    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }
}
