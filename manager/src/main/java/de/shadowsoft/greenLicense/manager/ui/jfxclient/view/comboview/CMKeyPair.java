package de.shadowsoft.greenLicense.manager.ui.jfxclient.view.comboview;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;

public class CMKeyPair {

    private FssKeyPair data;

    public CMKeyPair(final FssKeyPair data) {
        this.data = data;
    }

    public FssKeyPair getData() {
        return data;
    }

    public void setData(final FssKeyPair data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
    
    