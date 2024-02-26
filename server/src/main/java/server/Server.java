package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;
import spark.*;

import java.util.Collection;

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
        Gson gson = new Gson();
        UserData user = gson.fromJson(req.body(),UserData.class);
        try{
            AuthData authToken = registerService.register(user.username(),user.password(),user.email());
            return gson.toJson(authToken);
        }catch(DataAccessException e){
            if (e.getMessage() == "Error username is taken"){
                res.status(403);
            }else if (e.getMessage() == "Error bad request"){
                res.status(400);
            }
            else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    private Object loginHandler(Request req, Response res){
        Gson gson = new Gson();
        UserData user = gson.fromJson(req.body(),UserData.class);
        try{
            AuthData authToken = loginService.login(user.username(),user.password());
            return gson.toJson(authToken);
        }catch (DataAccessException e){
            if (e.getMessage() == "Error Incorrect password" || e.getMessage() == "Error user does not exit"){
                res.status(401);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    private Object logoutHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            logoutService.logout(authToken);
            return "";
        }catch (DataAccessException e){
            if (e.getMessage() == "Error unauthorized" || false){
                res.status(401);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    private Object listGamesHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            Collection <GameData> games = listGamesService.listGames(authToken);
            GameListRes gameRes = new GameListRes(games);
            return gson.toJson(gameRes);

        }catch (DataAccessException e){
            if (e.getMessage() == "Error unauthorized"){
                res.status(401);
            }else {
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    private Object createGameHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        CreateGameReq gameReq = gson.fromJson(req.body(), CreateGameReq.class);
        AuthData authData = new AuthData(authToken,null);
        try{
            int gameID = createGameService.createGame(authData,gameReq.gameName());
            CreateGameRes returnObj = new CreateGameRes(gameID);
            return gson.toJson(returnObj);
        }catch(DataAccessException e){
            if (e.getMessage() == "Error unauthorized"){
                res.status(401);
            }else if(e.getMessage() == "Error bad request"){
                res.status(400);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
    }

    private Object joinGameHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        JoinReq joinReq = gson.fromJson(req.body(),JoinReq.class);
        try{
            joinGameService.joinGame(authToken,joinReq.playerColor(),joinReq.gameID());
            return "";
        }catch (DataAccessException e){
            if (e.getMessage() == "Error unauthorized"){
                res.status(401);
            }else if(e.getMessage() == "Error bad request"){
                res.status(400);
            }else if(e.getMessage() == "Error already taken"){
                res.status(403);
            }else{
                res.status(500);
            }
            ErrorMessageRes message = new ErrorMessageRes(e.getMessage());
            return gson.toJson(message);
        }
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
