package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview;

import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.TMBase;
import javafx.beans.property.SimpleStringProperty;

public class TMLicenses extends TMBase {

    private final Feature feature;
    private SimpleStringProperty name;
    private SimpleStringProperty value;

    public TMLicenses(Feature feature) {
        this.feature = feature;
        name = new SimpleStringProperty(feature.getName());
        value = new SimpleStringProperty(feature.getValue());
    }

    public Feature getFeature() {
        return feature;
    }

    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(final String value) {
        feature.setValue(value);
        this.value.set(value);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }
}
    
    