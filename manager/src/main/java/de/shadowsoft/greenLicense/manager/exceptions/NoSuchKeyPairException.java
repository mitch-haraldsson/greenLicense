package de.shadowsoft.greenLicense.manager.exceptions;

public class NoSuchKeyPairException extends Exception {

    public NoSuchKeyPairException() {
    }

    public NoSuchKeyPairException(final String message) {
        super(message);
    }

    public NoSuchKeyPairException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoSuchKeyPairException(final Throwable cause) {
        super(cause);
    }

    public NoSuchKeyPairException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    