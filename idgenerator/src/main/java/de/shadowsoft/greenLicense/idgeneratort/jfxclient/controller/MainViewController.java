package de.shadowsoft.greenLicense.idgeneratort.jfxclient.controller;

import de.shadowsoft.greenLicense.common.license.generator.Generator;
import de.shadowsoft.greenLicense.common.license.generator.Selector;
import de.shadowsoft.greenLicense.common.license.generator.mac.MacEncrypt;
import de.shadowsoft.greenLicense.idgeneratort.jfxclient.Sysinfo;
import de.shadowsoft.greenLicense.idgeneratort.jfxclient.config.LanguageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController extends IdGeneratorController {
    private static final Logger LOGGER = LogManager.getLogger(MainViewController.class);
    @FXML
    private ChoiceBox<Selector> cmbGenerator;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private AnchorPane rootPane;
    private List<Selector> selectors;
    @FXML
    private TextArea txtId;

    public MainViewController() {
        selectors = new ArrayList<>();
        selectors.add(new Selector(0x5C, "MAC"));
//        selectors.add(new Selector(0xB6, "Hostname"));
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        killStage();
    }

    private byte[] generateId(final Selector selector) {
        byte[] result = null;
        try {
            switch (selector.getId()) {
                case 0x5C:
                    byte[] mac = getMacAddressesBytes();
                    if (mac != null) {
                        Generator macGenerator = new MacEncrypt(mac);
                        Thread t = new Thread(macGenerator);
                        t.start();
                        t.join();
                        result = macGenerator.getResult();
                    }
                    break;

                case 0xB6:
                    byte[] computerName = new Sysinfo().getComputername().getBytes();
                    Generator macGenerator = new MacEncrypt(computerName);
                    Thread t = new Thread(macGenerator);
                    t.start();
                    t.join();
                    result = macGenerator.getResult();
                    break;
            }
        } catch (InterruptedException e) {
            LOGGER.error("Unable to calculate ID", e);
        }
        return result;
    }

    private byte[] getMacAddressesBytes() {
        List<byte[]> bytes = new ArrayList<>();
        try {
            Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
            while (it.hasNext()) {
                NetworkInterface inf = it.next();
                if (inf != null) {
                    if (inf.getHardwareAddress() != null) {
                        bytes.add(inf.getHardwareAddress());
                    }
                }
            }
            byte[] mac = null;
            for (byte[] ba : bytes) {
                mac = ArrayUtils.addAll(mac, ba);
            }
            return mac;
        } catch (SocketException e) {
            LOGGER.error("Unable to enumerate network adapters", e);
        }
        return null;
    }

    @FXML
    void initialize() {
        LanguageManager.getInstance().setResources(resources);
        cmbGenerator.getItems().clear();
        cmbGenerator.getItems().addAll(selectors);
        cmbGenerator.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Thread t = new Thread(() -> {
                Platform.runLater(() -> rootPane.setDisable(true));
                String id = new String(Base64.getEncoder().encode(generateId(newValue)));
                Platform.runLater(() -> txtId.setText(id));
                Platform.runLater(() -> rootPane.setDisable(false));
            });
            t.start();
        });
    }

    private String macArrToHex(byte[] macArr) {
        int arrayLength = 6;
        StringBuilder sb = new StringBuilder("\n");
        if (macArr.length % arrayLength == 0) {
            for (int i = 0; i < macArr.length; i += arrayLength) {
                byte[] mac = new byte[arrayLength];
                for (int j = 0; j < arrayLength; j++) {
                    mac[j] = macArr[i + j];
                }
                sb.append(macToHex(mac));
                sb.append("\n");
            }
        } else {
            LOGGER.error(String.format("Invalid array length: %s (must be a magnitude of %s)", macArr.length, arrayLength));
        }
        return sb.toString();
    }

    private StringBuilder macToHex(final byte[] mac) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb;
    }

    @Override
    public void postShow() {
    }
}
    
    