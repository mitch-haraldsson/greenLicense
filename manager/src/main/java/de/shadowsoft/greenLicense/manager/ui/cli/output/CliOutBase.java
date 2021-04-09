package de.shadowsoft.greenLicense.manager.ui.cli.output;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class CliOutBase {

    private List<CliOutError> errorMessages;

    private boolean success;

    public CliOutBase() {
        success = true;
        errorMessages = new ArrayList<>();
    }

    public void addError(String msg) {
        errorMessages.add(new CliOutError(msg));
    }

    public void addError(String msg, Exception e) {
        errorMessages.add(new CliOutError(msg, e));
    }

    public String booleanToString(boolean b) {
        return b ? "true" : "false";
    }

    public abstract String formatOutput(StringBuilder res);

    public List<CliOutError> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(final List<CliOutError> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String output(Logger logger) {
        return output(new StringBuilder(), logger);
    }

    public String output(StringBuilder res, Logger logger) {
        res.append("\nSuccess: ").append(booleanToString(isSuccess()));
        res.append("\n");
        for (CliOutError error : getErrorMessages()) {
            logger.error(error.getException() + ": " + error.getMessage());
        }
        return formatOutput(res);
    }
}
    
