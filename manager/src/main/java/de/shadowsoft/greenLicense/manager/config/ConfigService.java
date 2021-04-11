package de.shadowsoft.greenLicense.manager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ConfigService {
    private static final String CONF_FILE = "./config/greenLicenseManager.json";
    private static final Logger LOGGER = LogManager.getLogger(ConfigService.class);
    private static ConfigService INSTANCE;

    public static ConfigService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigService();
        }
        return INSTANCE;
    }

    private ProgramSettings settings;


    private ConfigService() {
        reloadConfigFile();
    }

    private File getConfigFile() {
        return new File(CONF_FILE);
    }

    public ProgramSettings getSettings() {
        return settings;
    }

    public void reloadConfigFile() {
        settings = new ProgramSettings();
        File confFile = getConfigFile();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            if (confFile.exists()) {
                try (Reader reader = new FileReader(confFile)) {
                    settings = gson.fromJson(reader, ProgramSettings.class);
                }
            } else {
                saveConfigFile();
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to load config file: %s", confFile.getAbsolutePath()), e);
        }
    }

    public void saveConfigFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File confFile = getConfigFile();
        try {
            confFile.getParentFile().mkdirs();
            String json = gson.toJson(settings);
            try (Writer writer = new FileWriter(confFile)) {
                writer.write(json);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to write config file: %s", confFile.getAbsolutePath()), e);
        }
    }
}
