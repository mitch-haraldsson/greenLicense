package de.shadowsoft.greenLicense.common.license.generator.core;


import de.shadowsoft.greenLicense.common.license.generator.Generator;

public abstract class ResultCrypt extends Generator {
    protected final byte[] getEncryptionSequence() {
        return new byte[]{
                (byte) 0x34, (byte) 0x39, (byte) 0x38, ~(byte) 0x62, (byte) 0x37, (byte) 0x30, (byte) 0x34, (byte) 0x62, ~(byte) 0x65, ~(byte) 0x34, ~(byte) 0x34, (byte) 0x35, (byte) 0x33, (byte) 0x33, (byte) 0x61,
                ~(byte) 0x32, (byte) 0x63, (byte) 0x32, (byte) 0x66, ~(byte) 0x63, (byte) 0x62, (byte) 0x38, ~(byte) 0x65, (byte) 0x30, (byte) 0x35, (byte) 0x31, (byte) 0x65, ~(byte) 0x34, (byte) 0x31, (byte) 0x63,
                (byte) 0x64, (byte) 0x66, (byte) 0x34, ~(byte) 0x65, (byte) 0x38, ~(byte) 0x33, (byte) 0x63, (byte) 0x31, (byte) 0x32, (byte) 0x64, (byte) 0x63, ~(byte) 0x30, (byte) 0x64, ~(byte) 0x30, (byte) 0x31,
                (byte) 0x39, (byte) 0x34, ~(byte) 0x37, (byte) 0x36, ~(byte) 0x36, (byte) 0x33, (byte) 0x61, (byte) 0x38, ~(byte) 0x33, (byte) 0x61, ~(byte) 0x62, (byte) 0x63, (byte) 0x33, (byte) 0x65, (byte) 0x36,
                (byte) 0x62, ~(byte) 0x38, (byte) 0x38, (byte) 0x33, (byte) 0x31, (byte) 0x64, (byte) 0x35, ~(byte) 0x39, (byte) 0x65, ~(byte) 0x37, (byte) 0x39, (byte) 0x35, (byte) 0x39, (byte) 0x33, (byte) 0x37,
                ~(byte) 0x30, (byte) 0x38, ~(byte) 0x36, (byte) 0x64, (byte) 0x61, ~(byte) 0x38, (byte) 0x33, (byte) 0x64, (byte) 0x31, (byte) 0x30, (byte) 0x37, ~(byte) 0x65, (byte) 0x36, (byte) 0x39, (byte) 0x34,
                (byte) 0x38, (byte) 0x63, (byte) 0x38, (byte) 0x33, (byte) 0x37, (byte) 0x39, (byte) 0x32, (byte) 0x35, (byte) 0x61, ~(byte) 0x64, (byte) 0x35, (byte) 0x61, (byte) 0x30, (byte) 0x38, (byte) 0x34,
                (byte) 0x65, (byte) 0x63, (byte) 0x32, (byte) 0x31, (byte) 0x33, (byte) 0x65, (byte) 0x64, (byte) 0x63, (byte) 0x33, (byte) 0x34, ~(byte) 0x36, (byte) 0x38, (byte) 0x33, (byte) 0x32, (byte) 0x37,
                ~(byte) 0x30, (byte) 0x62, (byte) 0x65, (byte) 0x38, ~(byte) 0x38, (byte) 0x31, (byte) 0x39, (byte) 0x38
        };
    }

    protected final byte[] reverseByteOrder(final byte[] in) {
        final byte[] rbo = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            rbo[in.length - 1 - i] = in[i];
        }
        return rbo;
    }

    protected final byte[] shiftBytes(final byte[] in, final byte[] shifter) {
        final byte[] encoded = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            final int x = shifter[i % shifter.length];
            encoded[i] = (byte) (~in[i] ^ x);
        }
        return encoded;
    }
}
    
    