package de.shadowsoft.greenLicense.core.cli.tool;

import de.shadowsoft.greenLicense.core.cli.CliOutBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleWriter {

    private static final Logger LOGGER = LogManager.getLogger(ConsoleWriter.class);

    public static <T extends CliOutBase> void print(T msg) {
        print(msg, false);
    }

    public static <T extends CliOutBase> void print(T msg, boolean useJson) {
        if (!useJson) {
            System.out.println(msg.output(LOGGER));
        } else {
            System.out.println(new JsonPrinter().jsonPrinter(msg));
        }
    }

}
    
    