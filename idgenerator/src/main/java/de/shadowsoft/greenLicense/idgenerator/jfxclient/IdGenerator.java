package de.shadowsoft.greenLicense.idgenerator.jfxclient;

import de.shadowsoft.greenLicense.idgenerator.jfxclient.cli.CliInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;


public class IdGenerator {
    private static final Logger LOGGER = LogManager.getLogger(IdGenerator.class);

    public static void main(String[] args) {
        new CommandLine(new CliInitializer()).execute(args);
    }
}
    
    