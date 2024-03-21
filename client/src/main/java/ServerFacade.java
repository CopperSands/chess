import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private String urlBase;
    private AuthData authData;

    public ServerFacade(int port){
        urlBase = "http://localhost:" + Integer.toString(port);
        authData = null;
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
