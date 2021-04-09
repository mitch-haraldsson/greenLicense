package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview;

import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.TMBase;
import javafx.beans.property.SimpleStringProperty;

public class TMFeature extends TMBase {
    private Feature data;
    private SimpleStringProperty defaultValue;
    private SimpleStringProperty id;
    private SimpleStringProperty name;

    public TMFeature(final Feature data) {
        this.data = data;
        id = new SimpleStringProperty(data.getId());
        name = new SimpleStringProperty(data.getName());
        defaultValue = new SimpleStringProperty(data.getValue());
    }

    public SimpleStringProperty defaultValueProperty() {
        return defaultValue;
    }

    public Feature getData() {
        return data;
    }

    public void setData(final Feature data) {
        this.data = data;
    }

    public String getDefaultValue() {
        return defaultValue.get();
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue.set(defaultValue);
    }

    public String getId() {
        return id.get();
    }

    public void setId(final String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
    
    