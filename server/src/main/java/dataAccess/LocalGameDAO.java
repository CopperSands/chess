package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class LocalGameDAO implements GameDAO{

    private HashMap<Integer,GameData> games;

    public LocalGameDAO(){
        games = new HashMap<Integer,GameData>();
    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        //if the game alreadyString whiteUsername, String blackUsername, String gameName, ChessGame game exits throw error
        int gameID = games.size() + 1;
        GameData gameData = new GameData(gameID,whiteUsername,blackUsername,gameName, game);
            try{
                games.put(gameID, gameData);
            }catch(Exception e){
                throw new DataAccessException(e.getMessage());
            }
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {

        try{
            GameData gameData = games.get(gameID);
            return gameData;
        }catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try{
            ArrayList<GameData> list = new ArrayList<GameData>();

            //iterates through games and then adds each unique game to the list
            games.forEach((gameID,game) -> list.add(game));

            return list;
        }catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void updateGame(int gameID, GameData updatedGame) throws DataAccessException {
        if (!games.containsKey(gameID)){
            throw new DataAccessException("game does not exit");
        }
        else {
            try{
                games.put(gameID,updatedGame);
            }catch (Exception e){
                throw new DataAccessException(e.getMessage());
            }
        }

    }

    @Override
    public void clear() throws DataAccessException {
        try{
            games.clear();
        }catch (Exception e){
            throw new DataAccessException("Error clearing games " + e.getMessage());
        }

    }
}
