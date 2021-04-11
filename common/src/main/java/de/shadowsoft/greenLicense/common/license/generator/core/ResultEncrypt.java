package de.shadowsoft.greenLicense.common.license.generator.core;


public class ResultEncrypt extends ResultCrypt {

    private final byte[] mac;

    public ResultEncrypt(final byte[] mac) {
        this.mac = mac;
    }

    @Override
    public final void run() {
        setResult(shiftBytes(reverseByteOrder(mac), getEncryptionSequence()));
    }
}
    
    