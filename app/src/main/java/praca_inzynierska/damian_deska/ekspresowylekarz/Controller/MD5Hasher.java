package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Damian Deska on 2017-01-16.
 */

public class MD5Hasher {

    /*funkcja odpowiedzialna za zaszyfrowania hasla i stworzenie 128-bitowego skrotu*/
    public String hashToMD5(String password) {
        String hashText = null;
        try {
            MessageDigest message = MessageDigest.getInstance("MD5");
            message.reset();
            message.update(password.getBytes());
            byte[] digest = message.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashText = bigInt.toString(16);

            /*w przypadku zbyt krotkiego skrotu, uzupelniany jest on zerami wiodacymi*/
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (Exception e) {

        }
        return hashText;
    }
}
