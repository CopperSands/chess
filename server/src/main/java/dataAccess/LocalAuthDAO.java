package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class LocalAuthDAO implements AuthDAO{

    private HashMap <String, AuthData> authTokens;

    public LocalAuthDAO(){
        authTokens = new HashMap<String,AuthData>();
    }

    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {

        if(authTokens.containsKey(authToken)){
            throw new DataAccessException("failed to create authToken");
        }
        else{
            AuthData auth = new AuthData(authToken,username);
            try{
                authTokens.put(authToken,auth);
            }catch(Exception e){
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData result;

        try{
            result = authTokens.get(authToken);
        }catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }

        return result;

    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {
        try{
            if (!authTokens.remove(authToken.authToken(),authToken)){
                throw new DataAccessException("error deleting token");
            }
        }catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
