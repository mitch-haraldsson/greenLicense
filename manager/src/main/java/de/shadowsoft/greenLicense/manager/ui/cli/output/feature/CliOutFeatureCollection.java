package de.shadowsoft.greenLicense.manager.ui.cli.output.feature;

import de.shadowsoft.greenLicense.common.cli.CliOutBase;

import java.util.ArrayList;
import java.util.List;

public class CliOutFeatureCollection extends CliOutBase {

    private List<CliOutFeature> features;

    public CliOutFeatureCollection() {
        features = new ArrayList<>();
    }

    @Override
    public String formatOutput(final StringBuilder res) {
        for (CliOutFeature feature : features) {
            res.append("\n").append(feature.getId()).append(":");
            res.append(feature.getName()).append(":");
            res.append(feature.getValue());
        }
        return res.toString();
    }

    public List<CliOutFeature> getFeatures() {
        return features;
    }

    public void setFeatures(final List<CliOutFeature> features) {
        this.features = features;
    }
}
    
    