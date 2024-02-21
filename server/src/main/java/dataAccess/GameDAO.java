package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    /**
     * Creates a new game
     * @param gameID
     * @param whiteUsername
     * @param blackUsername
     * @param gameName
     * @param game
     */
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;


    /**
     * Get a game by the gameID
     * @param gameID
     * @return GameData record
     */
    public GameData getGame(int gameID) throws DataAccessException;

    /**
     * Get all games
     * @return Collection of GameData
     */
    public Collection<GameData> listGames() throws DataAccessException;

    /**
     * Takes in a gameID and GameData object to update the game
     * @param gameID
     * @param updatedGame
     */
    public void updateGame(int gameID, GameData updatedGame) throws DataAccessException;


    /**
     * Clears all Game data
     */
    public void clear() throws DataAccessException;
}
