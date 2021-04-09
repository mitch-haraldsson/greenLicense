package de.shadowsoft.greenLicense.manager.config;

import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairData;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.LicenseData;
import de.shadowsoft.greenLicense.manager.model.license.LicenseService;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareData;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.IOException;

public class ProgramData {

    private KeyPairData keyPairs;
    private LicenseData licenses;
    private SoftwareData software;

    public ProgramData() {
        reload();
    }

    public KeyPairData getKeyPairs() {
        return keyPairs;
    }

    public void setKeyPairs(final KeyPairData keyPairs) {
        this.keyPairs = keyPairs;
    }

    public LicenseData getLicenses() {
        return licenses;
    }

    public void setLicenses(final LicenseData licenses) {
        this.licenses = licenses;
    }

    public SoftwareData getSoftware() {
        return software;
    }

    public void setSoftware(final SoftwareData software) {
        this.software = software;
    }

    public void reload() {
        try {
            keyPairs = KeyPairService.getInstance().getData();
            licenses = LicenseService.getInstance().getData();
            software = SoftwareService.getInstance().getData();
        } catch (IOException | DataLoadingException e) {
            e.printStackTrace();
        }
    }
}
    
    