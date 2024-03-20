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
    }

    /**
     * Sends a registration request to the server
     * @param username
     * @param password
     * @param email
     * @throws Exception
     */
    public void registerRequest(String username, String password, String email) throws Exception{
        Gson gson = new Gson();
        UserData userData = new UserData(username, password, email);
        URI uri = new URI(urlBase + "/user");
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod("POST");
        //this must be set to write data to request
        httpConn.setDoOutput(true);
        //this is how to write to the headers
        httpConn.addRequestProperty("Content-Type", "application/json");
        //write to body
        try(OutputStream outputStream = httpConn.getOutputStream()){
            String json = gson.toJson(userData);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get response
        int resCode = httpConn.getResponseCode();
        if (resCode == 403){
            throw new Exception("Error username is taken");
        }
        else if(resCode == 400){
            throw new Exception("Error bad request");
        }
        else if (resCode == 500){
            throw new Exception("Internal Server Error");
        }
        try(InputStream resBody = httpConn.getInputStream()){
            InputStreamReader inputStreamReader = new InputStreamReader(resBody);
            authData = gson.fromJson(inputStreamReader,AuthData.class);
        }
    }


}
