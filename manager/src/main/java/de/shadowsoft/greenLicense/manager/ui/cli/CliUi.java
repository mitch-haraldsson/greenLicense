package de.shadowsoft.greenLicense.manager.ui.cli;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.UiInitializer;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
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
        Locale.setDefault(ProgramSettingsService.getInstance().getSettings().getLocale());
        new Thread(() -> {
            Application.launch(UiInitializer.class);
        }).start();
    }
}
    
    