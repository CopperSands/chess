package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.PersistentGameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentGameDAOTests {

    GameDAO gameDAO;

    @BeforeEach
    public void setup(){
        gameDAO = new PersistentGameDAO();
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
            gameDAO.createGame(null,null,"game", null);
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error creating game");
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }
}
