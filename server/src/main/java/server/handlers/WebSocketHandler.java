package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
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
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

import java.io.IOException;
import java.util.Map;


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

        System.out.println(command.getCommandType());
        System.out.println(command.getAuthString());

        if (command.getCommandType() == CommandType.JOIN_PLAYER ||
                command.getCommandType() == CommandType.JOIN_OBSERVER){
            System.out.println("wants to join game");
            joinGame(session,message,gameService);
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
            System.out.println("Successful join");
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
                other.getRemote().sendString(message);
            }
        }
    }
}
