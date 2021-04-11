package de.shadowsoft.greenLicense.manager.license;

import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Base64;

public class LicenseCreatorV1 extends LicenseCreatorBase {
    public static int MAGIC_BYTES = 1027728000;

    @Override
    public byte[] createLicense(License license) throws GeneralSecurityException, NoSuchKeyPairException, IOException, DataLoadingException {
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
            byte[] encMsg = encAes.encrypt(featureBuilder(license).getBytes());
            out.write(encMsg);
            byte[] signature = sign(out.toByteArray(), pair.getPrivate());
            String signatureHead = "$";
            out.write(signatureHead.getBytes());
            String signatureString = new String(Base64.getEncoder().encode(signature));
            out.write(signatureString.getBytes());
            return out.toByteArray();
        } else {
            throw new NoSuchKeyPairException(String.format("Key pair %s is not available", license.getSoftware().getKeyPairId()));
        }
    }

}
    
    