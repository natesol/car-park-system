package net.cps.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;


/*
 *
 */
public abstract class AbstractUser {
    static final private String HASH_ALGORITHM = "SHA-512";
    static final private Integer HASH_KEY_BASE = 16;
    
    Boolean isActive = false;
    String email = null;
    String passwordHash = null;
    String passwordSalt = null;
    
    public abstract String getPasswordHash ();
    public abstract String getPasswordSalt ();
    
    protected String generateSalt () {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[HASH_KEY_BASE];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    protected String hashPassword (String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, HASH_KEY_BASE).substring(1));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean isPasswordEquals (String password) {
        return Objects.equals(hashPassword(password, this.getPasswordSalt()), this.getPasswordHash());
    }
}
