package passoffTests.serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.LocalAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;
public class JoinReqServiceTest {

    private JoinGameService joinGameService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private static AuthData authData = new AuthData("this is a token", "user");
    private static AuthData authData1 = new AuthData("this is also a token", "user1");

    @Test
    public void validJoin(){
        joinGameService = new JoinGameService();
        authDAO = joinGameService.getAuthDAO();
        gameDAO = joinGameService.getGameDAO();

        try{
            authDAO.createAuth(authData.authToken(),authData.username());
            authDAO.createAuth(authData1.authToken(),authData1.username());
            int gameID = gameDAO.createGame(null,null,"game1",new ChessGame());
            joinGameService.joinGame(authData.authToken(),"BLACK",gameID);
            joinGameService.joinGame(authData1.authToken(),"WHITE", gameID);
            //compare results
            GameData game = gameDAO.getGame(gameID);
            assertNotNull(game);
            assertEquals("game1",game.gameName());
            assertEquals(authData.username(),game.blackUsername());
            assertEquals(authData1.username(),game.whiteUsername());
            assertNotNull(game.game());

        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void badAuth(){
        joinGameService = new JoinGameService();
        authDAO = joinGameService.getAuthDAO();
        gameDAO = joinGameService.getGameDAO();

        try{
            int gameID = gameDAO.createGame(null,null,"game1",new ChessGame());
            joinGameService.joinGame(authData.authToken(),"BLACK",gameID);
        }catch (DataAccessException e){
            DataAccessException expected = new DataAccessException("Error unauthorized");
            assertEquals(expected.getClass(),e.getClass());
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }

    @Test
    public void teamAlreadyTaken(){
        joinGameService = new JoinGameService();
        authDAO = joinGameService.getAuthDAO();
        gameDAO = joinGameService.getGameDAO();

        try{
            authDAO.createAuth(authData.authToken(),authData.username());
            authDAO.createAuth(authData1.authToken(),authData1.username());
            int gameID = gameDAO.createGame(null,null,"game1",new ChessGame());
            joinGameService.joinGame(authData.authToken(),"BLACK",gameID);
            joinGameService.joinGame(authData1.authToken(),"BLACK", gameID);
        }catch (DataAccessException e){
            DataAccessException expected = new DataAccessException("Error already taken");
            assertEquals(expected.getClass(),e.getClass());
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }
}
