package passoffTests.serverTests.serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearAppService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearAppServiceTest {

    private ClearAppService clearAppService;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    private static UserData user = new UserData("user","yep","example@mail.com");
    private static UserData user1 = new UserData("john","ok","john@mail.com");
    private static UserData user2 = new UserData("bob","password","bob@mail.com");

    private static AuthData authToken = new AuthData("this is a token", "user");
    private static AuthData authToken1 = new AuthData("this is another token", "john");
    private static AuthData authToken2 = new AuthData("I am a token", "bob");

    private static GameData game = new GameData(1,"john","bob","game1232",new ChessGame());
    private static GameData game1 = new GameData(2,"jim","bob","game1245",new ChessGame());
    private static GameData game2 = new GameData(3,"bob","alice","game1267",new ChessGame());

    @Test
    public void validClearing(){
        authDAO = new LocalAuthDAO();
        userDAO = new LocalUserDAO();
        gameDAO = new LocalGameDAO();
        clearAppService = new ClearAppService(authDAO,userDAO,gameDAO);

        try{
            //populate the databases
            userDAO.createUser(user.username(),user.password(),user.email());
            userDAO.createUser(user1.username(),user1.password(),user1.email());
            userDAO.createUser(user2.username(),user2.password(),user2.email());
            authDAO.createAuth(authToken.authToken(),authToken.username());
            authDAO.createAuth(authToken1.authToken(),authToken1.username());
            authDAO.createAuth(authToken2.authToken(),authToken2.username());
            gameDAO.createGame(game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
            gameDAO.createGame(game1.whiteUsername(),game1.blackUsername(),game1.gameName(),game1.game());
            gameDAO.createGame(game2.whiteUsername(),game2.blackUsername(),game2.gameName(),game2.game());

            clearAppService.clear();
            Collection<GameData> returnedGames = gameDAO.listGames();
            assertTrue(returnedGames.isEmpty());
            AuthData returnedToken = authDAO.getAuth(authToken.authToken());
            assertNull(returnedToken);
            returnedToken = authDAO.getAuth(authToken1.authToken());
            assertNull(returnedToken);
            returnedToken = authDAO.getAuth(authToken2.authToken());
            assertNull(returnedToken);
            UserData returnedUser = userDAO.getUser(user.username());
            assertNull(returnedUser);
            returnedUser = userDAO.getUser(user1.username());
            assertNull(returnedUser);
            returnedUser = userDAO.getUser(user2.username());
            assertNull(returnedUser);

        }catch (DataAccessException e){
            assertNull(e);
        }


    }

}
