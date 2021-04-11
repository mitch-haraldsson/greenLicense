package de.shadowsoft.greenLicense.manager.ui.cli;


import de.shadowsoft.greenLicense.common.license.generator.core.IdCreator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdResult;
import de.shadowsoft.greenLicense.core.cli.CliOutError;
import de.shadowsoft.greenLicense.core.cli.tool.ConsoleWriter;
import de.shadowsoft.greenLicense.manager.license.LicenseCreator;
import de.shadowsoft.greenLicense.manager.license.LicenseCreatorBase;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.model.license.LicenseService;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.ui.cli.output.export.CliOutExport;
import de.shadowsoft.greenLicense.manager.ui.cli.output.export.CliOutExportCollection;
import de.shadowsoft.greenLicense.manager.ui.cli.output.license.CliOutLicense;
import de.shadowsoft.greenLicense.manager.ui.cli.output.license.CliOutLicenseCollection;
import picocli.CommandLine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@CommandLine.Command(name = "license",
        description = "Manage licenses",
        subcommands = {
                CliLicenseShow.class,
                CliLicenseCreate.class,
                CliLicenseDelete.class,
                CliLicenseExport.class

        })
public class CliLicense {

}

@CommandLine.Command(name = "show", description = "Show created licenses")
class CliLicenseShow implements Runnable {

    @CommandLine.Option(names = {"--filter"}, description = "Show only licenses containing string in name")
    private String contains;
    @CommandLine.Option(names = {"--software"}, description = "Show only licenses for this software ID")
    private String softwareId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    public CliLicenseShow() {
        contains = "";
        softwareId = "";
        useJson = false;
    }

    private void addIfFilterMatch(String filter, CliOutLicenseCollection res, License license) {
        if (!filter.isEmpty()) {
            if (license.getName().toLowerCase().contains(filter.toLowerCase())) {
                res.getLicenses().add(new CliOutLicense(license));
            }
        } else {
            res.getLicenses().add(new CliOutLicense(license));
        }
    }

