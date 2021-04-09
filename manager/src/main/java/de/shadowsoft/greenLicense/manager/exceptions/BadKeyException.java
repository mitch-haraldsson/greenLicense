package de.shadowsoft.greenLicense.manager.exceptions;

public class BadKeyException extends Exception {
    public BadKeyException() {
    }

    public BadKeyException(final String message) {
        super(message);
    }

    public BadKeyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadKeyException(final Throwable cause) {
        super(cause);
    }

    public BadKeyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    