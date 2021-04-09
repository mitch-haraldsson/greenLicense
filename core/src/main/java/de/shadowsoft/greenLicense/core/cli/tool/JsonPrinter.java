package de.shadowsoft.greenLicense.core.cli.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonPrinter {

    public String jsonPrinter(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

}
    
    