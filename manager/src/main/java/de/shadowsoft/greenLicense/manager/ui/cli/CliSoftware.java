package de.shadowsoft.greenLicense.manager.ui.cli;

import de.shadowsoft.greenLicense.common.cli.CliOutError;
import de.shadowsoft.greenLicense.common.cli.tool.ConsoleWriter;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.ui.cli.output.software.CliOutSoftware;
import de.shadowsoft.greenLicense.manager.ui.cli.output.software.CliOutSoftwareCollection;
import picocli.CommandLine;

@CommandLine.Command(name = "software",
        description = "Manage software",
        subcommands = {
                CliSoftwareShow.class,
                CliSoftwareCreate.class,
                CliSoftwareDelete.class
        }
)
public class CliSoftware {

}

@CommandLine.Command(name = "show", description = "Show all software")
class CliSoftwareShow implements Runnable {

    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutSoftwareCollection res = new CliOutSoftwareCollection();
        try {
            for (Software software : SoftwareService.getInstance().getAllSoftware()) {
                res.getSoftware().add(new CliOutSoftware(software));
            }
        } catch (Exception e) {
            res.getErrorMessages().add(new CliOutError("Unable to read from software database!", e));
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "delete", description = "Remove software")
class CliSoftwareDelete implements Runnable {
    @CommandLine.Option(names = {"-f", "--force"}, description = "Force delete, do not ask for confirmation")
    private boolean force;
    @CommandLine.Option(names = {"--id"}, required = true, description = "ID of the software to be deleted")
    private String id;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutSoftwareCollection res = new CliOutSoftwareCollection();
        try {
            boolean proceed = false;
            if (!force) {
                String s = System.console().readLine("Are you sure you want to delete the software? y/n: ");
                String in = s.toLowerCase().trim();
                if (in.equals("y") || in.equals("yes")) {
                    proceed = true;
                }
            } else {
                proceed = true;
            }

            if (proceed) {
                String sanitizedId = id.toLowerCase().trim();
                SoftwareService softwareService = SoftwareService.getInstance();
                Software software = softwareService.getSoftwareById(sanitizedId);
                if (software != null) {
                    res.getSoftware().add(new CliOutSoftware(software));
                } else {
                    res.getErrorMessages().add(new CliOutError(String.format("Software key %s does not exist", id)));
                }
            } else {
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError("User canceled! Software has not been removed"));
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError(String.format("Unable to remove software '%s'", id), e));
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "create", description = "Create new software entry")
class CliSoftwareCreate implements Runnable {

    @CommandLine.Option(names = {"--key"}, description = "Key ID of the key pair to associate with the software", required = true)
    private String key;
    @CommandLine.Option(names = {"--name"}, description = "A name for yhe software to license", required = true)
    private String name;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;
    @CommandLine.Option(names = {"--version"}, description = "Version of the license you want to use", required = true)
    private String version;

    public CliSoftwareCreate() {
        key = "";
        name = "";
        version = "";
    }

    @Override
    public void run() {
        CliOutSoftwareCollection res = new CliOutSoftwareCollection();
        try {
            String sanitizedName = name.trim();
            String sanitizedVersion = version.trim();
            FssKeyPair keyPair = KeyPairService.getInstance().getKeyById(key);
            boolean hasError = false;

            if (keyPair == null) {
                hasError = true;
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError(String.format("Key %s does not exist", key)));
            }

            if (sanitizedName.length() < 1) {
                hasError = true;
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError("Software name may not be empty"));
            }

            if (sanitizedVersion.length() < 1) {
                hasError = true;
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError("Software version name may not be empty"));
            }

            if (!hasError) {
                Software software = new Software();
                software.setKeyPairId(keyPair.getId());
                software.setName(sanitizedName);
                software.setVersion(sanitizedVersion);
                SoftwareService.getInstance().addSoftware(software);
                res.getSoftware().add(new CliOutSoftware(software));
            }

        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError("Unable to access software store"));
        }
        ConsoleWriter.print(res, useJson);
    }
}


    