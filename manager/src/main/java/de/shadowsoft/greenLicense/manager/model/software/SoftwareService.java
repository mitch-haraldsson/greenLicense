package de.shadowsoft.greenLicense.manager.model.software;

import de.shadowsoft.greenLicense.manager.tools.serializer.Persistor;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.ClassCreator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SoftwareService {
    private static final String SAVE_PATH = "./data/software.dat";
    private static SoftwareService instance;

    public static SoftwareService getInstance() throws IOException, DataLoadingException {
        if (instance == null) {
            instance = new SoftwareService();
        }
        return instance;
    }

    private SoftwareData data;
    private Map<String, Software> map;

    private SoftwareService() throws IOException, DataLoadingException {
        map = new HashMap<>();
        reload();
    }

    public boolean addSoftware(final Software software) throws IOException {
        data.getSoftware().add(software);
        map.put(software.getId(), software);
        return save();
    }

    public List<Software> getAllSoftware() {
        return data.getSoftware();
    }

    public SoftwareData getData() {
        return data;
    }

    public void setData(final SoftwareData data) {
        this.data = data;
    }

    public Software getSoftwareById(String id) {
        return map.getOrDefault(id, null);
    }

    public void reload() throws IOException, DataLoadingException {
        Persistor persistor = ClassCreator.getPersistor(new File(SAVE_PATH));
        data = persistor.load(SoftwareData.class);
        remap();
    }

    private void remap() {
        map.clear();
        for (Software software : data.getSoftware()) {
            map.put(software.getId(), software);
        }
    }

    public boolean remove(final String id) throws IOException {
        Iterator<Software> it = data.getSoftware().iterator();
        while (it.hasNext()) {
            Software software = it.next();
            if (software.getId().equals(id)) {
                it.remove();
                if (map.remove(id) == null) {
                    remap();
                }
            }
        }
        return save();
    }

    public boolean remove(Software software) throws IOException {
        return remove(software.getId());
    }

    public boolean save() throws IOException {
        Persistor persistor = ClassCreator.getPersistor(new File(SAVE_PATH));
        return persistor.save(data);
    }
}
