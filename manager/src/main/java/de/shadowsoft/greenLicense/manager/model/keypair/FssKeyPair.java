package de.shadowsoft.greenLicense.manager.model.keypair;

import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class FssKeyPair {

    private String id;
    private String name;
    private String privKey;
    private String pubKey;
    private int size;

    public FssKeyPair() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public KeyPair getKeyPair() throws IOException, GeneralSecurityException, DataLoadingException {
        PrivateKey privateKey = KeyPairService.getInstance().loadPrivateKey(this.privKey);
        PublicKey pubKey = KeyPairService.getInstance().loadPublicKey(this.pubKey);
        return new KeyPair(pubKey, privateKey);
    }

    public void setKeyPair(KeyPair keyPair) throws IOException, GeneralSecurityException, DataLoadingException {
        setPrivKey(KeyPairService.getInstance().savePrivateKey(keyPair.getPrivate()));
        setPubKey(KeyPairService.getInstance().savePublicKey(keyPair.getPublic()));
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(final String privKey) {
        this.privKey = privKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(final String pubKey) {
        this.pubKey = pubKey;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name + " (" + size + ")";
    }

}
    
    