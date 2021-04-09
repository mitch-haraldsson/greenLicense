package de.shadowsoft.greenLicense.manager.tools.serializer;

import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.IOException;

public interface Persistor {

    <T> T load(final Class<T> clazz) throws IOException, DataLoadingException;

    boolean save(Object data) throws IOException;

}
