package de.shadowsoft.greenLicense.common.license.generator.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ResultCondenser {
    public byte[] condense(final IdResult result) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
                gos.write(gson.toJson(result).getBytes());
            }
            return bos.toByteArray();
        }
    }

    public IdResult vaporize(byte[] condensed) throws IOException {
        byte[] res;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(condensed)) {
            try (GZIPInputStream gis = new GZIPInputStream(bis)) {
                res = gis.readAllBytes();
            }
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(new String(res), IdResult.class);
        }
    }
}
    
    