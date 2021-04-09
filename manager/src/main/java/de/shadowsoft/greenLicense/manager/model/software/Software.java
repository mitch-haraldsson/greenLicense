package de.shadowsoft.greenLicense.manager.model.software;

import de.shadowsoft.greenLicense.common.license.LicenseVersion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Software {
    private List<Feature> features;
    private String id;
    private String keyPairId;
    private LicenseVersion licenseVersion;
    private String name;
    private String version;

    public Software() {
        features = new ArrayList<>();
        id = UUID.randomUUID().toString();
        keyPairId = "";
        name = "";
        version = "";
        licenseVersion = LicenseVersion.LICENSE_V1;
    }

    public void addFeature(Feature feature) {
        removeFeature(feature);
        features.add(feature);
    }

    @Override
    public Software clone() {
        Software software = new Software();
        software.setId(id);
        software.setKeyPairId(keyPairId);
        software.setName(name);
        software.setVersion(version);
        software.setLicenseVersion(licenseVersion);
        for (Feature feature : features) {
            software.getFeatures().add(feature.clone());
        }
        return software;
    }

    @Override
    public String toString() {
        return name + " (" + version + ")";
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(final List<Feature> features) {
        this.features = features;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getKeyPairId() {
        return keyPairId;
    }

    public void setKeyPairId(final String keyPairId) {
        this.keyPairId = keyPairId;
    }

    public LicenseVersion getLicenseVersion() {
        return licenseVersion;
    }

    public void setLicenseVersion(final LicenseVersion licenseVersion) {
        this.licenseVersion = licenseVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public void removeFeature(String id) {
        Iterator<Feature> it = features.iterator();
        while (it.hasNext()) {
            Feature item = it.next();
            if (item.getId().equals(id)) {
                it.remove();
            }
        }
    }

    public void removeFeature(Feature feature) {
        removeFeature(feature.getId());
    }
}
    
    