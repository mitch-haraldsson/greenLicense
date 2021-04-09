package de.shadowsoft.greenLicense.manager.ui.jfxclient;

import de.shadowsoft.greenLicense.manager.license.AESCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class LicenseValidationTest {
    private static final Logger LOGGER = LogManager.getLogger(LicenseValidationTest.class);
    private static byte pk[] = new byte[]{
            (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x22, (byte) 0x30, (byte) 0x0D, (byte) 0x06, (byte) 0x09, (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x82, (byte) 0x01, (byte) 0x0F, (byte) 0x00, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x0A, (byte) 0x02, (byte) 0x82,
            (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0xB2, (byte) 0x9D, (byte) 0xCA, (byte) 0xF4, (byte) 0xA8, (byte) 0xFD, (byte) 0x29, (byte) 0xFC, (byte) 0xAC, (byte) 0x52, (byte) 0x86, (byte) 0xC3,
            (byte) 0xA7, (byte) 0xDD, (byte) 0xF4, (byte) 0xE5, (byte) 0x2B, (byte) 0x34, (byte) 0x74, (byte) 0xC3, (byte) 0x3D, (byte) 0x51, (byte) 0xCF, (byte) 0x5C, (byte) 0x78, (byte) 0x80, (byte) 0x2D,
            (byte) 0x71, (byte) 0x67, (byte) 0x0C, (byte) 0xA6, (byte) 0x48, (byte) 0x4A, (byte) 0xEE, (byte) 0xE5, (byte) 0xA1, (byte) 0xDD, (byte) 0xFE, (byte) 0xF8, (byte) 0x0E, (byte) 0xDB, (byte) 0xCF,
            (byte) 0x8B, (byte) 0x9A, (byte) 0xE0, (byte) 0xC6, (byte) 0x4C, (byte) 0x36, (byte) 0x98, (byte) 0x6F, (byte) 0xC8, (byte) 0x63, (byte) 0x53, (byte) 0x67, (byte) 0x4E, (byte) 0x19, (byte) 0x17,
            (byte) 0x53, (byte) 0xFF, (byte) 0x09, (byte) 0xF0, (byte) 0xF3, (byte) 0xD5, (byte) 0x42, (byte) 0xB3, (byte) 0x8E, (byte) 0xB4, (byte) 0x49, (byte) 0x3D, (byte) 0x43, (byte) 0x9B, (byte) 0xAD,
            (byte) 0x42, (byte) 0x80, (byte) 0x3E, (byte) 0x0A, (byte) 0x4D, (byte) 0x2C, (byte) 0xCD, (byte) 0x6D, (byte) 0x69, (byte) 0xBD, (byte) 0xC8, (byte) 0x96, (byte) 0x07, (byte) 0xFB, (byte) 0x6F,
            (byte) 0x19, (byte) 0x4F, (byte) 0xA8, (byte) 0xAF, (byte) 0x4B, (byte) 0x62, (byte) 0x3F, (byte) 0xBB, (byte) 0x26, (byte) 0xE9, (byte) 0x61, (byte) 0x5A, (byte) 0x6E, (byte) 0xCD, (byte) 0x4E,
            (byte) 0x81, (byte) 0x42, (byte) 0x73, (byte) 0x54, (byte) 0xAE, (byte) 0x6C, (byte) 0x17, (byte) 0x28, (byte) 0xCF, (byte) 0xA3, (byte) 0x9F, (byte) 0x85, (byte) 0x36, (byte) 0xD4, (byte) 0xC6,
            (byte) 0xCE, (byte) 0xBD, (byte) 0x40, (byte) 0xDD, (byte) 0x14, (byte) 0x17, (byte) 0x87, (byte) 0x95, (byte) 0x3A, (byte) 0xA2, (byte) 0x26, (byte) 0xE5, (byte) 0xE7, (byte) 0x0E, (byte) 0x6E,
            (byte) 0x59, (byte) 0x3D, (byte) 0x07, (byte) 0x77, (byte) 0x09, (byte) 0x36, (byte) 0x35, (byte) 0xFE, (byte) 0x0B, (byte) 0x7A, (byte) 0xB9, (byte) 0x97, (byte) 0xC3, (byte) 0xE2, (byte) 0xDE,
            (byte) 0x0F, (byte) 0x64, (byte) 0x58, (byte) 0x6C, (byte) 0x40, (byte) 0xFE, (byte) 0x9B, (byte) 0xBA, (byte) 0x77, (byte) 0x27, (byte) 0xA6, (byte) 0x97, (byte) 0x5E, (byte) 0x31, (byte) 0xF2,
            (byte) 0xD3, (byte) 0x1A, (byte) 0xCF, (byte) 0xBF, (byte) 0x1B, (byte) 0xE9, (byte) 0x16, (byte) 0xDD, (byte) 0x58, (byte) 0xAB, (byte) 0x9D, (byte) 0x01, (byte) 0x18, (byte) 0xF9, (byte) 0x82,
            (byte) 0x65, (byte) 0x30, (byte) 0x17, (byte) 0xDA, (byte) 0x73, (byte) 0x77, (byte) 0xC5, (byte) 0x6E, (byte) 0x3E, (byte) 0x17, (byte) 0x54, (byte) 0x42, (byte) 0x56, (byte) 0x3C, (byte) 0xF7,
            (byte) 0x79, (byte) 0xCD, (byte) 0x1F, (byte) 0xF3, (byte) 0xCE, (byte) 0xF3, (byte) 0xFD, (byte) 0x3D, (byte) 0x25, (byte) 0x67, (byte) 0x20, (byte) 0x01, (byte) 0x2E, (byte) 0xAA, (byte) 0xD5,
            (byte) 0x41, (byte) 0x75, (byte) 0x78, (byte) 0xEF, (byte) 0xA0, (byte) 0x07, (byte) 0xD8, (byte) 0xC9, (byte) 0xF7, (byte) 0x78, (byte) 0xAD, (byte) 0x4A, (byte) 0x60, (byte) 0xE5, (byte) 0xF4,
            (byte) 0x64, (byte) 0x38, (byte) 0x81, (byte) 0x62, (byte) 0x9F, (byte) 0xC8, (byte) 0x9A, (byte) 0xC4, (byte) 0x12, (byte) 0xC0, (byte) 0xF2, (byte) 0x90, (byte) 0xE9, (byte) 0xBF, (byte) 0x64,
            (byte) 0x6C, (byte) 0xF5, (byte) 0xDC, (byte) 0xB1, (byte) 0x2D, (byte) 0xC0, (byte) 0x88, (byte) 0x1B, (byte) 0x6C, (byte) 0x41, (byte) 0x46, (byte) 0x19, (byte) 0x1F, (byte) 0x1F, (byte) 0xB2,
            (byte) 0xD2, (byte) 0x49, (byte) 0x1E, (byte) 0x81, (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x01
    };

    public static byte[] decrypt(byte[] cipherText, PublicKey publicKey) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
        return decryptCipher.doFinal(cipherText);
    }

    private static SecretKey getKey(byte[] encodedKey) {
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static void main(String[] args) throws Exception {
        byte[] lic = Files.readAllBytes(Paths.get("./licenses/Softwaretest.lic"));
        int magicBytes = ByteBuffer.wrap(lic).getInt(0);
        int encLength = ByteBuffer.wrap(lic).getInt(4);
//        for (int i = 0; i < lic.length - 4; i++) {
//            int l = ByteBuffer.wrap(lic).getInt(i);
//            if (l == 208) {
//                LOGGER.info(String.format("Found byte at pos %s", i));
//            }
//        }
        int lastReadPos = 8;
        byte[] secretKeyByte = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + encLength);
        lastReadPos += encLength;
        int payloadLength = ByteBuffer.wrap(lic).getInt(lastReadPos);
        lastReadPos += 4;
        byte[] encPayload = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + payloadLength);
        lastReadPos += payloadLength;
        int toCheckLength = lastReadPos;
        int signatureLength = ByteBuffer.wrap(lic).getInt(lastReadPos);
        lastReadPos += 4;
        byte[] sigByte = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + signatureLength);
        byte[] toCheck = Arrays.copyOfRange(lic, 0, toCheckLength);
//        int lastIdx = -1;
//        byte sigTerm = (byte) '$';
//        for (int i = 0; i < lic.length; i++) {
//            if (lic[i] == sigTerm) {
//                lastIdx = i;
//            }
//        }
//        int readFromIdx = lastIdx + 1;
//        byte[] sigByte = Arrays.copyOfRange(lic, readFromIdx, lic.length);
//        byte[] payload = Arrays.copyOfRange(lic, 8 + encLength, lastIdx);
//        byte[] toCheck = Arrays.copyOfRange(lic, 0, lastIdx);
//        byte[] secretKeyByte = Arrays.copyOfRange(lic, 8, 8 + encLength);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pk));
        AESCrypt aesDec = new AESCrypt(getKey(decrypt(secretKeyByte, publicKey)));
        byte[] decPayload = aesDec.decrypt(encPayload);
        int licenseLength = ByteBuffer.wrap(decPayload).getInt(0);
        lastReadPos = 4;
        byte[] licensePayload = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseLength);
        lastReadPos += licenseLength;
        int licenseIdLength = ByteBuffer.wrap(decPayload).getInt(lastReadPos);
        lastReadPos += 4;
        byte[] licenseId = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseIdLength);

        System.out.println("Magic Bytes         : " + magicBytes);
        System.out.println("Key length          : " + encLength);
        System.out.println("encSec              : " + Base64.getEncoder().encodeToString(secretKeyByte));
        System.out.println("payload             : " + Base64.getEncoder().encodeToString(licensePayload));
        System.out.println("License ID          : " + new String(licenseId));
        System.out.println("Lic signature       : " + new String(Base64.getEncoder().encode(sigByte)));
        System.out.println("Decrypted payload   : \n" + new String(licensePayload));
        System.out.println("Valid signature     : " + verify(toCheck, sigByte, publicKey));

    }

    public static boolean verify(byte[] plainText, byte[] signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText);
        return publicSignature.verify(signature);
    }
}
    
    