package de.shadowsoft.greenLicense.manager.ui.cli.output.key;

import de.shadowsoft.greenLicense.common.cli.CliOutBase;

import java.util.ArrayList;
import java.util.List;

public class CliOutKeyPairCollection extends CliOutBase {
    private List<CliOutKeyPair> keyPairs;

    public CliOutKeyPairCollection() {
        keyPairs = new ArrayList<>();
    }


    @Override
    public String formatOutput(final StringBuilder res) {
        for (CliOutKeyPair okp : keyPairs) {
            res.append("\n");
            res.append(okp.getId()).append("\t");
            res.append(okp.getName()).append("\t");
            res.append(okp.getSize()).append("\n");
            res.append(okp.getPublicKeyBytes());
        }
        return res.toString();
    }

    public List<CliOutKeyPair> getKeyPairs() {
        return keyPairs;
    }

    public void setKeyPairs(final List<CliOutKeyPair> keyPairs) {
        this.keyPairs = keyPairs;
    }
}
    
    