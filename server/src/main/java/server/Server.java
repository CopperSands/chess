package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import server.handlers.AuthHandler;
import server.handlers.GameHandler;
import server.handlers.WebSocketHandler;
import service.*;
import spark.*;

import java.util.Collection;


public class Server {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private ClearAppService clearAppService;
    private AuthHandler authHandler;
    private GameHandler gameHandler;

    //edit constructor after database up
    public Server(){
        authDAO = new PersistentAuthDAO();
        userDAO = new PersistentUserDAO();
        gameDAO = new PersistentGameDAO();
        clearAppService = new ClearAppService(authDAO,userDAO,gameDAO);
        authHandler = new AuthHandler();
        gameHandler = new GameHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.webSocket("/connect", WebSocketHandler.class);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::registrationHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game",this::listGamesHandler);
        Spark.post("/game",this::createGameHandler);
        Spark.put("/game",this::joinGameHandler);
        Spark.delete("/db",this::clearHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registrationHandler(Request req, Response res){
        return authHandler.registrationHandler(req,res);
    }

    private Object loginHandler(Request req, Response res){
        return authHandler.loginHandler(req,res);
    }

    private Object logoutHandler(Request req, Response res){
        return authHandler.logoutHandler(req,res);
    }

    private Object listGamesHandler(Request req, Response res){
        return gameHandler.listGamesHandler(req,res);
    }

    private Object createGameHandler(Request req, Response res){
        return gameHandler.createGameHandler(req,res);
    }

    public Object joinGameHandler(Request req, Response res){
        return gameHandler.joinGameHandler(req,res);
    }

    private Object clearHandler(Request req, Response res){
        try{
            clearAppService.clear();
            return "";
        }catch (DataAccessException e){
            res.status(500);
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return new Gson().toJson(message);
        }
    }

}
