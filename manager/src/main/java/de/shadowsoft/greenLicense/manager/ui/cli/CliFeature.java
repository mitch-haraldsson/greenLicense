package de.shadowsoft.greenLicense.manager.ui.cli;


import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.ui.cli.output.CliOutError;
import de.shadowsoft.greenLicense.manager.ui.cli.output.feature.CliOutFeature;
import de.shadowsoft.greenLicense.manager.ui.cli.output.feature.CliOutFeatureCollection;
import de.shadowsoft.greenLicense.manager.ui.cli.tool.ConsoleWriter;
import picocli.CommandLine;

import java.util.Iterator;

@CommandLine.Command(name = "feature",
        description = "Manage software features",
        subcommands = {
                CliFeatureShow.class,
                CliFeatureCreate.class,
                CliFeatureDelete.class
        }
)
public class CliFeature {

}

@CommandLine.Command(name = "show", description = "Show all features")
class CliFeatureShow implements Runnable {

    @CommandLine.Option(names = {"-s", "--software"}, description = "The ID of the software to list features from", required = true)
    private String softwareId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    public CliFeatureShow() {
        softwareId = "";
        useJson = false;
    }

    @Override
    public void run() {
        CliOutFeatureCollection res = new CliOutFeatureCollection();
        String sanitizedSoftwareId = softwareId.toLowerCase().trim();
        boolean hasErrors = false;
        if (sanitizedSoftwareId.length() < 1) {
            hasErrors = true;
            res.setSuccess(false);
            res.addError("Software ID must not be empty");
        }
        try {
            if (!hasErrors) {
                Software software = SoftwareService.getInstance().getSoftwareById(sanitizedSoftwareId);
                if (software != null) {
                    for (Feature feature : software.getFeatures()) {
                        res.getFeatures().add(new CliOutFeature(feature));
                    }
                } else {
                    res.setSuccess(false);
                    res.addError(String.format("Software with ID %s does not exist", softwareId));
                }
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError(String.format("Unable to load features for software %s", softwareId), e));
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "create", description = "Create a software feature")
class CliFeatureCreate implements Runnable {

    @CommandLine.Option(names = {"--default"}, description = "The default value for the feature")
    private String defaultValue;
    @CommandLine.Option(names = {"--id"}, description = "Set an ID instead of generating one, or update an existing feature")
    private String id;
    @CommandLine.Option(names = {"--name"}, description = "A name for the feature", required = true)
    private String name;
    @CommandLine.Option(names = {"-s", "--software"}, description = "The ID of the software to list features from", required = true)
    private String softwareId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    public CliFeatureCreate() {
        defaultValue = "";
        name = "";
        id = "";
        softwareId = "";
        useJson = false;
    }

    @Override
    public void run() {
        CliOutFeatureCollection res = new CliOutFeatureCollection();
        boolean hasError = false;
        try {
            String sanitizedSoftwareId = softwareId.toLowerCase().trim();
            String sanitizedId = id.toLowerCase().trim();
            String sanitizedName = name.trim();
            if (sanitizedName.isEmpty()) {
                hasError = true;
                res.setSuccess(false);
                res.addError("Feature name may not be empty");
            }

            if (sanitizedSoftwareId.length() < 1) {
                hasError = true;
                res.setSuccess(false);
                res.addError("Software ID must not be empty");
            }

            if (!hasError) {
                Software software = SoftwareService.getInstance().getSoftwareById(sanitizedSoftwareId);
                if (software != null) {
                    Feature feature = new Feature();
                    if (!sanitizedId.isEmpty()) {
                        feature.setId(sanitizedId);
                    }
                    feature.setName(sanitizedName);
                    feature.setValue(defaultValue);
                    software.addFeature(feature);
                    SoftwareService.getInstance().save();
                    res.getFeatures().add(new CliOutFeature(feature));
                } else {
                    res.setSuccess(false);
                    res.addError(String.format("Software ID %s does not exist", softwareId));
                }
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.addError("Unable to access software store!", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "delete", description = "Delete software feature")
class CliFeatureDelete implements Runnable {

    @CommandLine.Option(names = {"--id"}, description = "Feature ID to remove", required = true)
    private String id;
    @CommandLine.Option(names = {"-s", "--software"}, description = "The ID of the software to list features from")
    private String softwareId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;


    @Override
    public void run() {
        CliOutFeatureCollection res = new CliOutFeatureCollection();
        try {
            String sanitizedSoftwareId = softwareId.toLowerCase().trim();
            String sanitizedId = id.toLowerCase().trim();
            boolean hasErrors = false;

            if (sanitizedSoftwareId.isEmpty()) {
                hasErrors = true;
                res.setSuccess(false);
                res.addError("Software ID must not be empty!");
            }

            if (sanitizedId.isEmpty()) {
                hasErrors = true;
                res.setSuccess(false);
                res.addError("Feature ID must not be empty!");
            }

            if (!hasErrors) {
                Software software = SoftwareService.getInstance().getSoftwareById(sanitizedSoftwareId);
                if (software != null) {
                    Iterator<Feature> it = software.getFeatures().iterator();
                    boolean isDeleted = false;
                    while (it.hasNext()) {
                        Feature feature = it.next();
                        if (feature.getId().equals(sanitizedId)) {
                            it.remove();
                            SoftwareService.getInstance().save();
                            isDeleted = true;
                            res.getFeatures().add(new CliOutFeature(feature));
                        }
                    }
                    if (!isDeleted) {
                        res.setSuccess(false);
                        res.addError(String.format("Feature ID %s has not been found", id));
                    }
                } else {
                    res.setSuccess(false);
                    res.addError(String.format("Software with ID %s does not exist", softwareId));
                }
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.addError("Unable to access software store!", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}

