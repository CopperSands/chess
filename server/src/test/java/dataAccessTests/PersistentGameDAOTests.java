package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.PersistentGameDAO;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentGameDAOTests {

    private GameDAO gameDAO;
    private static GameData gameData = new GameData(1,null,null,"game", new ChessGame());
    private static GameData gameData1 = new GameData(2,"hello","there","game1",new ChessGame());

    @BeforeEach
    public void setup(){
        gameDAO = new PersistentGameDAO();
        try{
            gameDAO.clear();
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @AfterEach
    public void cleanup(){
        try{
            gameDAO.clear();
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void createGame(){
        try{
            gameDAO.createGame(null,null,"game", new ChessGame());
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void failedCreateGame(){
        try{
            gameDAO.createGame(gameData.whiteUsername(),gameData.blackUsername(),"game", null);
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error creating game");
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }

    @Test
    public void validGetGame(){
        try{
            int gameID = gameDAO.createGame(gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),gameData.game());
            GameData returned = gameDAO.getGame(gameID);
            assertEquals(gameData.getClass(), returned.getClass());
            assertEquals(gameData.whiteUsername(), returned.whiteUsername());
            assertEquals(gameData.blackUsername(), returned.blackUsername());
            assertEquals(gameData.gameName(), returned.gameName());
            assertEquals(gameData.game().getBoard(),returned.game().getBoard());
            assertEquals(gameData.game().getTeamTurn(), returned.game().getTeamTurn());
        } catch (DataAccessException e) {
            assertNull(e);
        }
    }

    @Test
    public void badGetGame(){
        int gameID = 2;
        try{
            GameData returned = gameDAO.getGame(gameID);
            assertNull(returned);
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void validListGames(){
        try{
           gameDAO.createGame(gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),gameData.game());
           gameDAO.createGame(gameData1.whiteUsername(),gameData1.blackUsername(), gameData1.gameName(), gameData1.game());
           ArrayList<GameData> list = (ArrayList<GameData>) gameDAO.listGames();
           assertEquals(2, list.size());

           GameData found = list.get(0);
           GameData found1 = list.get(1);
           assertEquals(gameData.whiteUsername(), found.whiteUsername());
           assertEquals(gameData.blackUsername(),found.blackUsername());
           assertEquals(gameData.gameName(), found.gameName());
           assertEquals(gameData.game().getBoard(),found.game().getBoard());
           assertEquals(gameData.game().getTeamTurn(), found.game().getTeamTurn());
           assertEquals(gameData1.whiteUsername(), found1.whiteUsername());
           assertEquals(gameData1.blackUsername(),found1.blackUsername());
           assertEquals(gameData1.gameName(), found1.gameName());
           assertEquals(gameData1.game().getBoard(),found1.game().getBoard());
           assertEquals(gameData1.game().getTeamTurn(), found1.game().getTeamTurn());
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void emptyListGames(){
        try{
            Collection<GameData> list = gameDAO.listGames();
            assertEquals(0,list.size());
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void validUpdateGame(){
        try{
            int gameID = gameDAO.createGame(gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameID,gameData1);
            GameData found = gameDAO.getGame(gameID);
            assertEquals(gameData1.whiteUsername(), found.whiteUsername());
            assertEquals(gameData1.blackUsername(), found.blackUsername());
            assertEquals(gameData.gameName(),found.gameName());
            assertEquals(gameData1.game().getBoard(),found.game().getBoard());
            assertEquals(gameData1.game().getTeamTurn(), found.game().getTeamTurn());

        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void badUpdateGame(){
        try{
            gameDAO.updateGame(3,gameData);
            GameData found = gameDAO.getGame(3);
            assertNull(found);
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void clearTest(){
        try{
            gameDAO.createGame(gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),gameData.game());
            gameDAO.createGame(gameData1.whiteUsername(),gameData1.blackUsername(),gameData1.gameName(),gameData1.game());
            gameDAO.clear();
            Collection<GameData> list = gameDAO.listGames();
            assertEquals(0, list.size());
        }catch(DataAccessException e){
            assertNull(e);
        }

    }
}
