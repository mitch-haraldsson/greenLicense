package de.shadowsoft.greenLicense.manager.ui.jfxclient.l18n;

import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    private ResourceBundle resources;

    private LanguageManager() {
        resources = ResourceBundle.getBundle("messages");
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(final ResourceBundle resources) {
        this.resources = resources;
    }
}
