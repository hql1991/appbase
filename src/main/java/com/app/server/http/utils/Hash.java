package com.app.server.http.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Hash {
    public static void main(String argv[]) {
        try {
            String password = "admin";
            System.out.println("Raw password: " + password);
            String hash = PBKDF2Hash(password);
            System.out.println("PBDFK hash: " + hash);
            hash = MD5Hash(password);
            System.out.println("MD5 Hash: " + hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String MD5Hash(String password) {
        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        return md5Hex;
    }

    public static String PBKDF2Hash(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = generateSalt();
        return PBKDF2Hash(password, salt);
    }

    public static String PBKDF2Hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        String saltedHash = DatatypeConverter.printHexBinary(hash);
        return saltedHash;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }


}