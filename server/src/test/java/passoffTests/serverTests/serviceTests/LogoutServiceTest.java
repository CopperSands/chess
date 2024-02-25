package passoffTests.serverTests.serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Test;
import service.LogoutService;
import service.helper.AuthString;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {

    private LogoutService logoutService;
    private AuthDAO authDAO;
    private static String username = "user";
    private static String username1 = "user1";

    @Test
    public void validLogout(){
        logoutService = new LogoutService();
        authDAO = logoutService.getAuthDAO();
        try{
            //populate authDAO
            String authToken = AuthString.createAuthToken();
            authDAO.createAuth(authToken,username);
            AuthData authData = new AuthData(authToken,username);
            logoutService.logout(authData);
            //check that token was deleted
            AuthData result = authDAO.getAuth(authToken);
            assertNull(result);

        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void failLogout(){
        logoutService = new LogoutService();
        authDAO = logoutService.getAuthDAO();
        try{
            //populate authDAO
            String authToken = AuthString.createAuthToken();
            authDAO.createAuth(authToken,username);
            AuthData authData = new AuthData(authToken,username1);
            logoutService.logout(authData);


        }catch (DataAccessException e){
            Exception expected = new DataAccessException("error deleting token");
            assertEquals(expected.getClass(),e.getClass());
            assertEquals(expected.getMessage(),expected.getMessage());
        }
    }


}
