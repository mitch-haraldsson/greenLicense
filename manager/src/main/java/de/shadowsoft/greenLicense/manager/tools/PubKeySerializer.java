package de.shadowsoft.greenLicense.manager.tools;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class PubKeySerializer {
    private static final Logger LOGGER = LogManager.getLogger(PubKeySerializer.class);

    public static String serialize(FssKeyPair keyPair) {
        try {
            PublicKey pk = keyPair.getKeyPair().getPublic();
            StringBuilder res = new StringBuilder("new byte[] {\n");
            boolean isFirst = true;
            int i = 0;
            for (byte b : pk.getEncoded()) {
                if (!isFirst) {
                    res.append(", ");
                }
                isFirst = false;
                if (i >= 15) {
                    res.append("\n");
                    i = 0;
                }
                res.append(String.format("(byte) 0x%02X", b));
                i++;
            }
            res.append("\n};");
            return res.toString();
        } catch (GeneralSecurityException | IOException | DataLoadingException e) {
            LOGGER.error("Unable to generate public byte code!", e);
        }
        return "";
    }
}
    
    