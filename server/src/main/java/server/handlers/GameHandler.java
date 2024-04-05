package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import server.*;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ListGamesService listGamesService;
    private JoinGameService joinGameService;
    private CreateGameService createGameService;

    public GameHandler(){
        authDAO = new PersistentAuthDAO();
        gameDAO = new PersistentGameDAO();
        listGamesService = new ListGamesService(authDAO,gameDAO);
        joinGameService = new JoinGameService(authDAO,gameDAO);
        createGameService = new CreateGameService(authDAO,gameDAO);
    }

    public Object listGamesHandler(Request req, Response res){
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try{
            Collection<GameData> games = listGamesService.listGames(authToken);
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

    public Object createGameHandler(Request req, Response res){
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

    public Object joinGameHandler(Request req, Response res){
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

}
