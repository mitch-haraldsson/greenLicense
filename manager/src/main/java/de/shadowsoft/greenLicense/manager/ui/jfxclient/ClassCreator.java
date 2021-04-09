package de.shadowsoft.greenLicense.manager.ui.jfxclient;

import de.shadowsoft.greenLicense.manager.tools.serializer.JsonPersistor;
import de.shadowsoft.greenLicense.manager.tools.serializer.Persistor;

import java.io.File;

public class ClassCreator {

    public static Persistor getPersistor(File file) {
        return new JsonPersistor(file);
    }

    public ClassCreator() {
    }

}
    
    