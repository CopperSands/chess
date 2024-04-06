package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;

public class GameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameData game;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ChessGame loadGame(String authToken, String clientColor, int gameID) throws DataAccessException {
        try{
            AuthData foundToken = authDAO.getAuth(authToken);
            joinValidation(foundToken,gameID);
            if (clientColor != null) {
                if (!isValidTeam(clientColor, foundToken.username())) {
                    throw new DataAccessException("Error team already taken");
                }
            }
            return game.game();
        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private void joinValidation(AuthData foundToken,int gameID) throws DataAccessException{
        if (foundToken == null){
            throw new DataAccessException("Error unauthorized");
        }
        game = gameDAO.getGame(gameID);
        if (game == null){
            throw new DataAccessException("Error bad request");
        }
    }

    private boolean isValidTeam(String clientColor, String username) throws DataAccessException{
        boolean isValid = false;
        if (clientColor.equals("WHITE")){
            if (game.whiteUsername() == null){
                throw new DataAccessException("Error empty player field");
            }
            if (game.whiteUsername().equals(username)){
                isValid = true;
            }
        }
        else if (clientColor.equals("BLACK")){
            if (game.blackUsername() == null) {
                throw new DataAccessException("Error empty player field");
            }
            if (game.blackUsername().equals(username)){
                isValid = true;
            }
        }
        return isValid;
    }
    public String getUsername(String authToken){
        try {
            AuthData token = authDAO.getAuth(authToken);
            String username = token.username();
            return username;
        } catch (DataAccessException e) {
            System.out.println("Error finding username");
        }
        return null;
    }
}
