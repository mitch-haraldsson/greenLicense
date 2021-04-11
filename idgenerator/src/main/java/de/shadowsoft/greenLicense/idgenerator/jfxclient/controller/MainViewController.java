package de.shadowsoft.greenLicense.idgenerator.jfxclient.controller;

import de.shadowsoft.greenLicense.common.license.generator.core.generator.BasicIdGenerator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdGenerator;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.config.LanguageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class MainViewController extends IdGeneratorController {
    private static final Logger LOGGER = LogManager.getLogger(MainViewController.class);
    @FXML
    private Button btnClose;
    @FXML
    private CheckBox chkHost;
    @FXML
    private CheckBox chkIp;
    @FXML
    private CheckBox chkMac;
    @FXML
    private CheckBox chkOs;
    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private AnchorPane rootPane;
    private byte selector;
    @FXML
    private TextArea txtId;


    public MainViewController() {

    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        killStage();
    }

    @FXML
    void chkSelectorOnAction(ActionEvent event) {
        updateSelection();
    }

    @FXML
    void initialize() {
        LanguageManager.getInstance().setResources(resources);
        updateSelection();
    }

    @Override
    public void postShow() {
    }

    private void updateSelection() {
        final String selectorString = "0000" +
                (chkOs.isSelected() ? "1" : "0") +
                (chkIp.isSelected() ? "1" : "0") +
                (chkHost.isSelected() ? "1" : "0") +
                (chkMac.isSelected() ? "1" : "0");
        selector = Byte.parseByte(selectorString, 2);
        final IdGenerator generator = new BasicIdGenerator();
        Thread t = new Thread(() -> {
            try {
                Platform.runLater(() -> rootPane.setDisable(true));
                String id = new String(Base64.getEncoder().encode(generator.generateId(selector)));
                Platform.runLater(() -> txtId.setText(id));
                Platform.runLater(() -> rootPane.setDisable(false));
            } catch (IOException | InterruptedException e) {
                LOGGER.error(String.format("Unable to enumerate mac addresses"), e);
            }
        });
        t.start();
    }
}
    
    