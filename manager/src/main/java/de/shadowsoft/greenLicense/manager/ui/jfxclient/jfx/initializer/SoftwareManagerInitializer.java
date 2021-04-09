package de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.initializer;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.JfxResource;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.LicenseManagerStageInitializer;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.StageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class SoftwareManagerInitializer extends LicenseManagerStageInitializer {
    private static final Logger LOGGER = LogManager.getLogger(KeyManagerInitializer.class);

    public SoftwareManagerInitializer(final Controller controller) {
        super(JfxResource.SOFTWARE_MANAGER, JfxResource.SOFTWARE_MANAGER, controller, true);
    }

    public SoftwareManagerInitializer(final String name, final String stageKey, final Controller controller, boolean modal, final Locale locale) {
        super(name, stageKey, controller, modal, locale);
    }

    @Override
    public void loadComponents() {

    }

    @Override
    public void postThreadProcessing(final String s) {

    }

    @Override
    public void stageInit() {
        getStage().setTitle(ProgramSettingsService.getInstance().getSettings().getProgramTitleBarPrefix()
                + getResources().getString("ui.softwaremanager.title"));

        getStage().setOnCloseRequest(we -> {
            LOGGER.info("Closing stage " + getStageKey());
            StageManager.getInstance().killStage(getStageKey());
        });
    }
}
    
    