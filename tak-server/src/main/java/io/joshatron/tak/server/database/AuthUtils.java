package io.joshatron.tak.server.database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthUtils {

    public static String encryptPassword(String pass, String salt) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return salt + encoder.encode(salt + pass);
    }

    public static String getSalt(String encryptedPass) {
        if(encryptedPass.length() < 24) {
            return null;
        }
        return encryptedPass.substring(0, 24);
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[18];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
