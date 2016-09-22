package demon.XFC.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

public class SSHA {

    private static final String SSHA = "{SSHA}";

    private static final Random RANDOM = new SecureRandom();

    private static final int DEFAULT_SALT_SIZE = 10;

//    public static void main(final String[] args) throws Exception {
//
//        // User wants to hash the password
//        final String password = "123456";
//        System.out.println(Test.getSaltedPassword(password.getBytes("UTF-8")));
//
//        // User wants to verify an existing password
//
//        final String digest = "{SSHA}8opuBrakKHuO1pXWShJavXc8ESgxMjM0NTY3ODkw";
//        System.out.println(Test.verifySaltedPassword(password.getBytes("UTF-8"), digest));
//
//    }

    public static String getSaltedPassword(String password) throws NoSuchAlgorithmException {
        if (null == password) {
            throw new IllegalArgumentException();
        }
        
        byte[] salt = new byte[DEFAULT_SALT_SIZE];
        RANDOM.nextBytes(salt);

        return getSaltedPassword(password.getBytes(), salt);
    }

    protected static String getSaltedPassword(byte[] password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(password);
        byte[] hash = digest.digest(salt);

        // Create an array with the hash plus the salt
        byte[] all = new byte[hash.length + salt.length];
        for (int i = 0; i < hash.length; i++) {
            all[i] = hash[i];
        }
        for (int i = 0; i < salt.length; i++) {
            all[hash.length + i] = salt[i];
        }
        byte[] base64 = Base64.encodeBase64(all);
        return SSHA + new String(base64);
    }

    public static boolean verifySaltedPassword(byte[] password, String entry) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        // First, extract everything after {SSHA} and decode from Base64
        if (!entry.startsWith(SSHA)) {
            throw new IllegalArgumentException("Hash not prefixed by {SSHA}; is it really a salted hash?");
        }
        byte[] challenge = Base64.decodeBase64(entry.substring(6).getBytes("UTF-8"));

        // Extract the password hash and salt
        byte[] passwordHash = extractPasswordHash(challenge);
        byte[] salt = extractSalt(challenge);

        // Re-create the hash using the password and the extracted salt
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(password);
        byte[] hash = digest.digest(salt);

        // See if our extracted hash matches what we just re-created
        return Arrays.equals(passwordHash, hash);
    }

    protected static byte[] extractPasswordHash(byte[] digest) throws IllegalArgumentException {
        if (digest.length < 20) {
            throw new IllegalArgumentException("Hash was less than 20 characters; could not extract password hash!");
        }

        // Extract the password hash
        byte[] hash = new byte[20];
        for (int i = 0; i < 20; i++) {
            hash[i] = digest[i];
        }

        return hash;
    }

    protected static byte[] extractSalt(byte[] digest) throws IllegalArgumentException {
        if (digest.length <= 20) {
            throw new IllegalArgumentException("Hash was less than 21 characters; we found no salt!");
        }

        // Extract the salt
        byte[] salt = new byte[digest.length - 20];
        for (int i = 20; i < digest.length; i++) {
            salt[i - 20] = digest[i];
        }

        return salt;
    }
    
    
    
}
