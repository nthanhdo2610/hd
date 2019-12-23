package com.tinhvan.hd.base;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESProvider {

    private static SecretKeySpec secretKey;
    private static Cipher cipher;
    private static byte[] key;

    private static void setKey()
    {
        try {
            Config config = HDConfig.getInstance();
            String SECRET_KEY = config.get("FINGERPRINT_SECRET_KEY");
            key = SECRET_KEY.getBytes("UTF-8");
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public static String encrypt(String strToEncrypt)
    {
        setKey();
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            return encrypted;


        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public static String decrypt(String strToDecrypt)
    {
        setKey();
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return decrypted;


        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
