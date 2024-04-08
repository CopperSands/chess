package client;

import clientRecords.ErrorRes;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ServerFacadeHelpers {

    public static void getErrorMessage(HttpURLConnection httpConn) throws Exception{
        Gson gson = new Gson();
        try (InputStream resBody = httpConn.getErrorStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(resBody);
            ErrorRes errorRes = gson.fromJson(inputStreamReader, ErrorRes.class);
            throw new Exception(errorRes.message());
        }
    }
    public static void writeBody(HttpURLConnection httpConn,  String json){
        try (OutputStream outputStream = httpConn.getOutputStream()) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {e.printStackTrace();}
    }
    public static HttpURLConnection getConnAndAuth(URI uri, String httpVerb, String authToken) throws Exception{
        HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
        httpConn.setRequestMethod(httpVerb);
        httpConn.addRequestProperty("authorization",authToken);
        return httpConn;
    }
}
