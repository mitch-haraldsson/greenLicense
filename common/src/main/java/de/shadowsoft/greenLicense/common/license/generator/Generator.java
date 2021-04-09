package de.shadowsoft.greenLicense.common.license.generator;

public abstract class Generator implements Runnable {

    private byte[] result;

    public Generator() {
    }

    public final byte[] getResult() {
        return result;
    }

    public final void setResult(final byte[] result) {
        this.result = result;
    }
}
    
    