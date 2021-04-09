package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StageManager {
    private static StageManager instance;
    private final Map<String, Stage> stages;

    private StageManager() {
        stages = new HashMap<>();
    }

    public static StageManager getInstance() {
        if (instance == null) {
            instance = new StageManager();
        }
        return instance;
    }

    public void setStage(String id, Stage stage) {
        stages.put(id, stage);
    }

    public Stage getStage(String id) {
        Stage newStage = stages.get(id);
        if (newStage == null) {
            newStage = new Stage();
            stages.put(id, newStage);
        }
        return newStage;
    }

    public void removeStage(String id) {
        stages.remove(id);
    }

    public boolean killStage(String id) {
        Stage stage = stages.get(id);
        stages.remove(id);
        if (stage == null) {
            return false;
        }
        stage.close();
        return true;
    }

    public boolean killAllStages() {
        for (Map.Entry<String, Stage> entry : stages.entrySet()) {
            entry.getValue().close();
        }
        stages.clear();
        return true;
    }
}
