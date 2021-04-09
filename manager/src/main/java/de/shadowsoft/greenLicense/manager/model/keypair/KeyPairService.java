package de.shadowsoft.greenLicense.manager.model.keypair;

import de.shadowsoft.greenLicense.manager.ui.jfxclient.ClassCreator;
import de.shadowsoft.greenLicense.manager.tools.serializer.Persistor;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KeyPairService {

    public static final String ALGORITHM = "RSA";
    private static final String SAVE_PATH = "./data/keyPairs.dat";
    private static KeyPairService instance;

    public static KeyPairService getInstance() throws IOException, DataLoadingException {
        if (instance == null) {
            instance = new KeyPairService();
        }
        return instance;
    }

    private KeyPairData data;
    private Map<String, FssKeyPair> map;

    private KeyPairService() throws IOException, DataLoadingException {
        data = new KeyPairData();
        map = new HashMap<>();
        refreshData();
    }

    private void addKeyPair(FssKeyPair keyPair) {
        data.getKeyPairs().add(keyPair);
    }

    public FssKeyPair createKeyPair(String name, int size) throws GeneralSecurityException, IOException, DataLoadingException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(size, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        FssKeyPair keyPair = new FssKeyPair();
        keyPair.setId(UUID.randomUUID().toString());
        keyPair.setName(name);
        keyPair.setKeyPair(pair);
        keyPair.setSize(size);
        addKeyPair(keyPair);
        save();
        map.put(keyPair.getId(), keyPair);
        return keyPair;
    }

    public List<FssKeyPair> getAllKeyPairs() {
        return data.getKeyPairs();
    }

    public KeyPairData getData() {
        return data;
    }

    public void setData(final KeyPairData data) {
        this.data = data;
        remap();
    }

    public FssKeyPair getKeyById(String id) {
        return map.getOrDefault(id, null);
    }

    public PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.getDecoder().decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }

    public PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        return fact.generatePublic(spec);
    }

    private void refreshData() throws IOException, DataLoadingException {
        Persistor persistor = ClassCreator.getPersistor(new File(SAVE_PATH));
        data = persistor.load(KeyPairData.class);
        remap();
    }

    private void remap() {
        map.clear();
        for (FssKeyPair pair : data.getKeyPairs()) {
            map.put(pair.getId(), pair);
        }
    }

    public void removeKeyPair(final FssKeyPair selectedKeyPair) throws IOException {
        Iterator<FssKeyPair> it = data.getKeyPairs().iterator();
        while (it.hasNext()) {
            FssKeyPair pair = it.next();
            if (pair.getId().equals(selectedKeyPair.getId())) {
                it.remove();
                map.remove(pair.getId());
            }
        }
        save();
    }

    public void save() throws IOException {
        Persistor persistor = ClassCreator.getPersistor(new File(SAVE_PATH));
        persistor.save(data);
    }

    public String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
                PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = Base64.getEncoder().encodeToString(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }

    public String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

}
