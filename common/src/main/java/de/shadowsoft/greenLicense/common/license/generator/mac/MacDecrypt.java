package de.shadowsoft.greenLicense.common.license.generator.mac;

public class MacDecrypt extends MacCrypt {

    private final byte[] input;

    public MacDecrypt(final byte[] input) {
        this.input = input;
    }

    @Override
    public final void run() {
        setResult(reverseByteOrder(shiftBytes(input, getEncryptionSequence())));
    }
}
    
    