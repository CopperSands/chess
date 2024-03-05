package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.PersistentAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersistentAuthDAOTests {

    private AuthDAO authDAO;
    private static AuthData token = new AuthData("this is a token", "hello");
    private static AuthData token1 = new AuthData("this is also a token", "user");

    @BeforeEach
    public void setup(){
        try{
            authDAO = new PersistentAuthDAO();
            authDAO.clear();
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void createNewToken(){
        try{
            authDAO.createAuth(token.authToken(),token.username());
        } catch (DataAccessException e) {
            assertNull(e);
        }
    }

    @Test
    public void failCreateNewToken(){
        try{
            authDAO.createAuth(token.authToken(), token.username());
            authDAO.createAuth(token.authToken(), token.username());
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error failed to create authToken");
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }

    @Test
    public void getAuthToken(){
        try{
            authDAO.createAuth(token.authToken(),token.username());
            AuthData found = authDAO.getAuth(token.authToken());
            assertEquals(token.authToken(),found.authToken());
            assertEquals(token.username(),found.username());
        } catch (DataAccessException e) {
            assertNull(e);
        }
    }

    @Test
    public void failGetAuthToken(){
        try{
            AuthData found = authDAO.getAuth(token.authToken());
            assertNull(found);
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void deleteAuthToken(){
        try{
            authDAO.createAuth(token.authToken(), token1.username());
            authDAO.deleteAuth(token);
            AuthData found = authDAO.getAuth(token.authToken());
            assertNull(found);
        } catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void failDeleteAuthToken(){
        try{
            authDAO.deleteAuth(token);
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void clearTest(){
        try{
            authDAO.createAuth(token.authToken(),token.username());
            authDAO.createAuth(token1.authToken(), token1.username());
            authDAO.clear();
            AuthData found = authDAO.getAuth(token.authToken());
            assertNull(found);
            found = authDAO.getAuth(token1.authToken());
            assertNull(found);
        }catch(DataAccessException e){
            assertNull(e);
        }
    }
}
