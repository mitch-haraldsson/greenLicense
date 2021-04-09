package de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.l18n.LanguageManager;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.StageInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class LicenseManagerStageInitializer extends StageInitializer {
    private static final Logger logger = LogManager.getLogger(LicenseManagerStageInitializer.class);

    public LicenseManagerStageInitializer(String name, String stageKey, Controller controller, boolean modal) {
        super(name, stageKey, controller, modal);
    }

    public LicenseManagerStageInitializer(String name, String stageKey, Controller controller, boolean modal, Locale locale) {
        super(name, stageKey, controller, modal, locale);
    }

    public ResourceBundle getResources() {
        return LanguageManager.getInstance().getResources();
    }

}