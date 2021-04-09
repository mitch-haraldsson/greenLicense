package de.shadowsoft.greenLicense.common.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESCrypt {

    private static final String ALGORITHM = "AES";

    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    private SecretKeySpec secretKey;

    public AESCrypt(SecretKey key) throws NoSuchAlgorithmException {
        byte[] tempkey = MessageDigest.getInstance("SHA-1").digest(key.getEncoded());
        secretKey = new SecretKeySpec(Arrays.copyOf(tempkey, 16), ALGORITHM);
    }

    public AESCrypt(String base64Key) throws Exception {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM);
    }

    public byte[] decrypt(byte[] strToDecrypt) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(strToDecrypt);
    }

    public String decrypt(String strToDecrypt) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return Base64.getEncoder().encodeToString(decrypt(strToDecrypt.getBytes()));
    }

    public String encrypt(String strToEncrypt) throws
            NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        return Base64.getEncoder().encodeToString(encrypt(strToEncrypt.getBytes()));
    }

    public byte[] encrypt(byte[] strToEncrypt) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(strToEncrypt);
    }
}
    
    