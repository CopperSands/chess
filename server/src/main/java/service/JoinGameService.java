package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.nio.charset.StandardCharsets;


public class JoinGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameData game;

    public JoinGameService(){
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();
    }
    //for phase3
    public JoinGameService(AuthDAO authDAO,GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, String clientColor, int gameID)throws DataAccessException {
        try{
            AuthData foundToken = authDAO.getAuth(authToken);
            if (foundToken == null){
                throw new DataAccessException("Error unauthorized");
            }
            game = gameDAO.getGame(gameID);
            if (game == null){
                throw new DataAccessException("Error bad request");
            }
            if (clientColor != null) {
                if (isTeamTaken(clientColor, foundToken.username())) {
                    throw new DataAccessException("Error already taken");
                }
                updateGameData(foundToken.username(), clientColor);
                gameDAO.updateGame(game.gameID(), game);
            }
        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    private boolean isTeamTaken(String clientColor, String username){
        boolean isTaken = false;
        if (clientColor.equals("WHITE")) {
            if (game.whiteUsername() != null && !game.whiteUsername().equals(username)) {
                isTaken = true;
            }
        }
        else if (clientColor.equals("BLACK")){
            if (game.blackUsername() != null && !game.blackUsername().equals(username)){
                isTaken = true;
            }
        }

        return isTaken;
    }

    private void updateGameData(String username, String clientColor) throws DataAccessException{
        if (clientColor.equals("WHITE")){
            game = new GameData(game.gameID(),username, game.blackUsername(),game.gameName(),game.game());
        }
        else if (clientColor.equals("BLACK")){
            game = new GameData(game.gameID(),game.whiteUsername(),username,game.gameName(),game.game());
        }
        else{
            throw new DataAccessException("Error invalid team color");
        }
    }
}
