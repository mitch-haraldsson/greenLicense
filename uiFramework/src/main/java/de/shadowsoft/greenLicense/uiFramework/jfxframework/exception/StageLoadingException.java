package de.shadowsoft.greenLicense.uiFramework.jfxframework.exception;

public class StageLoadingException extends RuntimeException {
    public StageLoadingException() {
    }

    public StageLoadingException(String message) {
        super(message);
    }

    public StageLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StageLoadingException(Throwable cause) {
        super(cause);
    }

    public StageLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
    
    