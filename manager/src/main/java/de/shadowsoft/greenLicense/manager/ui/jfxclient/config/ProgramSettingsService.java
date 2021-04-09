package de.shadowsoft.greenLicense.manager.ui.jfxclient.config;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.ClassCreator;
import de.shadowsoft.greenLicense.manager.tools.serializer.Persistor;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class ProgramSettingsService {
    private static final Logger LOGGER = LogManager.getLogger(ProgramSettingsService.class);
    private static final String SETTINGS_PATH = "./licenseCreatorSettings.json";
    private static ProgramSettingsService instance;

    public static ProgramSettingsService getInstance() {
        if (instance == null) {
            instance = new ProgramSettingsService();
        }
        return instance;
    }
    private ResourceBundle resourceBundle;
    private ProgramSettings settings;

    private ProgramSettingsService() {
        try {
            settings = load();
        } catch (IOException | DataLoadingException e) {
            LOGGER.error("Unable to load settings!", e);
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ProgramSettings getSettings() {
        return settings;
    }

    public void setSettings(final ProgramSettings settings) {
        this.settings = settings;
    }

    public ProgramSettings load() throws IOException, DataLoadingException {
        Persistor persistor = ClassCreator.getPersistor(new File(SETTINGS_PATH));
        return persistor.load(ProgramSettings.class);
    }

    public boolean save() throws IOException {
        return ClassCreator.getPersistor(new File(SETTINGS_PATH)).save(settings);
    }
}
