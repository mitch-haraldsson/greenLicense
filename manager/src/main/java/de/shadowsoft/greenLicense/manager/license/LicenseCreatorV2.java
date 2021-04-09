package de.shadowsoft.greenLicense.manager.license;

import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;

public class LicenseCreatorV2 extends LicenseCreator {
    private static final Logger LOGGER = LogManager.getLogger(LicenseCreatorV2.class);
    public static int MAGIC_BYTES = 1537221600;

    @Override
    public byte[] createLicense(final License license) throws GeneralSecurityException, NoSuchKeyPairException, IOException, DataLoadingException {

/*
License scheme:
    <magic byte>
    <key length><key>
    <payload length>
        <license length><license>
        <ID length><ID>
    <signature length><signature>
  */
        FssKeyPair fssKeyPair = KeyPairService.getInstance().getKeyById(license.getSoftware().getKeyPairId());
        if (fssKeyPair != null) {
            KeyPair pair = fssKeyPair.getKeyPair();
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            byte[] encSecKey = encrypt(secretKey.getEncoded(), pair.getPrivate());
            AESCrypt encAes = new AESCrypt(secretKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(intToByte(MAGIC_BYTES));
            out.write(intToByte(encSecKey.length));
            out.write(encSecKey);
            byte[] encMsg = encAes.encrypt(getPayloadV2(license));
            out.write(intToByte(encMsg.length));
            out.write(encMsg);
            byte[] signature = sign(out.toByteArray(), pair.getPrivate());

            out.write(intToByte(signature.length));
            out.write(signature);
            return out.toByteArray();
        } else {
            throw new NoSuchKeyPairException(String.format("Key pair %s is not available", license.getSoftware().getKeyPairId()));
        }
    }
}
    
    