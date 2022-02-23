package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.TMBase;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class TMSoftware extends TMBase {

    private Software data;
    private SimpleStringProperty keyPair;
    private SimpleStringProperty name;
    private SimpleStringProperty version;

    public TMSoftware(final Software data) throws IOException, DataLoadingException {
        this.data = data;
        name = new SimpleStringProperty(data.getName());
        version = new SimpleStringProperty(data.getVersion());
        FssKeyPair kp = KeyPairService.getInstance().getKeyById(data.getKeyPairId());
        if (kp != null) {
            keyPair = new SimpleStringProperty(kp.toString());
        }
    }

    public Software getData() {
        return data;
    }

    public void setData(final Software data) {
        this.data = data;
    }

    public String getKeyPair() {
        return keyPair.get();
    }

    public void setKeyPair(final String keyPair) {
        this.keyPair.set(keyPair);
    }

    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(final String version) {
        this.version.set(version);
    }

    public SimpleStringProperty keyPairProperty() {
        return keyPair;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty versionProperty() {
        return version;
    }
}
    
    