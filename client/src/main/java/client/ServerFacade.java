package client;

import clientRecords.*;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {

    private String urlBase;
    private AuthData authData;
    private ArrayList<GameData> gameList;

    public ServerFacade(int port){
        urlBase = "http://localhost:" + port;
        authData = new AuthData(null,null);
        gameList = null;
    }

    public void registerRequest(String username, String password, String email) throws Exception {
        UserData userData = new UserData(username, password, email);
        URI uri = new URI(urlBase + "/user");
        postRegLogin(uri,userData);
    }

    public void login(String username, String password) throws Exception{
        UserData userData = new UserData(username, password, null);
        URI uri = new URI(urlBase + "/session");
        postRegLogin(uri,userData);
    }

    public void logout() throws Exception{
        URI uri = new URI(urlBase +"/session");
        HttpURLConnection httpConn = ServerFacadeHelpers.getConnAndAuth(uri,"DELETE",authData.authToken());
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode != HttpURLConnection.HTTP_OK){
            ServerFacadeHelpers.getErrorMessage(httpConn);
        }
    }

    public int createGame(String gameName) throws Exception{
        int gameID = -1;
        Gson gson = new Gson();
        CreateGameJson createGame = new CreateGameJson(gameName);
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = ServerFacadeHelpers.getConnAndAuth(uri,"POST",authData.authToken());
        httpConn.setDoOutput(true);
        String json = gson.toJson(createGame);
        ServerFacadeHelpers.writeBody(httpConn,json);
        //get response
        int resCode = httpConn.getResponseCode();
        if(resCode == HttpURLConnection.HTTP_OK){
            try (InputStream resBody = httpConn.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                CreateGameRes res = gson.fromJson(inputStreamReader, CreateGameRes.class);
                gameID = res.gameID();
            }
        }
        else{
            ServerFacadeHelpers.getErrorMessage(httpConn);}
        return gameID;
    }

    public GameData joinGame(int id, String team) throws Exception{
        //check if game exits
        int i = id -1;
        GameData gameData = null;
        if(gameList == null){
            throw new Exception("Error call list before join");
        }
        else if ((i >= 0) && (i  < gameList.size())){
            //get the gameID and update the game
            int gameID = gameList.get(i).gameID();
            putGame(team,gameID);
            // call list to update
            listGames();
            // get updated game
            for (GameData game : gameList){
                if (game.gameID() == gameID){
                    gameData = game;
                    break;
                }
            }
        }
        else{throw new Exception("Error invalid game code");}
        return gameData;
    }

    public Collection<GameData> listGames() throws Exception{
        Gson gson = new Gson();
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = ServerFacadeHelpers.getConnAndAuth(uri,"GET",authData.authToken());
        //get response
        int resCode = httpConn.getResponseCode();
        if(resCode == HttpURLConnection.HTTP_OK){
            try (InputStream resBody = httpConn.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                GameList res = gson.fromJson(inputStreamReader, GameList.class);
                gameList = (ArrayList<GameData>) res.games();
            }
        }
        else{
            ServerFacadeHelpers.getErrorMessage(httpConn);}
        return gameList;
    }

    private void putGame(String team, int gameID) throws Exception{
        Gson gson = new Gson();
        JoinJson joinData = new JoinJson(team,gameID);
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = ServerFacadeHelpers.getConnAndAuth(uri,"PUT",authData.authToken());
        httpConn.setDoOutput(true);
        String json = gson.toJson(joinData);
        ServerFacadeHelpers.writeBody(httpConn,json);
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode != HttpURLConnection.HTTP_OK){
            ServerFacadeHelpers.getErrorMessage(httpConn);
        }
    }

    private void postRegLogin(URI uri, UserData userData) throws Exception {
        Gson gson = new Gson();
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("POST");
        //this must be set to write data to request
        httpConn.setDoOutput(true);
        //this is how to write to the headers
        httpConn.addRequestProperty("Content-Type", "application/json");
        //write to body
        String json = gson.toJson(userData);
        ServerFacadeHelpers.writeBody(httpConn,json);
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = httpConn.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                authData = gson.fromJson(inputStreamReader, AuthData.class);
            }
        } else {
            ServerFacadeHelpers.getErrorMessage(httpConn);}
    }
}
