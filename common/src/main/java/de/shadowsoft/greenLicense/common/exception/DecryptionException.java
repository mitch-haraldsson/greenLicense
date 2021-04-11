package de.shadowsoft.greenLicense.common.exception;

public class DecryptionException extends Exception {
    public DecryptionException() {
    }

    public DecryptionException(final String message) {
        super(message);
    }

    public DecryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DecryptionException(final Throwable cause) {
        super(cause);
    }

    public DecryptionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    