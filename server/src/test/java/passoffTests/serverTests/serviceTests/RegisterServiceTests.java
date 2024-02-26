package passoffTests.serverTests.serviceTests;

import dataAccess.*;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTests {

    private RegisterService registerService;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;

    private static UserData user = new UserData("user","password", "john@email.com");
    private static UserData user1 = new UserData("user1","password", "other@mail.com");
    private static UserData user2 = new UserData("user2","password1", "jon@mail.com");

    @Test
    @DisplayName("valid registration 1 user")
    public void validRegistration() {
        registerService = new RegisterService();
        userDAO = registerService.getUserDAO();
        authDAO = registerService.getAuthDAO();

        try {
            registerService.register(user.username(), user.password(),user.email());
            UserData returnedUser = userDAO.getUser(user.username());
            assertNotNull(returnedUser);
            assertEquals(user.email(),returnedUser.email());
        }catch(DataAccessException e){
            assertNull(e);
        }


    }

    @Test
    @DisplayName("valid multiple registrations")
    public void validMultiRegistration(){
        registerService = new RegisterService();
        try {
            registerService.register(user.username(), user.password(),user.email());
            registerService.register(user1.username(), user1.password(),user1.email());
            registerService.register(user2.username(), user2.password(),user2.email());
        }catch(DataAccessException e){
            assertNull(e);
        }
    }

    @Test
    @DisplayName("fail username already taken")
    public void failRegistrationUsernameTaken(){
        registerService = new RegisterService();
        try {
            registerService.register(user.username(), user.password(),user.email());
            registerService.register(user.username(), user.password(),user.email());
        }catch(DataAccessException e){
            DataAccessException expected = new DataAccessException("Error username is taken");
            assertEquals(expected.getClass(),e.getClass());
            assertEquals(expected.getMessage(),e.getMessage());
        }
    }

}
