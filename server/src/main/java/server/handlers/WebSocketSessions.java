package server.handlers;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    //concurrentHashMaps are thread safe with a default size of 16
    static private Map<Integer, Map<String, Session>> sessionMap = new ConcurrentHashMap<>() ;

    protected void addSession(int gameID, String authToken, Session session){
        //check if sessions for game exist
        Map<String, Session> gameSession = getGameSession(gameID);
        if (gameSession == null){
            gameSession = new HashMap<>();
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
    protected boolean isSessionInGame(int gameID, String authToken){
        Map<String,Session> result = getGameSession(gameID);
        if (result.get(authToken) != null){
            return true;
        }
        return false;
    }
    //there should only ever be one game per session
    protected void removeSession(Session session){
        for (int key: sessionMap.keySet()){
            Map<String,Session> result = sessionMap.get(key);
            if (result.containsValue(session)){
                for (String key2: result.keySet()){
                    if (result.get(key2) == session){
                        result.remove(key2);
                        sessionMap.replace(key,result);
                        break;
                    }
                }
                break;
            }
        }
    }

}
