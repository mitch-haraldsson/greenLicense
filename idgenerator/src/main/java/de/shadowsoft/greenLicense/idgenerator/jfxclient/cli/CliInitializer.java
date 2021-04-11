package de.shadowsoft.greenLicense.idgenerator.jfxclient.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "ID Generator",
        subcommands = {
                CliUi.class,
                CliGenerateId.class
        }
)
public class CliInitializer {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
}

    
    