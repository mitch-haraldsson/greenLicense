package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import javafx.stage.Stage;

public abstract class Controller {
    private Stage stage;
    private String stageKey;

    public Controller() {

    }

    public abstract void postShow();

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getStageKey() {
        return stageKey;
    }

    public void killStage() {
        StageManager.getInstance().killStage(getStageKey());
    }

    public void removeStageFromManager() {
        StageManager.getInstance().removeStage(getStageKey());
    }

    public void setStageKey(String stageKey) {
        this.stageKey = stageKey;
    }
}
    