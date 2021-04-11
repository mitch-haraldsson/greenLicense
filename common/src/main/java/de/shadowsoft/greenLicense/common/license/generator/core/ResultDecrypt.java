package de.shadowsoft.greenLicense.common.license.generator.core;

public class ResultDecrypt extends ResultCrypt {

    private final byte[] input;

    public ResultDecrypt(final byte[] input) {
        this.input = input;
    }

    @Override
    public final void run() {
        setResult(reverseByteOrder(shiftBytes(input, getEncryptionSequence())));
    }
}
    
    