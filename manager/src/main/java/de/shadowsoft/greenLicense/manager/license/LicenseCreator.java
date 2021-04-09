package de.shadowsoft.greenLicense.manager.license;

import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

public abstract class LicenseCreator {


    abstract public byte[] createLicense(License license) throws GeneralSecurityException, NoSuchKeyPairException, IOException, DataLoadingException;

    protected byte[] encrypt(byte[] plainText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return encryptCipher.doFinal(plainText);
    }

    protected String getPayloadV1(License license) {
        StringBuilder builder = new StringBuilder();
        for (Feature feature : license.getSoftware().getFeatures()) {
            builder.append(feature.getId()).append(":").append(feature.getValue()).append("\n");
        }
        return builder.toString();
    }

    protected byte[] getPayloadV2(License license) {
        byte[] licensePayload = getPayloadV1(license).getBytes();
        byte[] licensePayloadLength = intToByte(licensePayload.length);
        byte[] licenseId = license.getLicenseId().getBytes();
        byte[] licenseIdLength = intToByte(licenseId.length);
        byte[] res = ArrayUtils.addAll(licensePayloadLength, licensePayload);
        res = ArrayUtils.addAll(res, licenseIdLength);
        return ArrayUtils.addAll(res, licenseId);
    }

    protected byte[] intToByte(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    protected byte[] sign(byte[] plainText, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText);
        return privateSignature.sign();
    }
}
