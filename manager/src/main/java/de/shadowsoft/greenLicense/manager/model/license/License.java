package de.shadowsoft.greenLicense.manager.model.license;

import de.shadowsoft.greenLicense.manager.model.software.Software;

import java.util.UUID;

public class License {

    private String id;
    private String name;
    private Software software;
    private String systemId;

    public License() {
        id = UUID.randomUUID().toString();
        systemId = "";
        name = "";
        software = new Software();
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

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(final Software software) {
        this.software = software;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

}
    
    