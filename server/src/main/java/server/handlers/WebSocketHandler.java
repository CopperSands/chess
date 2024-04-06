package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import service.JoinGameService;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NoticeMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public WebSocketHandler(){
        authDAO = new PersistentAuthDAO();
        gameDAO = new PersistentGameDAO();
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
        System.out.println(command.getCommandType());
        System.out.println(command.getAuthString());
        if (command.getCommandType() == CommandType.JOIN_PLAYER ||
                command.getCommandType() == CommandType.JOIN_OBSERVER){
            System.out.println("wants to join game");
            try {
                ChessGame game = joinGame(message);
                String result = "Successful join";
                LoadMessage notice = new LoadMessage(ServerMessage.ServerMessageType.LOAD_GAME,result,game);

                System.out.println("in try");
                String res = gson.toJson(notice);
                session.getRemote().sendString(res);
            } catch (IOException e) {
                System.out.println("Error sending message");
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private ChessGame joinGame(String message) throws DataAccessException{
        Gson gson = new Gson();
        JoinCommand joinCommand = gson.fromJson(message,JoinCommand.class);
        try {
            JoinGameService joinGameService = new JoinGameService(authDAO,gameDAO);
            ChessGame game = joinGameService.loadGame(joinCommand.getAuthString(),joinCommand.getPlayerColor(),joinCommand.getGameID());
            System.out.println("Successful join");
            return game;
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
