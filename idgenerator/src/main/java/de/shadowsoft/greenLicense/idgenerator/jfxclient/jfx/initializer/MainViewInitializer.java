package de.shadowsoft.greenLicense.idgenerator.jfxclient.jfx.initializer;

import de.shadowsoft.greenLicense.idgenerator.jfxclient.config.ProgramSettings;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.jfx.IdGeneratorStageInitializer;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.jfx.JfxResource;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;

import java.util.Locale;

public class MainViewInitializer extends IdGeneratorStageInitializer {

    public MainViewInitializer(final Controller controller) {
        super(JfxResource.MAIN_VIEW, JfxResource.MAIN_VIEW, controller, false);
    }

    public MainViewInitializer(final String name, final String stageKey, final Controller controller, final boolean modal) {
        super(name, stageKey, controller, modal);
    }

    public MainViewInitializer(final String name, final String stageKey, final Controller controller, final boolean modal, final String language, final String country) {
        super(name, stageKey, controller, modal, language, country);
    }

    public MainViewInitializer(final String name, final String stageKey, final Controller controller, final boolean modal, final Locale locale) {
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
        getStage().setTitle(ProgramSettings.getInstance().getProgramTitleBarPrefix()
                + getResources().getString("ui.main.title"));
        getStage().setResizable(false);
//        getStage().initStyle(StageStyle.UNDECORATED);
    }
}
    
    