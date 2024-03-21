import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {

    private String urlBase;
    private AuthData authData;
    private ArrayList<GameData> gameList;

    public ServerFacade(int port){
        urlBase = "http://localhost:" + Integer.toString(port);
        authData = null;
        gameList = null;
    }

    /**
     * Sends a registration request to the server
     * @param username
     * @param password
     * @param email
     * @throws Exception
     */
    public void registerRequest(String username, String password, String email) throws Exception {
        UserData userData = new UserData(username, password, email);
        URI uri = new URI(urlBase + "/user");
        postRegLogin(uri,userData);
    }

    /**
     * Sends a login request to the server
     * @param username
     * @param password
     * @throws Exception
     */
    public void login(String username, String password) throws Exception{
        UserData userData = new UserData(username, password, null);
        URI uri = new URI(urlBase + "/session");
        postRegLogin(uri,userData);
    }

    public void logout() throws Exception{
        URI uri = new URI(urlBase +"/session");
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("DELETE");
        //set authToken
        httpConn.addRequestProperty("authorization",authData.authToken());
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode != HttpURLConnection.HTTP_OK){
            getErrorMessage(httpConn);
        }
    }

    public int createGame(String gameName) throws Exception{
        int gameID = -1;
        Gson gson = new Gson();
        CreateGameJson createGame = new CreateGameJson(gameName);
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.addRequestProperty("authorization",authData.authToken());
        httpConn.setDoOutput(true);
        try (OutputStream outputStream = httpConn.getOutputStream()) {
            String json = gson.toJson(createGame);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            getErrorMessage(httpConn);
        }
        return gameID;
    }

    public GameData joinGame(int id, String team) throws Exception{
        //check if game exits
        int i = id -1;
        GameData gameData = null;
        if(gameList == null){
            throw new Exception("Error call list");
        }
        else if ((i >= 0) && (i  <= gameList.size())){
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
        else{
            throw new Exception("Error invalid game code");
        }
        return gameData;
    }

    public Collection<GameData> listGames() throws Exception{
        Gson gson = new Gson();
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.addRequestProperty("authorization",authData.authToken());
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
            getErrorMessage(httpConn);
        }
        return gameList;
    }

    private void putGame(String team, int gameID) throws Exception{
        Gson gson = new Gson();
        JoinJson joinData = new JoinJson(team,gameID);
        URI uri = new URI(urlBase +"/game");
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("PUT");
        httpConn.addRequestProperty("authorization",authData.authToken());
        httpConn.setDoOutput(true);
        try (OutputStream outputStream = httpConn.getOutputStream()) {
            String json = gson.toJson(joinData);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode != HttpURLConnection.HTTP_OK){
            getErrorMessage(httpConn);
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
        try (OutputStream outputStream = httpConn.getOutputStream()) {
            String json = gson.toJson(userData);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = httpConn.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(resBody);
                authData = gson.fromJson(inputStreamReader, AuthData.class);
            }
        } else {
            getErrorMessage(httpConn);
        }
    }
    private void getErrorMessage(HttpURLConnection httpConn) throws Exception{
        Gson gson = new Gson();
        try (InputStream resBody = httpConn.getErrorStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(resBody);
            ErrorRes errorRes = gson.fromJson(inputStreamReader, ErrorRes.class);
            throw new Exception(errorRes.message());
        }
    }

}
