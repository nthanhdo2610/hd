package com.tinhvan.hd.base;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AES256Provider {

    private static Cipher cipher;
    private static SecretKeySpec secretKey;
    private static IvParameterSpec ivSpec;

    /**
     * Init instances
     * @param salt
     */
    private static void init(String salt) {
        try {
            Config config = HDConfig.getInstance();
            String SECRET_KEY = config.get("FINGERPRINT_SECRET_KEY");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            ivSpec = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Encrypt String by AES256
     *
     * @param strToEncrypt string need to encrypt
     * @param salt
     * @return string encrypted
     */
    public static String encrypt(String strToEncrypt, String salt) {
        init(salt);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Decrypt String by AES256
     *
     * @param strToDecrypt string need to decrypt
     * @param salt
     * @return string decrypted
     */
    public static String decrypt(String strToDecrypt, String salt) {
        init(salt);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
