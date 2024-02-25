package service.helper;

import java.time.LocalDateTime;
import java.util.Random;

public class AuthString {

    /**
     * Creates a unique token string
     * @return
     */
    public static String createAuthToken(){
        //use
        StringBuilder token = new StringBuilder();

        LocalDateTime time = LocalDateTime.now();

        //add 7 random chars to the string
        Random random = new Random();
        for (int i = 0; i < 8; i++){
            // num is an ascii char in decimal starting at 0 ending at z
            int num = random.nextInt(74) + 48;
            //choose a new char if \ or ; or `
            if (num == 92 || num == 59 || num == 96){
                i--;
            }
            else{
                token.append(num);
            }
        }
        token.append(time);
        //for future hash the string before returning for more security

        return token.toString();
    }
}
