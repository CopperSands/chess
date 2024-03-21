package clientTests;

import client.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String password = "This is a SuperSecure Password";
    private static LocalDateTime unique;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade(port);
        unique = LocalDateTime.now();
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        try{
            serverFacade.logout();
        }catch (Exception e){

        }

        server.stop();
    }


    @Test
    public void registerRequestTest() {
        try {
            serverFacade.registerRequest(unique.toString(),password,unique.toString() );
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void failRegisterRequest(){
        try {
            serverFacade.registerRequest(unique.toString(),password,unique.toString() );
            serverFacade.registerRequest(unique.toString(),password,unique.toString() +1 );
        } catch (Exception e) {
            assertEquals("Error username is taken", e.getMessage());
        }
    }

    @Test void loginTest(){
        try{
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            serverFacade.logout();
            serverFacade.login(unique.toString(),password);
        } catch (Exception e) {
            assertNull(e);
        }
    }
    @Test void failLogin(){
        try {
            serverFacade.login(unique.toString(),password);
        }catch (Exception e){
            assertEquals("Error user does not exit",e.getMessage());
        }
        try {
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            serverFacade.logout();
            serverFacade.login(unique.toString(),"not the password");
        }catch (Exception e){
            assertEquals("Error Incorrect password",e.getMessage());
        }
    }

    @Test void logoutTest(){
        try{
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            serverFacade.logout();
        }catch (Exception e){
            assertNull(e);
        }
    }

    @Test void failLogout(){
        try{
            serverFacade.logout();
        }catch (Exception e){
            assertEquals("Error unauthorized",e.getMessage());
        }
        try{
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            serverFacade.logout();
            serverFacade.logout();
        }catch (Exception e){
            assertEquals("Error unauthorized",e.getMessage());
        }
    }

    @Test void createGameTest(){
        try{
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            int gameID = serverFacade.createGame("testgame");
            assertTrue(gameID > 0);
        }catch (Exception e){
            assertNull(e);
        }
    }

    @Test void failCreateGame(){
        try{
            serverFacade.createGame("testFailgame");
        }catch (Exception e){
            assertEquals("Error unauthorized",e.getMessage());
        }
    }

    @Test void listGamesTest(){
        try{
            serverFacade.registerRequest(unique.toString(),password,unique.toString());
            int gameID = serverFacade.createGame("testgame");
            assertTrue(gameID > 0);
        }catch (Exception e){
            assertNull(e);
        }
    }

}
