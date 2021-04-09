package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview;

import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.TMBase;
import javafx.beans.property.SimpleStringProperty;

public class TMIssuedLicenses extends TMBase {


    private final License license;
    private SimpleStringProperty name;
    private SimpleStringProperty softwareName;
    private SimpleStringProperty softwareVersion;


    public TMIssuedLicenses(License license) {
        this.license = license;
        name = new SimpleStringProperty(license.getName());
        softwareName = new SimpleStringProperty(license.getSoftware().getName());
        softwareVersion = new SimpleStringProperty(license.getSoftware().getVersion());
    }

    public License getLicense() {
        return license;
    }


    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public String getSoftwareName() {
        return softwareName.get();
    }

    public void setSoftwareName(final String softwareName) {
        this.softwareName.set(softwareName);
    }

    public String getSoftwareVersion() {
        return softwareVersion.get();
    }

    public void setSoftwareVersion(final String softwareVersion) {
        this.softwareVersion.set(softwareVersion);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty softwareNameProperty() {
        return softwareName;
    }

    public SimpleStringProperty softwareVersionProperty() {
        return softwareVersion;
    }
}
    
    