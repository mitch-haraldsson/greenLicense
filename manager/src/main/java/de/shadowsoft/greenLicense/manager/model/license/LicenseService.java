package de.shadowsoft.greenLicense.manager.model.license;

import de.shadowsoft.greenLicense.manager.config.ConfigService;
import de.shadowsoft.greenLicense.manager.tools.serializer.Persistor;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.ClassCreator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LicenseService {
    private static final String SAVE_PATH = "data/licenses.json";
    private static LicenseService instance;

    public static LicenseService getInstance() throws IOException, DataLoadingException {
        if (instance == null) {
            instance = new LicenseService();
        }
        return instance;
    }

    private LicenseData data;

    private LicenseService() throws IOException, DataLoadingException {
        reload();

    }

    public boolean addLicense(final License license) throws IOException {
        data.getLicenses().add(license);
        return save();
    }

    public List<License> getAllLicenses() {
        return data.getLicenses();
    }

    public LicenseData getData() {
        return data;
    }

    public void setData(final LicenseData data) {
        this.data = data;
    }

    public License getLicenseById(String id) {
        for (License license : getAllLicenses()) {
            if (license.getId().equals(id)) {
                return license;
            }
        }
        return null;
    }

    private File getLicenseFile() {
        return new File(ConfigService.getInstance().getSettings().getBasePath() + SAVE_PATH);
    }

    public void reload() throws IOException, DataLoadingException {
        Persistor persistor = ClassCreator.getPersistor(getLicenseFile());
        data = persistor.load(LicenseData.class);
    }

    public boolean remove(License license) throws IOException {
        return remove(license.getId());
    }

    public boolean remove(final String id) throws IOException {
        data.getLicenses().removeIf(license -> license.getId().equals(id));
        return save();
    }

    public boolean save() throws IOException {
        Persistor persistor = ClassCreator.getPersistor(getLicenseFile());
        return persistor.save(data);
    }

}
