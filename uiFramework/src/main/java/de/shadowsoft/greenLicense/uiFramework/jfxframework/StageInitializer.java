package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import de.shadowsoft.greenLicense.uiFramework.jfxframework.exception.StageLoadingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class StageInitializer {
    public static final String RESOURCES_ROOT = "/view/";
    private Controller controller;
    private AnchorPane pnlMain;
    private Pane rootLayout;
    private Scene scene;
    private Stage stage;
    private String stageKey;

    public StageInitializer(String name, String stageKey, Controller controller, boolean modal) {
        this(name, stageKey, controller, modal, "en", "US");
    }

    public StageInitializer(String name, String stageKey, Controller controller, boolean modal, String language, String country) {
        this(name, stageKey, controller, modal, new Locale(language, country));
    }

    public StageInitializer(String name, String stageKey, Controller controller, boolean modal, Locale locale) {
        try {
            if (controller == null) {
                this.controller = new DummyController();
            } else {
                this.controller = controller;
            }
            this.stage = StageManager.getInstance().getStage(stageKey);
            if (modal) {
                this.stage.initModality(Modality.APPLICATION_MODAL);
            }
            this.stageKey = stageKey;
            this.controller.setStage(stage);
            this.controller.setStageKey(stageKey);

            load(name, this.controller, locale);
            loadComponents();
            if (StageManagerSettings.getInstance().getApplicationIcon() != null) {
                getStage().getIcons().add(StageManagerSettings.getInstance().getApplicationIcon());
            }
            stageInit();
        } catch (IOException e) {
            throw new StageLoadingException(e);
        }
    }

    public AnchorPane getPnlMain() {
        return pnlMain;
    }

    public void setPnlMain(AnchorPane pnlMain) {
        this.pnlMain = pnlMain;
    }

    public Pane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(Pane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public String getStageKey() {
        return stageKey;
    }

    public void setStageKey(String stageKey) {
        this.stageKey = stageKey;
    }

    public void hideStatus() {
        StageManager.getInstance().killStage("status");
    }

    public void load(String file, Controller controller) throws IOException {
        loadFile(file, controller, new Locale("en", "US"));
    }

    public void load(String file, Controller controller, Locale locale) throws IOException {
        loadFile(file, controller, locale);
    }

    public abstract void loadComponents();

    public void loadFile(String name, Controller controller, Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(ResourceBundle.getBundle("messages", locale));
        String resourceFile = RESOURCES_ROOT + name + ".fxml";
        loader.setLocation(getClass().getResource((resourceFile)));
        loader.setController(controller);
        rootLayout = loader.load();
        scene = new Scene(rootLayout);
        pnlMain = (AnchorPane) getScene().lookup("#pnlMain");
        stage.centerOnScreen();
    }

    public abstract void postThreadProcessing(String id);

    public void show() {
        getStage().setScene(getScene());
        getStage().show();
        controller.postShow();
    }

    public void showAndWait() {
        getStage().setScene(getScene());
        getStage().showAndWait();
    }

    public abstract void stageInit();
}
    
    