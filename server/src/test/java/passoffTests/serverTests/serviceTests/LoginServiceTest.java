package passoffTests.serverTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.helper.HashPassword;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private LoginService loginService;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    private static UserData user = new UserData("user","password", "john@email.com");
    private static UserData user1 = new UserData("user1","password", "other@mail.com");
    private static UserData user2 = new UserData("user2","password1", "jon@mail.com");

    private static AuthData authData = new AuthData("this is a random token","user");
    private static AuthData authData1 = new AuthData("this is a  different random token","user1");
    private static AuthData authData2 = new AuthData("this is more than random","user2");


    @Test
    public void validLogin(){
        try{
            loginService = new LoginService();
            authDAO = loginService.getAuthDAO();
            userDAO = loginService.getUserDAO();
            //populate tables
            userDAO.createUser(user.username(), HashPassword.hashPassword(user.password()),user.email());
            authDAO.createAuth(authData.authToken(),authData.username());

            //test login
            AuthData result = loginService.login(user.username(),user.password());
            assertNotNull(result.authToken());
            assertEquals(user.username(),result.username());
        }catch(Exception e){
            assertNull(e);
        }



    }

    @Test
    public void badPassword(){
        try{
            loginService = new LoginService();
            authDAO = loginService.getAuthDAO();
            userDAO = loginService.getUserDAO();
            //populate tables
            userDAO.createUser(user.username(), HashPassword.hashPassword(user.password()),user.email());
            authDAO.createAuth(authData.authToken(),authData.username());

            try{
                loginService.login(user.username(),"not the password");

            }catch(DataAccessException e){
                DataAccessException expected = new DataAccessException("Error Incorrect password");
                assertEquals(expected.getClass(),e.getClass());
                assertEquals(expected.getMessage(),e.getMessage());
            }

        }catch(Exception e){
            assertNull(e);
        }

    }

    @Test
    public void badUsername(){
        try{
            loginService = new LoginService();
            authDAO = loginService.getAuthDAO();
            userDAO = loginService.getUserDAO();
            //populate tables
            userDAO.createUser(user.username(), HashPassword.hashPassword(user.password()),user.email());
            authDAO.createAuth(authData.authToken(),authData.username());

            try{
                loginService.login(user1.username(),"not the password");

            }catch(DataAccessException e){
                DataAccessException expected = new DataAccessException("Error user does not exit");
                assertEquals(expected.getClass(),e.getClass());
                assertEquals(expected.getMessage(),e.getMessage());
            }

        }catch(Exception e){
            assertNull(e);
        }
    }


}
