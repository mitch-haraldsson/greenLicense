package de.shadowsoft.greenLicense.manager.ui.cli.output.license;

import de.shadowsoft.greenLicense.core.cli.CliOutBase;

import java.util.ArrayList;
import java.util.List;

public class CliOutLicenseCollection extends CliOutBase {
    private List<CliOutLicense> licenses;

    public CliOutLicenseCollection() {
        licenses = new ArrayList<>();
    }

    @Override
    public String formatOutput(final StringBuilder res) {
        for (CliOutLicense license : licenses) {
            res.append("\n").append(license.getId()).append(":");
            res.append(license.getLicenseId()).append(":");
            res.append(license.getName()).append(":");
            res.append(license.getSoftware());
        }
        return res.toString();
    }

    public List<CliOutLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(final List<CliOutLicense> licenses) {
        this.licenses = licenses;
    }
}
    
    