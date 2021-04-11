package de.shadowsoft.greenLicense.idgenerator.jfxclient.cli;

import de.shadowsoft.greenLicense.idgenerator.jfxclient.UiInitializer;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.config.ProgramSettings;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.Locale;

@CommandLine.Command(name = "showui", description = "Show user interface")
public class CliUi implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(CliUi.class);

    @Override
    public void run() {
        LOGGER.info("Initializing UI...");
        Locale.setDefault(ProgramSettings.getInstance().getLocale());
        new Thread(() -> {
            Application.launch(UiInitializer.class);
        }).start();
    }
}
    
    