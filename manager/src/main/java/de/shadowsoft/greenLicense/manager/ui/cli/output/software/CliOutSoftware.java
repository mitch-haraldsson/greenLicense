package de.shadowsoft.greenLicense.manager.ui.cli.output.software;

import de.shadowsoft.greenLicense.manager.model.software.Software;

public class CliOutSoftware {

    private String id;
    private String licenseVersion;
    private String name;
    private String version;

    public CliOutSoftware() {
    }

    public CliOutSoftware(Software software) {
        this.id = software.getId();
        this.licenseVersion = software.getLicenseVersion().toShortString();
        this.name = software.getName();
        this.version = software.getVersion();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLicenseVersion() {
        return licenseVersion;
    }

    public void setLicenseVersion(final String licenseVersion) {
        this.licenseVersion = licenseVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
    
    