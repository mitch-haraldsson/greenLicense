package de.shadowsoft.greenLicense.idgenerator.jfxclient.config;

import java.util.Locale;

public class ProgramSettings {
    private static ProgramSettings instance;

    public static ProgramSettings getInstance() {
        if (instance == null) {
            instance = new ProgramSettings();
        }
        return instance;
    }

    private Locale locale;
    private String programTitleBarPrefix;

    private ProgramSettings() {
        programTitleBarPrefix = "GreenLicense ID generator - ";
        locale = Locale.getDefault();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public String getProgramTitleBarPrefix() {
        return programTitleBarPrefix;
    }

    public void setProgramTitleBarPrefix(final String programTitleBarPrefix) {
        this.programTitleBarPrefix = programTitleBarPrefix;
    }
}
