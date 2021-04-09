package de.shadowsoft.greenLicense.common.license;

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
}