    @Override
    public void run() {
        CliOutLicenseCollection res = new CliOutLicenseCollection();
        try {
            List<License> licenses = LicenseService.getInstance().getAllLicenses();
            String sanitizedSoftwareId = softwareId.toLowerCase().trim();
            String sanitizedFilter = contains.toLowerCase();
            for (License license : licenses) {
                if (!softwareId.isEmpty()) {
                    if (license.getSoftware().getId().equals(sanitizedSoftwareId)) {
                        addIfFilterMatch(sanitizedFilter, res, license);
                    }
                } else {
                    addIfFilterMatch(sanitizedFilter, res, license);
                }
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.addError("Unable to access license store", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "create", description = "Create a license")
class CliLicenseCreate implements Runnable {

    @CommandLine.Option(names = {"--binding"}, description = "License binding for V2 license")
    private String binding;
    @CommandLine.Option(names = {"--feature"}, split = ",", description = "<feature id>=<value> - Can be defined multiple times to set a feature value different from software default")
    private Map<String, String> featureSet;
    @CommandLine.Option(names = {"--name"}, description = "Name of the license (i. E. Customer/Date)", required = true)
    private String name;
    @CommandLine.Option(names = {"--software"}, description = "Software to create license for", required = true)
    private String softwareId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutLicenseCollection res = new CliOutLicenseCollection();
        try {
            boolean hasErrors = false;
            String sanitizedName = name.trim();
            String sanitizedSoftwareId = softwareId.toLowerCase().trim();

            if (sanitizedName.isEmpty()) {
                hasErrors = true;
                res.setSuccess(false);
                res.addError("Software name must not be empty!");
            }

            if (sanitizedSoftwareId.isEmpty()) {
                hasErrors = true;
                res.setSuccess(false);
                res.addError("A license can not be issued without a software ID");
            }

            if (!hasErrors) {
                Software software = SoftwareService.getInstance().getSoftwareById(sanitizedSoftwareId);
                if (software != null) {
                    License license = new License();
                    license.setName(sanitizedName);
                    license.setSoftware(software.clone());
                    if (binding != null && binding.length() > 0) {
                        license.setSystemId(binding);
                    } else {
                        IdResult idResult = new IdResult();
                        idResult.setSelector(Byte.parseByte("00000000", 2));
                        license.setSystemId(Base64.getEncoder().encodeToString(new IdCreator().createId(idResult)));
                    }
                    if (featureSet != null) {
                        for (Map.Entry<String, String> featureGroup : featureSet.entrySet()) {
                            for (Feature feature : license.getSoftware().getFeatures()) {
                                if (feature.getId().equals(featureGroup.getKey().toLowerCase())) {
                                    feature.setValue(featureGroup.getValue());
                                }
                            }
                        }
                    }
                    LicenseService.getInstance().addLicense(license);
                    res.getLicenses().add(new CliOutLicense(license));
                } else {
                    res.setSuccess(false);
                    res.addError(String.format("Software id %s is invalid", softwareId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setSuccess(false);
            res.addError("Error while accessing data store!", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "delete", description = "Delete a license")
class CliLicenseDelete implements Runnable {

    @CommandLine.Option(names = {"-f", "--force"}, description = "Force delete, do not ask for confirmation")
    private boolean force;
    @CommandLine.Option(names = {"--id"}, description = "License ID to delete", required = true)
    private String id;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutLicenseCollection res = new CliOutLicenseCollection();
        try {
            boolean proceed = false;
            if (!force) {
                String s = System.console().readLine("Are you sure you want to delete the license? y/n: ");
                String in = s.toLowerCase().trim();
                if (in.equals("y") || in.equals("yes")) {
                    proceed = true;
                }
            } else {
                proceed = true;
            }

            if (proceed) {
                String sanitizedId = id.toLowerCase().trim();
                LicenseService service = LicenseService.getInstance();
                License license = service.getLicenseById(sanitizedId);
                service.remove(license);
                res.getLicenses().add(new CliOutLicense(license));
            } else {
                res.setSuccess(false);
                res.getErrorMessages().add(new CliOutError("User canceled! License has not been removed"));
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.getErrorMessages().add(new CliOutError(String.format("Unable to remove license %s", id), e));
        }
        ConsoleWriter.print(res, useJson);
    }
}

@CommandLine.Command(name = "export", description = "Export a license")
class CliLicenseExport implements Runnable {

    @CommandLine.Option(names = {"--file"}, description = "Export file path")
    private String exportPath;
    @CommandLine.Option(names = {"--license"}, description = "License ID to export", required = true)
    private String licenseId;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;

    @Override
    public void run() {
        CliOutExportCollection res = new CliOutExportCollection();
        try {
            String sanitizedLicenseId = licenseId.toLowerCase().trim();
            boolean hasErrors = false;
            if (sanitizedLicenseId.isEmpty()) {
                hasErrors = true;
                res.setSuccess(false);
                res.addError("Software to export must be specified");
            }

            License license = LicenseService.getInstance().getLicenseById(sanitizedLicenseId);

            if (license != null) {
                if (!hasErrors) {
                    LicenseCreatorBase creator = new LicenseCreator();
                    byte[] licenseBytes = creator.createLicense(license);
                    String fileExportInfo = "No export to file";
                    if (!exportPath.isEmpty()) {
                        File file = new File(exportPath);
                        file.getParentFile().mkdirs();
                        try (DataOutputStream os = new DataOutputStream(new FileOutputStream(file))) {
                            os.write(licenseBytes);
                            fileExportInfo = file.getAbsolutePath();
                        } catch (Exception e) {
                            res.setSuccess(false);
                            res.addError("Unable to write export file", e);
                        }
                    }
                    CliOutExport export = new CliOutExport(license);
                    export.setFilePath(fileExportInfo);
                    export.setBase64License(new String(Base64.getEncoder().encode(licenseBytes)));
                    res.getExports().add(export);
                }
            } else {
                res.setSuccess(false);
                res.addError(String.format("Can not find license with ID %s", licenseId));
            }
        } catch (Exception e) {
            res.setSuccess(false);
            res.addError("Unable to create license!", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}