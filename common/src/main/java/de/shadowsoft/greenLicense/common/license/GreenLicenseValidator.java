package de.shadowsoft.greenLicense.common.license;

import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;

import java.io.IOException;

public interface GreenLicenseValidator {

    GreenLicense readLicense(byte[] content) throws DecryptionException, SystemValidationException, InvalidSignatureException;

    GreenLicense readLicenseFromFile(String path) throws IOException, DecryptionException, SystemValidationException, InvalidSignatureException;

}
