package passoffTests.serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameServiceTest {

    private CreateGameService createGameService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private static AuthData authData = new AuthData("this is a token", "user");

    @Test
    public void validGameCreation(){
        authDAO = new LocalAuthDAO();
        gameDAO = new LocalGameDAO();
        createGameService = new CreateGameService(authDAO,gameDAO);
        try{
            authDAO.createAuth(authData.authToken(),authData.username());

            int game1ID = createGameService.createGame(authData,"game1");
            int game2ID = createGameService.createGame(authData,"game2");
            //check for consistency
            GameData game1 = gameDAO.getGame(game1ID);
            GameData game2 = gameDAO.getGame(game2ID);
            assertNotNull(game1);
            assertNotNull(game2);
            assertEquals(game1ID,game1.gameID());
            assertEquals(game2ID,game2.gameID());
            assertEquals("game1", game1.gameName());
            assertEquals("game2", game2.gameName());
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void badAuthentication(){
        createGameService = new CreateGameService();

        try{
            createGameService.createGame(authData,"game1");
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error unauthorized");
            assertEquals(expected.getClass(),e.getClass());
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }


}
