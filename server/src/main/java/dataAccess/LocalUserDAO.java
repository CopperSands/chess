package dataAccess;

import model.UserData;

import java.util.HashMap;

public class LocalUserDAO implements UserDAO{

    private HashMap<String,UserData> users;

    public LocalUserDAO(){
        users = new HashMap<String,UserData>();
    }

    @Override
    public void createUser(String username, String passwordHash, String email) throws DataAccessException {

        //If the user is already created throw an error
        if (users.containsKey(username)){
            throw new DataAccessException("user already exits");
        }
        else{
            UserData user = new UserData(username,passwordHash,email);
            try{
                users.put(username,user);
            }catch(Exception e){
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{

        try{
            UserData userInfo = users.get(username);
            return userInfo;
        }catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try{
            users.clear();
        }catch(Exception e){
            throw new DataAccessException("Error clearing users " + e.getMessage());
        }

    }
}
