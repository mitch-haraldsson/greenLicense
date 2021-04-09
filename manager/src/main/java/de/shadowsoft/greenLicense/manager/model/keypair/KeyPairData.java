package de.shadowsoft.greenLicense.manager.model.keypair;

import java.util.ArrayList;
import java.util.List;

public class KeyPairData {

    private List<FssKeyPair> keyPairs;

    public KeyPairData() {
        keyPairs = new ArrayList<>();
    }

    public List<FssKeyPair> getKeyPairs() {
        return keyPairs;
    }

    public void setKeyPairs(final List<FssKeyPair> keyPairs) {
        this.keyPairs = keyPairs;
    }
}
    
    