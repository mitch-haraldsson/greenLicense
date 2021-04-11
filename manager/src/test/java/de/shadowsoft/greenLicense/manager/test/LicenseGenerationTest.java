package de.shadowsoft.greenLicense.manager.test;

import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReader;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.BasicIdGenerator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdGenerator;
import de.shadowsoft.greenLicense.manager.config.ConfigService;
import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.license.LicenseCreator;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class LicenseGenerationTest {
    private static final Logger LOGGER = LogManager.getLogger(LicenseGenerationTest.class);
    private static final String TEST_DATA_PATH = "./test/";

    public LicenseGenerationTest() {

    }

    private void addFeatures(final Software software, final int amount) {
        LOGGER.info(String.format("Adding %s features", amount));
        for (int i = 0; i < amount; i++) {
            Feature feature = new Feature();
            feature.setName(randomValue(5, 15));
            feature.setValue(randomValue(5, 4096));
            software.addFeature(feature);
        }
    }

    private void changeRandomFeature(final License createdLicense) {
        LOGGER.info(String.format("Changing feature"));
        Feature feature = createdLicense.getSoftware().getFeatures().get(ThreadLocalRandom.current().nextInt(0, createdLicense.getSoftware().getFeatures().size()));
        feature.setValue(randomValue(5, 4096));
    }

    private FssKeyPair createKeyPair(int keySize) throws GeneralSecurityException, IOException, DataLoadingException {
        FssKeyPair keyPair = KeyPairService.getInstance().createKeyPair(randomValue(5, 15), keySize);
        LOGGER.info(String.format("KeyPair created"));
        return keyPair;
    }

    private byte[] createLicense(final License createdLicense) throws DataLoadingException, GeneralSecurityException, NoSuchKeyPairException, InterruptedException, IOException {
        return new LicenseCreator().createLicense(createdLicense);
    }

    private Software createSoftware(final FssKeyPair keyPair) {
        LOGGER.info(String.format("Creating new software"));
        Software software = new Software();
        software.setKeyPairId(keyPair.getId());
        software.setName(randomValue(5, 15));
        software.setVersion("1.0");
        return software;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        Path pathToBeDeleted = directoryToBeDeleted.toPath();
        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to delete directory '%s'", new File(TEST_DATA_PATH)), e);
        }
        return false;
    }

    private String getSystemId(byte selector) throws InterruptedException, IOException {
        final IdGenerator generator = new BasicIdGenerator();
        return new String(Base64.getEncoder().encode(generator.generateId(selector)));
    }

    private String randomValue(int minLength, int maxLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(minLength, maxLength + 1); i++) {
            sb.append((char) ThreadLocalRandom.current().nextInt(97, 123));
        }
        return sb.toString();
    }

    private void readGeneralLicense(GreenLicenseValidator validator, final License licenseConfiguration, final byte[] licenseBytes) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        GreenLicense license = validator.readLicense(licenseBytes);
        assert license.isValid();
        LOGGER.info(String.format("License has %s features", license.getFeature().entrySet().size()));
        for (Feature item : licenseConfiguration.getSoftware().getFeatures()) {
            LOGGER.debug(String.format("%s: %s", item.getId(), license.getFeature().get(item.getId())));
            assert (license.getFeature().get(item.getId()).equals(item.getValue()));
        }
    }

    private void readLicense(GreenLicenseValidator validator, final License licenseConfiguration, final String licenseB64) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        LOGGER.info(String.format("---------------- Reading from base64 ----------------"));
        readGeneralLicense(validator, licenseConfiguration, Base64.getDecoder().decode(licenseB64.getBytes()));
    }

    private void readLicense(GreenLicenseValidator validator, final License licenseConfiguration, final byte[] licenseBytes) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        LOGGER.info(String.format("---------------- Reading from byteArray ----------------"));
        readGeneralLicense(validator, licenseConfiguration, licenseBytes);
    }

    private License setupLicense(final Software software, byte selector) throws IOException, InterruptedException {
        License createdLicense = new License();
        createdLicense.setName(randomValue(5, 15));
        createdLicense.setSoftware(software.clone());
        createdLicense.setSystemId(getSystemId(selector));
        return createdLicense;
    }

    @Test
    @DisplayName("Full license test")
    public void testFullLicense() {
        LOGGER.info(String.format("---- Testing licenses generation & read ----"));
        ConfigService.getInstance().getSettings().setBasePath(TEST_DATA_PATH);
        LOGGER.info(String.format("Creating new KeyPair"));
        try {
            FssKeyPair keyPair = createKeyPair(4096);
            Software software = createSoftware(keyPair);
            addFeatures(software, 5);

            License licenseConfiguration = setupLicense(software, Byte.parseByte("00000000", 2));
            changeRandomFeature(licenseConfiguration);
            LOGGER.info(String.format("License configuration completed"));
            byte[] licenseBytes = createLicense(licenseConfiguration);
            String licenseB64 = new String(Base64.getEncoder().encode(licenseBytes));
            LOGGER.info(String.format("License created: %s", licenseB64));

            GreenLicenseValidator validator = new GreenLicenseReader(keyPair.getKeyPair().getPublic().getEncoded());

            readLicense(validator, licenseConfiguration, licenseBytes);

            readLicense(validator, licenseConfiguration, licenseB64);

        } catch (IOException | GeneralSecurityException | DecryptionException | InterruptedException | DataLoadingException | NoSuchKeyPairException | SystemValidationException | InvalidSignatureException e) {
            LOGGER.error(String.format("Error while creating license"), e);
            assert (false);
        }
        deleteDirectory(new File(TEST_DATA_PATH));
    }
}
    
    