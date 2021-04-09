package de.shadowsoft.greenLicense.manager.ui.cli.output.feature;

import de.shadowsoft.greenLicense.manager.model.software.Feature;

public class CliOutFeature {

    private String id;
    private String name;
    private String value;

    public CliOutFeature(Feature feature) {
        this.id = feature.getId();
        this.name = feature.getName();
        this.value = feature.getValue();
    }

    public CliOutFeature() {
        id = "";
        name = "";
        value = "";
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
    
    