package de.shadowsoft.greenLicense.common.license.generator.mac;


public class MacEncrypt extends MacCrypt {

    private final byte[] mac;

    public MacEncrypt(final byte[] mac) {
        this.mac = mac;
    }

    @Override
    public final void run() {
        setResult(shiftBytes(reverseByteOrder(mac), getEncryptionSequence()));
    }
}
    
    