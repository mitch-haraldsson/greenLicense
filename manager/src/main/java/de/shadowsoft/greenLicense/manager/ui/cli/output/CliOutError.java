package de.shadowsoft.greenLicense.manager.ui.cli.output;

public class CliOutError {

    private String exception;
    private String message;

    public CliOutError(final String message, final Exception e) {
        this.exception = e.getMessage();
        this.message = message;
    }

    public CliOutError(String message) {
        exception = "";
        this.message = message;
    }


    public CliOutError() {
        exception = "";
        message = "";
    }

    public String getException() {
        return exception;
    }

    public void setException(final String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
    
    