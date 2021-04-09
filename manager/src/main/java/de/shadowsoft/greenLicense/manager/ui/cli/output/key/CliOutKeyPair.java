package de.shadowsoft.greenLicense.manager.ui.cli.output.key;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.tools.PubKeySerializer;

public class CliOutKeyPair {

    private String id;
    private String name;
    private String publicKey;
    private String publicKeyBytes;
    private int size;

    public CliOutKeyPair(FssKeyPair keyPair) {
        this.id = keyPair.getId();
        this.name = keyPair.getName();
        this.size = keyPair.getSize();
        this.publicKey = keyPair.getPubKey();
        this.publicKeyBytes = PubKeySerializer.serialize(keyPair);
    }

    public CliOutKeyPair() {
        id = "";
        name = "";
        size = 0;
        publicKeyBytes = "";
        publicKey = "";
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyBytes() {
        return publicKeyBytes;
    }

    public void setPublicKeyBytes(final String publicKeyBytes) {
        this.publicKeyBytes = publicKeyBytes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }
}
    
    