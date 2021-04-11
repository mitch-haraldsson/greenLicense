package de.shadowsoft.greenLicense.common.cli.tool;

import de.shadowsoft.greenLicense.common.cli.CliOutBase;

public class ConsoleWriter {


    public static <T extends CliOutBase> void print(T msg) {
        print(msg, false);
    }

    public static <T extends CliOutBase> void print(T msg, boolean useJson) {
        if (!useJson) {
            System.out.println(msg.output());
        } else {
            System.out.println(new JsonPrinter().jsonPrinter(msg));
        }
    }

}
    
    