package de.shadowsoft.greenLicense.manager.ui.jfxclient;

import de.shadowsoft.greenLicense.manager.ui.cli.CliInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

public class LicenseManager {
    private static final Logger LOGGER = LogManager.getLogger(LicenseManager.class);

    public static void main(String[] args) {
        new CommandLine(new CliInitializer()).execute(args);
    }
}
    
    