package de.shadowsoft.greenLicense.manager.model.software;

import java.util.ArrayList;
import java.util.List;

public class SoftwareData {

    private List<Software> software;

    public SoftwareData() {
        software = new ArrayList<>();
    }

    public List<Software> getSoftware() {
        return software;
    }

    public void setSoftware(final List<Software> software) {
        this.software = software;
    }
}
    
    