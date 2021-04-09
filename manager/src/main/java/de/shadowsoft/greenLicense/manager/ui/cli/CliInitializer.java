package de.shadowsoft.greenLicense.manager.ui.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "LicenseManager",
        subcommands = {
                CliKeyPair.class,
                CliUi.class,
                CliSoftware.class,
                CliFeature.class,
                CliLicense.class
        }
)
public class CliInitializer {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
}
    
    