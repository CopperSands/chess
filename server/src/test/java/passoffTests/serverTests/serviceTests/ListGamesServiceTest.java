package passoffTests.serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.*;
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
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();
        listGamesService = new ListGamesService(authDAO,gameDAO);
        try{
            //populate data
            authDAO.createAuth(authData.authToken(),authData.username());
            gameDAO.createGame(game.whiteUsername(),game.blackUsername(), game.gameName(),game.game());
            gameDAO.createGame(game1.whiteUsername(),game1.blackUsername(),game1.gameName(),game1.game());
            gameDAO.createGame(game2.whiteUsername(),game2.blackUsername(),game2.gameName(),game2.game());

            Collection<GameData> result = listGamesService.listGames(authData.authToken());
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
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();
        listGamesService = new ListGamesService(authDAO,gameDAO);
        try{
            //populate data
            authDAO.createAuth(authData.authToken(),authData.username());
            gameDAO.createGame(game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
            gameDAO.createGame(game1.whiteUsername(), game1.blackUsername(),game1.gameName(),game1.game());
            gameDAO.createGame(game2.whiteUsername(), game2.blackUsername(),game2.gameName(),game2.game());
            AuthData badData = new AuthData("nope", "nope");
            try{
                Collection<GameData> result = listGamesService.listGames(badData.authToken());
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
