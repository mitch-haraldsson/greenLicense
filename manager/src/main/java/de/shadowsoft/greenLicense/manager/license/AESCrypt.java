package de.shadowsoft.greenLicense.manager.license;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESCrypt {

    private final byte[] key;
    private SecretKeySpec secretKey;

    public AESCrypt(SecretKey key) throws NoSuchAlgorithmException {
        byte[] tempkey = key.getEncoded();
        tempkey = MessageDigest.getInstance("SHA-1").digest(tempkey);
        this.key = Arrays.copyOf(tempkey, 16);
        secretKey = new SecretKeySpec(this.key, "AES");
    }

    public byte[] decrypt(byte[] strToDecrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(strToDecrypt);
    }

    public byte[] encrypt(byte[] strToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(strToEncrypt);
    }
}
    
    