package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.JoinGameService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NoticeMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.GenCommand;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

import java.io.IOException;
import java.util.Map;

//the annotation gives compiler hints that this is a web socket
@WebSocket
public class WebSocketHandler {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private WebSocketSessions webSessions;

    public WebSocketHandler(){
        authDAO = new PersistentAuthDAO();
        gameDAO = new PersistentGameDAO();
        webSessions = new WebSocketSessions();
    }

    @OnWebSocketConnect
    public void OnConnect(Session session) {
        System.out.print("connected to websocket");
    }

    @OnWebSocketMessage
    public void OnMessage(Session session, String message) {
        System.out.println(message);
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        GameService gameService = new GameService(authDAO,gameDAO);

        if (command.getCommandType() == CommandType.JOIN_PLAYER ||
                command.getCommandType() == CommandType.JOIN_OBSERVER){
            joinGame(session,message,gameService);
        }
        else if (command.getCommandType() == CommandType.MAKE_MOVE){
            makeMove(session,message,gameService);
        }
        else if (command.getCommandType() == CommandType.RESIGN){
            resign(session,message,gameService);
        }

    }

    private void resign(Session session, String message, GameService gameService) {
        Gson gson = new Gson();
        try{
            GenCommand command = gson.fromJson(message,GenCommand.class);
            gameService.resignGame(command.getAuthString(), command.getGameID());
            String username = gameService.getUsername(command.getAuthString());
            String result = username + " has resigned";
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,result);
            String res = gson.toJson(notice);
            session.getRemote().sendString(res);
            broadcastMessage(command.getGameID(),res,command.getAuthString());
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,e.getMessage());
            String error = gson.toJson(errorMessage);
            try {
                session.getRemote().sendString(error);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeMove(Session session, String message, GameService gameService){
        Gson gson = new Gson();
        try{
            MoveCommand moveCommand = gson.fromJson(message,MoveCommand.class);
            GameData game = gameService.makeMove(moveCommand.getAuthString(), moveCommand.getMove(), moveCommand.getGameID());
            String result = "Successful move";
            LoadMessage loadMessage = new LoadMessage(ServerMessage.ServerMessageType.LOAD_GAME,result,game.game());
            String res = gson.toJson(loadMessage);
            session.getRemote().sendString(res);
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,result);
            String data = gson.toJson(notice);
            broadcastMessage(game.gameID(),res, moveCommand.getAuthString());
            broadcastMessage(game.gameID(),data, moveCommand.getAuthString());


        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,e.getMessage());
            String error = gson.toJson(errorMessage);
            try {
                session.getRemote().sendString(error);
            } catch (IOException ex) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void joinGame(Session session, String message, GameService gameService){
        Gson gson = new Gson();
        try {
            JoinCommand joinCommand = gson.fromJson(message,JoinCommand.class);
            ChessGame game = getGame(joinCommand);
            String result = "Successful join";
            webSessions.addSession(joinCommand.getGameID(), joinCommand.getAuthString(),session);
            LoadMessage loadMessage = new LoadMessage(ServerMessage.ServerMessageType.LOAD_GAME,result,game);
            String res = gson.toJson(loadMessage);
            session.getRemote().sendString(res);
            String username = gameService.getUsername(joinCommand.getAuthString());
            String joinMessage = username + " has joined the game";
            //broadcast
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,joinMessage);
            String data = gson.toJson(notice);
            broadcastMessage(joinCommand.getGameID(),data,joinCommand.getAuthString());

        } catch (IOException e) {
            System.out.println("Error sending message");
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,e.getMessage());
            String error = gson.toJson(errorMessage);
            try {
                session.getRemote().sendString(error);
            } catch (IOException ex) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        }
    }

    private ChessGame getGame(JoinCommand joinCommand) throws DataAccessException{
        try {
            GameService gameService = new GameService(authDAO,gameDAO);
            ChessGame game = gameService.loadGame(joinCommand.getAuthString(),joinCommand.getPlayerColor(),joinCommand.getGameID());
            return game;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    private void broadcastMessage(int gameID, String message, String excludeAuthToken) throws IOException {
        Map<String,Session> gameSession = webSessions.getGameSession(gameID);
        for (String key: gameSession.keySet()){
            if(!key.equals(excludeAuthToken)){
                Session other = gameSession.get(key);
                if (other.isOpen()){
                    other.getRemote().sendString(message);
                }
            }
        }
    }
}
