package de.shadowsoft.greenLicense.idgenerator.jfxclient;

import de.shadowsoft.greenLicense.idgenerator.jfxclient.config.ProgramSettings;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;


public class IdGenerator {
    private static final Logger LOGGER = LogManager.getLogger(IdGenerator.class);

    public static void main(String[] args) {
        LOGGER.info("Initializing UI...");
        Locale.setDefault(ProgramSettings.getInstance().getLocale());

        new Thread(() -> {
            Application.launch(UiInitializer.class);
        }).start();
    }
}
    
    