package utility;

import exceptions.AuthenticationException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encoder {
    public static String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AuthenticationException("Ошибка при хэшировании пароля.");
        }
    }
}
