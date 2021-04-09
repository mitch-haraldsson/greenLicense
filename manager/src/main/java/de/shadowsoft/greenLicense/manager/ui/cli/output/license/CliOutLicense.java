package de.shadowsoft.greenLicense.manager.ui.cli.output.license;

import de.shadowsoft.greenLicense.manager.model.license.License;

public class CliOutLicense {

    private String id;
    private String licenseId;
    private String name;
    private String software;
    private String softwareId;

    public CliOutLicense(License license) {
        this.id = license.getId();
        this.licenseId = license.getLicenseId();
        this.name = license.getName();
        this.software = license.getSoftware().getName();
        this.softwareId = license.getSoftware().getId();
    }

    public CliOutLicense() {
        this.id = "";
        this.licenseId = "";
        this.name = "";
        this.software = "";
        this.softwareId = "";
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(final String licenseId) {
        this.licenseId = licenseId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(final String software) {
        this.software = software;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(final String softwareId) {
        this.softwareId = softwareId;
    }
}
    
    