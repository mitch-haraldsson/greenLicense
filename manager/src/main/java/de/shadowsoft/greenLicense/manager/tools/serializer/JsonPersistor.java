package de.shadowsoft.greenLicense.manager.tools.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonPersistor implements Persistor {

    private File file;

    public JsonPersistor(final File file) {
        this.file = file;
    }

    private void createDummyFile() throws IOException {
        createPath(file);
        new FileOutputStream(file).close();
    }

    private void createPath(final File file) {
        file.getParentFile().mkdirs();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T load(final Class<T> clazz) throws IOException, DataLoadingException {
        try {
            T res = clazz.getDeclaredConstructor().newInstance();
            if (file.exists()) {
                if (file.canRead()) {
                    return (T) new Gson().fromJson(
                            Files.readString(Paths.get(file.toURI())),
                            res.getClass()
                    );
                }
            } else {
                createDummyFile();
                save(res);
                return res;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DataLoadingException(String.format("Unable to serialize JSON to %s", clazz.getCanonicalName()));
        }
        throw new IOException(String.format("Unable to read from file %s", file.getAbsolutePath()));
    }

    @Override
    public boolean save(final Object data) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            createDummyFile();
        }

        if (file.canWrite()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(data);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(json);
            }
            return true;
        } else {
            throw new IOException(String.format("Unable to write to file %s", file.getAbsolutePath()));
        }
    }
}
    
    