package de.shadowsoft.greenLicense.common.exception;

public class SystemValidationException extends Exception {
    public SystemValidationException() {
    }

    public SystemValidationException(final String message) {
        super(message);
    }

    public SystemValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SystemValidationException(final Throwable cause) {
        super(cause);
    }

    public SystemValidationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    