package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.TMBase;
import javafx.beans.property.SimpleStringProperty;

public class TMKeyPair extends TMBase {

    private FssKeyPair data;
    private SimpleStringProperty name;
    private SimpleStringProperty size;

    public TMKeyPair(final FssKeyPair data) {
        name = new SimpleStringProperty(data.getName());
        size = new SimpleStringProperty(String.valueOf(data.getSize()));
        this.data = data;
    }

    public FssKeyPair getData() {
        return data;
    }

    public void setData(final FssKeyPair data) {
        this.data = data;
    }

    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public String getSize() {
        return size.get();
    }

    public void setSize(final String size) {
        this.size.set(size);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }
}
    
    