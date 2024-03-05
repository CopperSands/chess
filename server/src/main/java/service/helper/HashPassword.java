package service.helper;

import dataAccess.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            passwordHash = encoder.encode(password);
        }catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
        return passwordHash;
    }
}
