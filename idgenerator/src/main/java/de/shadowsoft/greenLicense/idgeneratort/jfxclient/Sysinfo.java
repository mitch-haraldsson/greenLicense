package de.shadowsoft.greenLicense.idgeneratort.jfxclient;

import java.util.Map;

public class Sysinfo {

    public String getComputername() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        } else {
            return "Unknown Computer";
        }
    }

}
    
    