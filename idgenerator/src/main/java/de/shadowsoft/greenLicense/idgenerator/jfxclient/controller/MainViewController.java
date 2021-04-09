package de.shadowsoft.greenLicense.idgenerator.jfxclient.controller;

import de.shadowsoft.greenLicense.common.license.generator.Selector;
import de.shadowsoft.greenLicense.idgenerator.core.generator.BasicIdGenerator;
import de.shadowsoft.greenLicense.idgenerator.core.generator.IdGenerator;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.config.LanguageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
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

    @FXML
    void initialize() {
        final IdGenerator generator = new BasicIdGenerator();
        LanguageManager.getInstance().setResources(resources);
        cmbGenerator.getItems().clear();
        cmbGenerator.getItems().addAll(selectors);
        cmbGenerator.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Thread t = new Thread(() -> {
                Platform.runLater(() -> rootPane.setDisable(true));
                String id = new String(Base64.getEncoder().encode(generator.generateId(newValue)));
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
    
    