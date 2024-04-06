package server.handlers;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    //concurrentHashMaps are thread safe
    static private Map<Integer, Map<String, Session>> sessionMap = new ConcurrentHashMap<>() ;

    protected void addSession(int gameID, String authToken, Session session){
        //check if sessions for game exist
        Map<String, Session> gameSession = getGameSession(gameID);
        if (gameSession == null){
            gameSession = new ConcurrentHashMap<>();
        }
        gameSession.put(authToken,session);
        sessionMap.put(Integer.valueOf(gameID), gameSession);
    }


    protected Map<String,Session> getGameSession(int gameID){
        Map<String,Session> result = sessionMap.get(Integer.valueOf(gameID));
        return result;
    }

    protected void removeSessionInGame (int gameID, String authToken,Session session){
        Map<String, Session> gameSession = getGameSession(gameID);
        if(gameSession != null){
            gameSession.remove(authToken);
            sessionMap.put(Integer.valueOf(gameID), gameSession);
        }
    }

    //used to close the session call after removeSessionIn Game maybe
    protected void removeSession(Session session){

    }

}
