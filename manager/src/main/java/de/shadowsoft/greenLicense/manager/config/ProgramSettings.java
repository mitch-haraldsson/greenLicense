package de.shadowsoft.greenLicense.manager.config;

public class ProgramSettings {

    private String basePath;

    public ProgramSettings() {
        basePath = "./";
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(final String basePath) {
        this.basePath = basePath;
        if (!this.basePath.endsWith("/") && !this.basePath.endsWith("\\")) {
            this.basePath = this.basePath + "/";
        }
    }
}
