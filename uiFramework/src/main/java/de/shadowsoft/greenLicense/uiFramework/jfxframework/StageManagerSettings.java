package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import javafx.scene.image.Image;

public class StageManagerSettings {
    private static StageManagerSettings instance;

    public static StageManagerSettings getInstance() {
        if (instance == null) {
            instance = new StageManagerSettings();
        }
        return instance;
    }

    private Image applicationIcon;


    private StageManagerSettings() {

    }

    public Image getApplicationIcon() {
        return applicationIcon;
    }

    public void setApplicationIcon(Image applicationIcon) {
        this.applicationIcon = applicationIcon;
    }
}
