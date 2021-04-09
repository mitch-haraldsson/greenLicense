package de.shadowsoft.greenLicense.manager.model.license;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;

public enum LicenseVersion {
    LICENSE_V1, LICENSE_V2;

    public String toShortString() {
        switch (this) {
            case LICENSE_V1:
                return "v1";

            case LICENSE_V2:
                return "v2";
        }
        return "N/A";
    }

    @Override
    public String toString() {
        switch (this) {
            case LICENSE_V1:
                return ProgramSettingsService.getInstance().getResourceBundle().getString("licenseversion.v1");

            case LICENSE_V2:
                return ProgramSettingsService.getInstance().getResourceBundle().getString("licenseversion.v2");
        }
        return ProgramSettingsService.getInstance().getResourceBundle().getString("licenseversion.undefined");
    }
}
