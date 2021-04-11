package de.shadowsoft.greenLicense.manager.ui.cli;

import de.shadowsoft.greenLicense.common.cli.CliOutError;
import de.shadowsoft.greenLicense.common.cli.tool.ConsoleWriter;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.cli.output.key.CliOutKeyPair;
import de.shadowsoft.greenLicense.manager.ui.cli.output.key.CliOutKeyPairCollection;
import picocli.CommandLine;

import java.io.IOException;
import java.security.GeneralSecurityException;

@CommandLine.Command(name = "keypair",
        description = "Manage key pairs",
        subcommands = {
                CliKeyPairCreate.class,
                CliKeyPairShow.class,
                CliKeyPairDelete.class
        }
)
public class CliKeyPair {

}

@CommandLine.Command(name = "show", description = "Show all key pairs")
class CliKeyPairShow implements Runnable {

    @CommandLine.Option(names = {"--id"}, description = "Show key with this ID only")
    private String id;
    @CommandLine.Option(names = {"-b", "--bytes"}, description = "Show byte output to include in code. Only available if --id is set")
    private boolean showBytes;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;


    public CliKeyPairShow() {
        useJson = false;
        showBytes = false;
        id = "";
    }

    @Override
    public void run() {
        CliOutKeyPairCollection res = new CliOutKeyPairCollection();
        try {
            for (FssKeyPair keyPair : KeyPairService.getInstance().getAllKeyPairs()) {
                if (id.length() == 0 || id.equals(keyPair.getId())) {
                    CliOutKeyPair pair = new CliOutKeyPair(keyPair);
                    if (!showBytes || id.length() == 0) {
                        pair.setPublicKeyBytes("");
                    }
                    res.getKeyPairs().add(pair);
                }
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError("Unable to load key pairs!", e));
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "create", description = "Create a new key pair")
class CliKeyPairCreate implements Runnable {
    @CommandLine.Option(names = {"-n", "--name"}, description = "A descriptive name for the key", required = true)
    private String name;
    @CommandLine.Option(names = {"-s", "--size"}, description = "The size of the key (1024, 2048, 4096, 8192)", required = true)
    private int size;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    private boolean isPowerOf2(int number) {
        return number > 0 && ((number & (number - 1)) == 0);
    }

    @Override
    public void run() {
        CliOutKeyPairCollection outRes = new CliOutKeyPairCollection();
        if (size > 1023 && size < 8193) {
            if (isPowerOf2(size)) {
                String sanitizedName = name.trim();
                if (sanitizedName.length() > 0) {
                    try {
                        FssKeyPair keyPair = KeyPairService.getInstance().createKeyPair(name, size);
                        outRes.getKeyPairs().add(new CliOutKeyPair(keyPair));
                    } catch (GeneralSecurityException | IOException | DataLoadingException e) {
                        outRes.getErrorMessages().add(new CliOutError("Unable to create new key!", e));
                    }
                } else {
                    outRes.getErrorMessages().add(new CliOutError("Name must not be empty"));
                }
            } else {
                outRes.getErrorMessages().add(new CliOutError(String.format("Size (%s) must be a power of 2", size)));
            }
        } else {
            outRes.getErrorMessages().add(new CliOutError(String.format("Size (%s) must be between 1024 and 8192", size)));
        }
        ConsoleWriter.print(outRes, useJson);
    }
}

@CommandLine.Command(name = "delete", description = "Delete a key pair")
class CliKeyPairDelete implements Runnable {
    @CommandLine.Option(names = {"-f", "--force"}, description = "Force delete, do not ask for confirmation")
    private boolean force;
    @CommandLine.Option(names = {"--id"}, required = true, description = "ID of the key pair to be deleted")
    private String id;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutKeyPairCollection res = new CliOutKeyPairCollection();
        try {
            boolean proceed = false;
            if (!force) {
                String s = System.console().readLine("If you remove the key you won't be able to create any more licenses for software using that key!\n" +
                        "Are you sure you want to delete it? y/n: ");
                String in = s.toLowerCase().trim();
                if (in.equals("y") || in.equals("yes")) {
                    proceed = true;
                }
            } else {
                proceed = true;
            }

            if (proceed) {
                String sanitizedId = id.toLowerCase().trim();
                KeyPairService kpService = KeyPairService.getInstance();
                FssKeyPair keyPair = kpService.getKeyById(sanitizedId);
                kpService.removeKeyPair(keyPair);
                res.getKeyPairs().add(new CliOutKeyPair(keyPair));
            } else {
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError("User canceled! Key has not been removed"));
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError(String.format("Unable to remove key %s", id), e));
        }
        ConsoleWriter.print(res, useJson);
    }
}