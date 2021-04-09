package de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.initializer;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.JfxResource;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.LicenseManagerStageInitializer;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;

import java.util.Locale;

public class MainViewInitializer extends LicenseManagerStageInitializer {

    public MainViewInitializer(final Controller controller) {
        super(JfxResource.MAIN_VIEW, JfxResource.MAIN_VIEW, controller, false);
    }

    public MainViewInitializer(final String name, final String stageKey, final Controller controller, boolean modal, final Locale locale) {
        super(name, stageKey, controller, modal, locale);
    }

    public void loadComponents() {

    }

    public void postThreadProcessing(final String s) {

    }

    public void stageInit() {
        getStage().setTitle(ProgramSettingsService.getInstance().getSettings().getProgramTitleBarPrefix()
                + getResources().getString("ui.main.title"));
    }
}
    
    