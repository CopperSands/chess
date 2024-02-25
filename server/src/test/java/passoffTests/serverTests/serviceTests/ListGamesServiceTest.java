package passoffTests.serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.ListGamesService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesServiceTest {

    private ListGamesService listGamesService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private static AuthData authData = new AuthData("this is a token", "eve");
    private static GameData game = new GameData(1,"john","bob","game1232",new ChessGame());
    private static GameData game1 = new GameData(2,"jim","bob","game1245",new ChessGame());
    private static GameData game2 = new GameData(3,"bob","alice","game1267",new ChessGame());

    @Test
    public void validListGames(){
        listGamesService = new ListGamesService();
        authDAO = listGamesService.getAuthDAO();
        gameDAO = listGamesService.getGameDAO();

        try{
            //populate data
            authDAO.createAuth(authData.authToken(),authData.username());
            gameDAO.createGame(game);
            gameDAO.createGame(game1);
            gameDAO.createGame(game2);

            Collection<GameData> result = listGamesService.listGames(authData);
            assertNotNull(result);
            assertTrue(result.contains(game));
            assertTrue(result.contains(game1));
            assertTrue(result.contains(game2));
            assertEquals(3,result.size());

        }catch(DataAccessException e){
            assertNull(e);
        }

    }

    @Test
    public void badAuth(){
        listGamesService = new ListGamesService();
        authDAO = listGamesService.getAuthDAO();
        gameDAO = listGamesService.getGameDAO();

        try{
            //populate data
            authDAO.createAuth(authData.authToken(),authData.username());
            gameDAO.createGame(game);
            gameDAO.createGame(game1);
            gameDAO.createGame(game2);
            AuthData badData = new AuthData("nope", "nope");
            try{
                Collection<GameData> result = listGamesService.listGames(badData);
            }catch(DataAccessException e){
                DataAccessException expected = new DataAccessException("Error unauthorized");
                assertEquals(expected.getClass(),e.getClass());
                assertEquals(expected.getMessage(),e.getMessage());
            }


        }catch(DataAccessException e){
            assertNull(e);
        }
    }
}
