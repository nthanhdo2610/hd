package com.tinhvan.hd.base;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AESProvider {

    private static Cipher cipher;
    private static SecretKeySpec secretKey;
    private static IvParameterSpec ivSpec;

    /**
     * Init instances
     */
    private static void init(String salt, String iv) {
        try {
            Config config = HDConfig.getInstance();
            String SECRET_KEY = config.get("AES_SECRET_KEY");
            //String SECRET_KEY = "vKPSEzjZXlwwP05i";
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), hexStringToByteArray(salt), 1024, 256);
            SecretKey tmp = factory.generateSecret(spec);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            ivSpec = new IvParameterSpec(hexStringToByteArray(iv));
        } catch (Exception e) {
            //throw new InternalServerErrorException(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Encrypt String by AES256
     *
     * @param strToEncrypt string need to encrypt
     * @return string encrypted
     */
    public static String encrypt(String strToEncrypt, String salt, String iv) {
        init(salt, iv);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            //throw new InternalServerErrorException(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt String by AES256
     *
     * @param strToDecrypt string need to decrypt
     * @return string decrypted
     */
    public static String decrypt(String strToDecrypt) {
        System.out.println("strToDecrypt: "+strToDecrypt);
        String salt = "", iv = "", password = "";
        String[] encrypted = strToDecrypt.split("");
        for (int i = 0; i < encrypted.length; i++) {
            if (i < 32)
                salt += encrypted[i];
            else if (i >= 32 && i < 64)
                iv += encrypted[i];
            else
                password += encrypted[i];
        }
        System.out.println("salt: " + salt);
        System.out.println("iv: " + iv);
        System.out.println("encrypted: " + password);
        init(salt, iv);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
        } catch (Exception e) {
            //throw new InternalServerErrorException(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        /*String encryptedString = "4b18d54199b20a9459eddad055fc0f66835155df9b33df8d20b8b19abf781d9btcuGLdLUW3i8QoKcqWvTkA==";
        String decryptedString = decrypt(encryptedString);
        System.out.println("password: " + decryptedString);*/
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
