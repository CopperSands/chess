package server.handlers;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;


@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void OnConnect(Session session){
        System.out.print("connected to websocket");
    }

    @OnWebSocketMessage
    public void OnMessage(Session session, String message){
        System.out.println(message);
        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        System.out.println(command.getCommandType());
        System.out.println(command.getAuthString());
        switch (command.getCommandType()){
            case JOIN_PLAYER:
            {
            }
        }

    }
}
