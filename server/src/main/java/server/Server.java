package server;

import dataAccess.*;
import service.*;
import spark.*;

public class Server {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private RegisterService registerService;
    private LogoutService logoutService;
    private LoginService loginService;
    private ListGamesService listGamesService;
    private JoinGameService joinGameService;
    private CreateGameService createGameService;
    private ClearAppService clearAppService;

    //edit constructor after database up
    public Server(){
        authDAO = new LocalAuthDAO();
        userDAO = new LocalUserDAO();
        gameDAO = new LocalGameDAO();
        registerService = new RegisterService(userDAO,authDAO);
        logoutService = new LogoutService(authDAO);
        loginService = new LoginService(userDAO,authDAO);
        listGamesService = new ListGamesService(authDAO,gameDAO);
        joinGameService = new JoinGameService(authDAO,gameDAO);
        createGameService = new CreateGameService(authDAO,gameDAO);
        clearAppService = new ClearAppService(authDAO,userDAO,gameDAO);

    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::registrationHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/db",this::clearHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registrationHandler(Request req, Response res){
        return "hello";
    }

    private Object loginHandler(Request req, Response res){
        return "hello";
    }

    private Object logoutHandler(Request req, Response res){
        return "hello";
    }

    private Object listGamesHandler(Request req, Response res){
        return "hello";
    }

    private Object createGameHandler(Request req, Response res){
        return "hello";
    }

    private Object joinGameHandler(Request req, Response res){
        return "hello";
    }

    private Object clearHandler(Request req, Response res){
        try{
            clearAppService.clear();
        }catch (DataAccessException e){
            res.status(500);
        }
        return "";
    }
}
