package de.shadowsoft.greenLicense.manager.tools.serializer.exception;

public class DataLoadingException extends Exception {
    public DataLoadingException() {
    }

    public DataLoadingException(final String message) {
        super(message);
    }

    public DataLoadingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataLoadingException(final Throwable cause) {
        super(cause);
    }

    public DataLoadingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    