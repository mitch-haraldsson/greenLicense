package de.shadowsoft.greenLicense.common.license;

import de.shadowsoft.greenLicense.common.crypto.AESCrypt;
import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.generator.Generator;
import de.shadowsoft.greenLicense.common.license.generator.core.ResultCondenser;
import de.shadowsoft.greenLicense.common.license.generator.core.ResultDecrypt;
import de.shadowsoft.greenLicense.common.license.generator.core.SysInfo;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdResult;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

public class GreenLicenseReader extends GreenLicenseReaderBase {
    private static final int MAGIC_BYTES = 1645570800;

    public GreenLicenseReader(final byte[] pk) {
        super(pk);
    }

    private boolean byteArrayListCompareSingleItemMatch(final List<byte[]> validBytes, final List<byte[]> sysBytes) {
        for (byte[] sysByte : sysBytes) {
            for (byte[] validByte : validBytes) {
                if (Arrays.equals(sysByte, validByte)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkValidSystem(final byte[] licenseId) throws SystemValidationException {
        SysInfo info = new SysInfo();
        Generator generator = new ResultDecrypt(licenseId);
        ResultCondenser condenser = new ResultCondenser();
        IdResult res;
        try {
            Thread t = new Thread(generator);
            t.start();
            t.join();
            res = condenser.vaporize(generator.getResult());
        } catch (IOException | InterruptedException e) {
            throw new SystemValidationException("Unable to re-vaporize system ID", e);
        }

        boolean isValid = true;
        String selectorString = String.format("%8s", Integer.toBinaryString(res.getSelector() & 0xFF)).replace(' ', '0');

        try {
            for (int i = 0; i < selectorString.length(); i++) {
                if (selectorString.charAt(i) == '1') {
                    switch (i) {
                        case 7:
                            if (!hasValidMacAddress(res.getMacs(), info.getMacAddresses())) {
                                isValid = false;
                            }
                            break;

                        case 6:
                            if (!Arrays.equals(res.getHostname(), info.getComputerName().getBytes())) {
                                isValid = false;
                            }
                            break;

                        case 5:
                            if (!hasValidIpAddress(res.getIp(), info.getAllIps())) {
                                isValid = false;
                            }
                            break;

                        case 4:
                            if (!Arrays.equals(res.getOs(), info.getOs().getBytes())) {
                                isValid = false;
                            }
                            break;
                    }
                }
            }
        } catch (NullPointerException npe) {
            throw new SystemValidationException("Unable to verify system. No attribute found for given selector", npe);
        } catch (SocketException e) {
            throw new SystemValidationException("Unable to read sockets of network adapters", e);
        }
        return isValid;
    }

    private boolean hasValidIpAddress(final List<byte[]> ips, final List<byte[]> sysIps) {
        return byteArrayListCompareSingleItemMatch(ips, sysIps);
    }

    private boolean hasValidMacAddress(final List<byte[]> validMacs, final List<byte[]> sysMacs) {
        return byteArrayListCompareSingleItemMatch(validMacs, sysMacs);
    }

    @Override
    public GreenLicense readLicense(final byte[] lic) throws SystemValidationException, DecryptionException, InvalidSignatureException {
        final GreenLicense license = new GreenLicense();
        final int magicBytes = ByteBuffer.wrap(lic).getInt(0);
        final int encLength = ByteBuffer.wrap(lic).getInt(4);
        int lastReadPos = 8;
        final byte[] secretKeyByte = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + encLength);
        lastReadPos += encLength;
        final int payloadLength = ByteBuffer.wrap(lic).getInt(lastReadPos);
        lastReadPos += 4;
        final byte[] encPayload = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + payloadLength);
        lastReadPos += payloadLength;
        final int toCheckLength = lastReadPos;
        final int signatureLength = ByteBuffer.wrap(lic).getInt(lastReadPos);
        lastReadPos += 4;
        final byte[] sigByte = Arrays.copyOfRange(lic, lastReadPos, lastReadPos + signatureLength);
        final byte[] toCheck = Arrays.copyOfRange(lic, 0, toCheckLength);

        final byte[] decPayload;
        final PublicKey publicKey;

        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getPk()));
            final AESCrypt aesDec = new AESCrypt(getKey(decrypt(secretKeyByte, publicKey)));
            decPayload = aesDec.decrypt(encPayload);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | InvalidKeySpecException | IllegalBlockSizeException e) {
            throw new DecryptionException("Unable to decrypt payload", e);
        }
        final int licenseLength = ByteBuffer.wrap(decPayload).getInt(0);
        lastReadPos = 4;
        final byte[] licensePayload = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseLength);
        lastReadPos += licenseLength;
        final int licenseIdLength = ByteBuffer.wrap(decPayload).getInt(lastReadPos);
        lastReadPos += 4;
        final byte[] licenseId = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseIdLength);
        try {
            license.setValidSignature(verify(toCheck, sigByte, publicKey));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new InvalidSignatureException("The signature is invalid", e);
        }
        license.setValidMagicBytes(magicBytes == MAGIC_BYTES);
        license.setFeature(payloadToMap(new String(licensePayload)));
        license.setValidSystem(checkValidSystem(licenseId));
        return license;
    }
}
    
    