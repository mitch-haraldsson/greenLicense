package de.shadowsoft.greenLicense.manager.exceptions;

public class EncryptionException extends Exception {
    public EncryptionException() {
    }

    public EncryptionException(final String message) {
        super(message);
    }

    public EncryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EncryptionException(final Throwable cause) {
        super(cause);
    }

    public EncryptionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    