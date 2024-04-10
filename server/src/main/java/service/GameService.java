package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
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

    public GameData makeMove(String authToken, ChessMove move, int gameID) throws DataAccessException{
        try{
            AuthData foundToken = authDAO.getAuth(authToken);
            validation(foundToken,gameID);
            if(game.game().getResigned() != null){
                throw new DataAccessException("Error game over. " + game.game().getResigned() + " team resigned");
            }
            if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK) || game.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                    throw new DataAccessException("Error game over. " + game.blackUsername() + " is in Checkmate" );
                }
                throw new DataAccessException("Error game over. " + game.whiteUsername() + " is in Checkmate");
            }
            ChessGame.TeamColor teamTurn = game.game().getTeamTurn();
            if (!game.blackUsername().equals(foundToken.username()) &&
                    !game.whiteUsername().equals(foundToken.username())){
                throw new DataAccessException("Error you are not a player");
            }
            if (!game.whiteUsername().equals(game.blackUsername())){
                if (game.blackUsername().equals(foundToken.username()) && ChessGame.TeamColor.BLACK != teamTurn){
                    throw new DataAccessException("Error not your turn");
                }
                else if (game.whiteUsername().equals(foundToken.username()) && ChessGame.TeamColor.WHITE != teamTurn){
                    throw new DataAccessException("Error not your turn");
                }
            }
            if (game.game().isInStalemate(teamTurn)){
                throw new DataAccessException("Error game over. You are in Stalemate");
            }
            game.game().makeMove(move);
            gameDAO.updateGame(gameID,game);
            return game;

        } catch (DataAccessException e) {
            throw e;
        } catch (InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ChessGame loadGame(String authToken, String clientColor, int gameID) throws DataAccessException {
        try{
            AuthData foundToken = authDAO.getAuth(authToken);
            validation(foundToken,gameID);
            if (clientColor != null) {
                if (!isValidTeam(clientColor, foundToken.username())) {
                    throw new DataAccessException("Error team already taken");
                }
            }
            return game.game();
        }catch (DataAccessException e){
            throw e;
        }
    }

    public void resignGame(String authToken, int gameID) throws DataAccessException {
        AuthData foundToken = authDAO.getAuth(authToken);
        validation(foundToken,gameID);
        if (game.game().getResigned() != null){
            throw new DataAccessException("Error game over cannot resign");
        }
        String username = getUsername(authToken);

        if (game.whiteUsername().equals(username)){
            game.game().setResigned(ChessGame.TeamColor.WHITE);
        }
        else if (game.blackUsername().equals(username)){
            game.game().setResigned(ChessGame.TeamColor.BLACK);
        }
        else{
            throw new DataAccessException("Error observers cannot resign");
        }
        gameDAO.updateGame(gameID,game);
    }

    private void validation(AuthData foundToken,int gameID) throws DataAccessException{
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
