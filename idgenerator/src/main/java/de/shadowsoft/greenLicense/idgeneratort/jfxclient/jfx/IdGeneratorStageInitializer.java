package de.shadowsoft.greenLicense.idgeneratort.jfxclient.jfx;

import de.shadowsoft.greenLicense.idgeneratort.jfxclient.config.LanguageManager;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.Controller;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.StageInitializer;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class IdGeneratorStageInitializer extends StageInitializer {

    public IdGeneratorStageInitializer(final String name, final String stageKey, final Controller controller, final boolean modal) {
        super(name, stageKey, controller, modal);
    }

    public IdGeneratorStageInitializer(final String name, final String stageKey, final Controller controller, final boolean modal, final String language, final String country) {
        super(name, stageKey, controller, modal, language, country);
    }

    public IdGeneratorStageInitializer(final String name, final String stageKey, final Controller controller, final boolean modal, final Locale locale) {
        super(name, stageKey, controller, modal, locale);
    }

    public ResourceBundle getResources() {
        return LanguageManager.getInstance().getResources();
    }
}
    
    