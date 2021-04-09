package de.shadowsoft.greenLicense.manager.tools;

import de.shadowsoft.greenLicense.manager.exceptions.BadKeyException;
import de.shadowsoft.greenLicense.manager.exceptions.EncryptionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESStringCrypt {

    private final static byte[] defaultKey = new byte[]{(byte) 0x0};

    private static byte[] crypt(byte[] data, byte[] key, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadKeyException {
        final Cipher cipher = Cipher.getInstance("AES");
        final SecretKeySpec secKey = new SecretKeySpec(padKey(key), "AES");
        cipher.init(cipherMode, secKey);
        try {
            return cipher.doFinal(data);
        } catch (BadPaddingException e) {
            throw new BadKeyException(e);
        }
    }

    public static byte[] decrypt(byte[] data, String key) throws EncryptionException, BadKeyException {
        return decrypt(data, key.getBytes());
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws EncryptionException, BadKeyException {
        try {
            return crypt(data, key, Cipher.DECRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException e) {
            throw new EncryptionException("Unable to encrypt data with AES", e);
        }
    }

    public static byte[] encrypt(byte[] data, String key) throws EncryptionException, BadKeyException {
        return encrypt(data, key.getBytes());
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws EncryptionException, BadKeyException {
        try {
            return crypt(data, key, Cipher.ENCRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException e) {
            throw new EncryptionException("Unable to decrypt data with AES", e);
        }
    }

    private static byte[] padKey(final byte[] key) {
        int padsize = 256 / 8;
        byte[] newKey = new byte[padsize];
        byte[] originalKey = key.length > 0 ? key : defaultKey;
        int lastIdx = 0;

        if (originalKey.length > padsize) {
            for (int i = 0; i < newKey.length; i++) {
                newKey[i] = originalKey[i];
            }
        } else {
            for (int i = 0; i < originalKey.length; i++) {
                newKey[i] = originalKey[i];
                lastIdx = i;
            }

            for (int i = lastIdx; i < newKey.length; i++) {
                newKey[i] = (byte) 0x0;
            }
        }
        return newKey;
    }
}
    
    