package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NoticeMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.GenCommand;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.xml.crypto.Data;
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
    public void onConnect(Session session) {
        System.out.print("connected to websocket");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        GameService gameService = new GameService(authDAO,gameDAO);
        switch (command.getCommandType()){
            case JOIN_PLAYER,JOIN_OBSERVER -> joinGame(session,message,gameService);
            case MAKE_MOVE -> makeMove(session,message,gameService);
            case RESIGN -> resign(session,message,gameService);
            case LEAVE -> leaveGame(session,message,gameService);
        }
    }

    @OnWebSocketError
    public void onError(Throwable error){
        System.out.println("There was a websocket error");
        System.out.println(error.getMessage());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason){
        webSessions.removeSession(session);
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
            sendError(gson,session,e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeMove(Session session, String message, GameService gameService){
        Gson gson = new Gson();
        try{
            MoveCommand moveCommand = gson.fromJson(message,MoveCommand.class);
            GameData game = gameService.makeMove(moveCommand.getAuthString(), moveCommand.getMove(), moveCommand.getGameID());
            String result = "Successful move " + moveCommand.getStartEnd();
            LoadMessage loadMessage = new LoadMessage(ServerMessage.ServerMessageType.LOAD_GAME,result,game.game());
            String res = gson.toJson(loadMessage);
            session.getRemote().sendString(res);
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,result);
            String data = gson.toJson(notice);
            broadcastMessage(game.gameID(),res, moveCommand.getAuthString());
            broadcastMessage(game.gameID(),data, moveCommand.getAuthString());
            alertGameChange(game);



        } catch (DataAccessException e) {
            sendError(gson,session,e);
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
            if (joinCommand.getPlayerColor() != null){
                joinMessage = joinMessage + " as " + joinCommand.getPlayerColor();
            }
            //broadcast
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,joinMessage);
            String data = gson.toJson(notice);
            broadcastMessage(joinCommand.getGameID(),data,joinCommand.getAuthString());
        } catch (IOException e) {
            System.out.println("Error sending message");
        } catch (DataAccessException e) {
            sendError(gson,session,e);
        }
    }

    private void leaveGame(Session session, String message, GameService gameService) {
        Gson gson = new Gson();
        GenCommand command = gson.fromJson(message,GenCommand.class);
        try {
        if (!webSessions.isSessionInGame(command.getGameID(), command.getAuthString())){
            throw new DataAccessException("you are not part of this game");
        }
        webSessions.removeSessionInGame(command.getGameID(), command.getAuthString(),session);
        String username = gameService.getUsername(command.getAuthString());
        String leaveMessage = username + " has left the game";
        NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,leaveMessage);
        String res = gson.toJson(notice);
        session.getRemote().sendString(res);
        broadcastMessage(command.getGameID(), res, command.getAuthString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            sendError(gson,session,e);
        }
        session.close();

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

    private void alertGameChange(GameData game) throws IOException {
        String result = null;
        if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
            result = game.blackUsername() + " has won! " + game.whiteUsername() + " is in checkmate";
        }
        else if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
            result = game.whiteUsername() + " has won! " + game.blackUsername() + " is in checkmate";
        }
        else if (game.game().isInCheck(ChessGame.TeamColor.WHITE)){
            result = game.whiteUsername() + " is in check";
        }
        else if (game.game().isInCheck(ChessGame.TeamColor.BLACK)){
            result = game.blackUsername() + " is in check";
        }
        if (result != null){
            NoticeMessage notice = new NoticeMessage(ServerMessage.ServerMessageType.NOTIFICATION,result);
            Gson gson = new Gson();
            String res = gson.toJson(notice);
            broadcastMessage(game.gameID(),res,null);
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

    private void sendError(Gson gson, Session session, Exception e){
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,e.getMessage());
        String error = gson.toJson(errorMessage);
        try {
            session.getRemote().sendString(error);
        } catch (IOException ex) {
            System.out.println(e.getMessage());
        }
    }
}
