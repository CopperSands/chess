package service.helper;

import dataAccess.DataAccessException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashPassword {


    public HashPassword(){

    }

    /**
     * Hash a password in SHA-512
     * @param password
     * @return passwordHash
     * @throws DataAccessException
     */
    public static String hashPassword(String password) throws DataAccessException {
        //in future add salt
        String passwordHash = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            passwordHash = new String(hashBytes);
        }catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
        return passwordHash;
    }
}
