package de.shadowsoft.greenLicense.common.license;

import java.util.HashMap;
import java.util.Map;

public class GreenLicense {

    private Map<String, String> feature;
    private boolean valid;
    private boolean validMagicBytes;
    private boolean validSystem;

    public GreenLicense() {
        feature = new HashMap<>();
        valid = false;
        validSystem = false;
        validMagicBytes = false;
    }

    public final Map<String, String> getFeature() {
        return feature;
    }

    public final void setFeature(final Map<String, String> feature) {
        this.feature = feature;
    }

    public final boolean isValid() {
        return valid;
    }

    public final void setValid(final boolean valid) {
        this.valid = valid;
    }

    public final boolean isValidMagicBytes() {
        return validMagicBytes;
    }

    public final void setValidMagicBytes(final boolean validMagicBytes) {
        this.validMagicBytes = validMagicBytes;
    }

    public final boolean isValidSystem() {
        return validSystem;
    }

    public final void setValidSystem(final boolean validSystem) {
        this.validSystem = validSystem;
    }
}
    
    