package de.shadowsoft.greenLicense.common.license;

import de.shadowsoft.greenLicense.common.crypto.AESCrypt;
import de.shadowsoft.greenLicense.common.license.generator.mac.MacCrypt;
import de.shadowsoft.greenLicense.common.license.generator.mac.MacDecrypt;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GreenLicenseReaderV2 extends GreenLicenseReader {
    private static final int MAC_LENGTH = 6;
    private static final int MAGIC_BYTES = 1537221600;

    public GreenLicenseReaderV2(final byte[] pk) {
        super(pk);
    }

    private byte[] getMacAddressesBytes() throws SocketException {
        final List<byte[]> bytes = new ArrayList<>();
        final Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
        while (it.hasNext()) {
            final NetworkInterface inf = it.next();
            if (inf != null) {
                if (inf.getHardwareAddress() != null) {
                    bytes.add(inf.getHardwareAddress());
                }
            }
        }
        byte[] mac = null;
        for (final byte[] ba : bytes) {
            mac = ArrayUtils.addAll(mac, ba);
        }
        return mac;
    }

    private List<byte[]> getMacList(final byte[] macs) {
        final List<byte[]> res = new ArrayList<>();
        if (macs.length % MAC_LENGTH == 0) {
            for (int i = 0; i < macs.length; i += MAC_LENGTH) {
                final byte[] mac = new byte[MAC_LENGTH];
                for (int j = 0; j < MAC_LENGTH; j++) {
                    mac[j] = macs[i + j];
                }
                res.add(mac);
            }
        }
        return res;
    }

    private boolean hasValidMac(final byte[] licenseId) throws SocketException, InterruptedException {
        final MacCrypt macCrypt = new MacDecrypt(licenseId);
        final Thread t = new Thread(macCrypt);
        t.start();
        t.join();
        final List<byte[]> systemMacs = getMacList(getMacAddressesBytes());
        final List<byte[]> validMacs = getMacList(macCrypt.getResult());
        for (final byte[] sysMac : systemMacs) {
            for (final byte[] validMac : validMacs) {
                if (Arrays.equals(sysMac, validMac)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final GreenLicense readLicense(final byte[] lic) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SignatureException, InvalidKeySpecException, SocketException, InterruptedException {
        final GreenLicense license = new GreenLicense(LicenseVersion.LICENSE_V2);

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

        final PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getPk()));
        final AESCrypt aesDec = new AESCrypt(getKey(decrypt(secretKeyByte, publicKey)));
        final byte[] decPayload = aesDec.decrypt(encPayload);
        final int licenseLength = ByteBuffer.wrap(decPayload).getInt(0);
        lastReadPos = 4;
        final byte[] licensePayload = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseLength);
        lastReadPos += licenseLength;
        final int licenseIdLength = ByteBuffer.wrap(decPayload).getInt(lastReadPos);
        lastReadPos += 4;
        final byte[] licenseId = Arrays.copyOfRange(decPayload, lastReadPos, lastReadPos + licenseIdLength);
        license.setValidSignature(verify(toCheck, sigByte, publicKey));
        license.setValidMagicBytes(magicBytes == MAGIC_BYTES);
        license.setFeature(payloadToMap(new String(licensePayload)));
        license.setValidSystem(hasValidMac(licenseId));
        return license;
    }

}
    
    