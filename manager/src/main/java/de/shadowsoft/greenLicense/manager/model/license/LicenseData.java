package de.shadowsoft.greenLicense.manager.model.license;

import java.util.ArrayList;
import java.util.List;

public class LicenseData {

    private List<License> licenses;

    public LicenseData() {
        licenses = new ArrayList<>();
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(final List<License> licenses) {
        this.licenses = licenses;
    }
}
    
    