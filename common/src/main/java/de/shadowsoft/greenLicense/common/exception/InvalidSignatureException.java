package de.shadowsoft.greenLicense.common.exception;

public class InvalidSignatureException extends Exception {
    public InvalidSignatureException() {
    }

    public InvalidSignatureException(final String message) {
        super(message);
    }

    public InvalidSignatureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidSignatureException(final Throwable cause) {
        super(cause);
    }

    public InvalidSignatureException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    