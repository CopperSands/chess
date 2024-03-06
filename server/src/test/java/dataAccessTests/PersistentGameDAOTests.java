package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.PersistentGameDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentGameDAOTests {

    GameDAO gameDAO;

    @Test
    public void test(){
        gameDAO = new PersistentGameDAO();
        try{
            gameDAO.createGame(null,null,"game", new ChessGame());
        }catch(DataAccessException e){
            assertNull(e);
        }
    }
}
