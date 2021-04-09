package de.shadowsoft.greenLicense.manager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.shadowsoft.greenLicense.manager.exceptions.BadKeyException;
import de.shadowsoft.greenLicense.manager.exceptions.EncryptionException;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.LicenseService;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.tools.AESStringCrypt;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImportExportService {

    private ProgramData data;

    public ImportExportService() {
        data = new ProgramData();
    }

    public ProgramData getData() {
        return data;
    }

    public void setData(final ProgramData data) {
        this.data = data;
    }

    public void load(final File file, final String key) throws IOException, DataLoadingException, EncryptionException, BadKeyException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] json = AESStringCrypt.decrypt(fis.readAllBytes(), key);
            String jsonString = new String(json);
            data = new Gson().fromJson(jsonString, ProgramData.class);
            KeyPairService.getInstance().setData(data.getKeyPairs());
            LicenseService.getInstance().setData(data.getLicenses());
            SoftwareService.getInstance().setData(data.getSoftware());
            KeyPairService.getInstance().save();
            LicenseService.getInstance().save();
            SoftwareService.getInstance().save();
        }
    }

    public void save(final File file, final String key) throws IOException, EncryptionException, BadKeyException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        byte[] json = AESStringCrypt.encrypt(gson.toJson(data).getBytes(), key);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(json);
        }
    }
}
    
    