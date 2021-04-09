package de.shadowsoft.greenLicense.common.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shredher on 23.05.2017.
 */
public class HashGenerator {
    private String toHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes)
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public String hash(String plain, String algorythm) throws NoSuchAlgorithmException {
        MessageDigest digest;
        digest = MessageDigest.getInstance(algorythm);
        byte[] hash = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
        return toHex(hash);
    }

    public String hash(String plain) throws NoSuchAlgorithmException {
        return hash(plain, "SHA-256");
    }
}
