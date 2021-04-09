package de.shadowsoft.greenLicense.manager.ui.cli.output.export;

import de.shadowsoft.greenLicense.manager.model.license.License;

public class CliOutExport {

    private String base64License;
    private String filePath;
    private License license;

    public CliOutExport() {
        license = new License();
        base64License = "";
    }

    public CliOutExport(final License license) {
        this.license = license;

    }

    public String getBase64License() {
        return base64License;
    }

    public void setBase64License(final String base64License) {
        this.base64License = base64License;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }
}
    
    