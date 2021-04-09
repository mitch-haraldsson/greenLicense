package de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx;

import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class FssJfxLoader {
    private static final Logger LOGGER = LogManager.getLogger(FssJfxLoader.class);

    public Pane loadPane(String resource, Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("messages", new Locale("en", "us")));
            String resourceFile = "/view/" + resource + ".fxml";
            loader.setLocation(getClass().getResource((resourceFile)));
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to load resource %s", resource), e);
            throw new RuntimeException("Unable to load FXML resource");
        }
    }


}
    
    