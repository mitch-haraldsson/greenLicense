package de.shadowsoft.greenLicense.common.tool;

import java.util.Base64;

/**
 * Created by shredher on 07.02.2017.
 */
public class StringOps {

    public static String cutLastChar(String s) {
        if (s.length() > 0) {
            return s.substring(0, s.length() - 1);
        }
        return "";
    }

    public static String lastChar(String s) {
        if (s.length() > 0) {
            return s.substring(s.length() - 1);
        }
        return "";
    }

    public static String toHex(int i) {
        return String.format("0x%02X", i).toUpperCase();
    }

    public static String toHex(long l) {
        return "0x" + Long.toHexString(l).toUpperCase();
    }

    public String decodeBase64(String s) {
        return decodeBase64(s.getBytes());
    }

    public String decodeBase64(byte[] bytes) {
        return new String(Base64.getDecoder().decode(bytes));
    }

    public byte[] decodeBase64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public String encodeBase64(String s) {
        return encodeBase64(s.getBytes());
    }

    public String encodeBase64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    public String padLeft(String s, int padding) {
        return String.format("%1$" + padding + "s", s);
    }

    public String padRight(String s, int padding) {
        return String.format("%1$-" + padding + "s", s);
    }

}
