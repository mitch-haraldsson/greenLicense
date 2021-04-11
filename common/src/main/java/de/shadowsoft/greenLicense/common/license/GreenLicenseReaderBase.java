package de.shadowsoft.greenLicense.common.license;

import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public abstract class GreenLicenseReaderBase implements GreenLicenseValidator {
    private final byte[] pk;

    public GreenLicenseReaderBase(final byte[] pk) {
        this.pk = pk;
    }

    protected final byte[] decrypt(final byte[] cipherText, final PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        final Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
        return decryptCipher.doFinal(cipherText);
    }

    protected final SecretKey getKey(final byte[] encodedKey) {
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public byte[] getPk() {
        return pk;
    }

    protected final Map<String, String> payloadToMap(final String payload) {
        final Map<String, String> features = new HashMap<>();
        final String[] lines = payload.split("\n");
        if (lines.length > 1) {
            String id = "";
            for (final String line : lines) {
                final String[] featureParts = line.split(":");
                id = featureParts[0];
                boolean isFirst = true;
                String feature = "";
                for (int i = 1; i < featureParts.length; i++) {
                    if (!isFirst) {
                        feature = feature.concat(":");
                    }
                    isFirst = false;
                    feature = feature.concat(featureParts[i]);
                }
                features.put(id, feature);
            }
        }
        return features;
    }

    @Override
    public final GreenLicense readLicenseFromFile(final String path) throws DecryptionException, SystemValidationException, InvalidSignatureException, IOException {
        return readLicense(Files.readAllBytes(Paths.get(path)));
    }

    protected final boolean verify(final byte[] plainText, final byte[] signature, final PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText);
        return publicSignature.verify(signature);
    }
}
    
    