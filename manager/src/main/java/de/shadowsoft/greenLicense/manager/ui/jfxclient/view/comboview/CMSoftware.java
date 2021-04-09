package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.comboview;

import de.shadowsoft.greenLicense.manager.model.software.Software;

public class CMSoftware {
    private Software data;

    public CMSoftware(final Software data) {
        this.data = data;
    }

    public Software getData() {
        return data;
    }

    public void setData(final Software data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
    
    