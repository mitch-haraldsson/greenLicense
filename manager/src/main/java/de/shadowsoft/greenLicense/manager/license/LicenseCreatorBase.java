package de.shadowsoft.greenLicense.manager.license;

import de.shadowsoft.greenLicense.common.license.generator.core.IdCreator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdResult;
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
import java.util.Base64;

public abstract class LicenseCreatorBase {


    abstract public byte[] createLicense(License license) throws GeneralSecurityException, NoSuchKeyPairException, IOException, DataLoadingException, InterruptedException;

    protected byte[] encrypt(byte[] plainText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return encryptCipher.doFinal(plainText);
    }

    protected String featureBuilder(License license) {
        StringBuilder builder = new StringBuilder();
        for (Feature feature : license.getSoftware().getFeatures()) {
            builder.append(feature.getId()).append(":").append(feature.getValue()).append("\n");
        }
        return builder.toString();
    }

    protected byte[] getPayload(License license) throws IOException, InterruptedException {
        byte[] licensePayload = featureBuilder(license).getBytes();
        byte[] licensePayloadLength = intToByte(licensePayload.length);

        // If license has no system ID, create an empty one to match all systems
        if (license.getSystemId() == null || license.getSystemId().length() == 0) {
            IdResult res = new IdResult();
            res.setSelector(Byte.parseByte("00000000", 2));
            IdCreator idCreator = new IdCreator();
            license.setSystemId(new String(Base64.getEncoder().encode(idCreator.createId(res))));
        }
        byte[] systemId = Base64.getDecoder().decode(license.getSystemId().getBytes());
        byte[] licenseIdLength = intToByte(systemId.length);
        byte[] res = ArrayUtils.addAll(licensePayloadLength, licensePayload);
        res = ArrayUtils.addAll(res, licenseIdLength);
        return ArrayUtils.addAll(res, systemId);
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
