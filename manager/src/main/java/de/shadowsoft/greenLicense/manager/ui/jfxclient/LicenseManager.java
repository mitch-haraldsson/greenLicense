package de.shadowsoft.greenLicense.manager.ui.jfxclient;

import de.shadowsoft.greenLicense.manager.ui.cli.CliInitializer;
import picocli.CommandLine;

public class LicenseManager {

    public static void main(String[] args) {
        new CommandLine(new CliInitializer()).execute(args);
    }
}
    
    