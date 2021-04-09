package de.shadowsoft.greenLicense.common.license;

import de.shadowsoft.greenLicense.common.crypto.AESCrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class GreenLicenseReaderV1 extends GreenLicenseReader {
    private static final int MAGIC_BYTES = 1027728000;

    public GreenLicenseReaderV1(final byte[] pk) {
        super(pk);
    }

    @Override
    public final GreenLicense readLicense(final byte[] lic) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SignatureException, InvalidKeySpecException {
        final GreenLicense license = new GreenLicense(LicenseVersion.LICENSE_V1);
        final int magicBytes = ByteBuffer.wrap(lic).getInt(0);
        final int length = ByteBuffer.wrap(lic).getInt(4);
        int lastIdx = -1;
        final byte sigTerm = (byte) '$';
        for (int i = 0; i < lic.length; i++) {
            if (lic[i] == sigTerm) {
                lastIdx = i;
            }
        }
        final int readFromIdx = lastIdx + 1;
        final byte[] sigByte = Arrays.copyOfRange(lic, readFromIdx, lic.length);
        final byte[] payload = Arrays.copyOfRange(lic, 8 + length, lastIdx);
        final byte[] toCheck = Arrays.copyOfRange(lic, 0, lastIdx);
        final byte[] secretKeyByte = Arrays.copyOfRange(lic, 8, 8 + length);
        final PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getPk()));
        final AESCrypt aesDec = new AESCrypt(getKey(decrypt(secretKeyByte, publicKey)));
        final byte[] decPayload = aesDec.decrypt(payload);
        license.setValidSignature(verify(toCheck, Base64.getDecoder().decode(sigByte), publicKey));
        license.setValidMagicBytes(magicBytes == MAGIC_BYTES);
        license.setFeature(payloadToMap(new String(decPayload)));
        return license;
    }
}
    
    