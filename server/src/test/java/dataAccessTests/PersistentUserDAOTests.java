package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.PersistentUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentUserDAOTests {

    private UserDAO userDAO;

    private static UserData userData = new UserData("Johnny","fadfadsfadsfasdfaef","johnny@mail.com");
    private static UserData userData1 = new UserData("Jim","0102930201029", "jimmey@mail.com");

    @BeforeEach
    public void setup(){
        userDAO = new PersistentUserDAO();
        try{
            userDAO.clear();
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @AfterEach
    public void cleanup(){
        try{
            userDAO.clear();
        }catch (DataAccessException e){
            assertNull(e);
        }
    }



    @Test
    public void validCreateUser(){
        try{
            userDAO.createUser(userData.username(),userData.password(),userData.email());
            userDAO.createUser(userData1.username(),userData1.password(),userData1.email());
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void badCreateUser(){
        try{
            userDAO.createUser(userData.username(),userData.password(),userData.email());
            userDAO.createUser(userData.username(),userData1.password(),userData1.email());
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error creating user");
            assertEquals(expected.getMessage(),e.getMessage());
        }
        try{
            userDAO.createUser(userData1.username(),userData1.password(),userData.email());

        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error creating user");
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }

    @Test
    public void validGetUser(){
        try{
            userDAO.createUser(userData.username(),userData.password(),userData.email());
            UserData found = userDAO.getUser(userData.username());
            assertEquals(userData.username(),found.username());
            assertEquals(userData.password(),found.password());
            assertEquals(userData.email(),found.email());
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void badGetUser(){
        try{
            UserData found = userDAO.getUser(userData.username());
            assertNull(found);
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    public void clearTest(){
        try{
            userDAO.createUser(userData.username(),userData.password(),userData.email());
            userDAO.createUser(userData1.username(),userData1.password(),userData1.email());
            userDAO.clear();
            UserData found = userDAO.getUser(userData.username());
            UserData found1 = userDAO.getUser(userData1.username());
            assertNull(found);
            assertNull(found1);
        }catch (DataAccessException e){
            assertNull(e);
        }
    }

}
