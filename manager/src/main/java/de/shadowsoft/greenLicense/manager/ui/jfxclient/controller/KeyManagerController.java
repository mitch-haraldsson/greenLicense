package de.shadowsoft.greenLicense.manager.ui.jfxclient.controller;


import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.tools.PubKeySerializer;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview.TMKeyPair;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.ColumnLoader;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBox;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxHelper;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxSetting;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.ResourceBundle;

public class KeyManagerController extends LicenseManagerController {
    private static final Logger LOGGER = LogManager.getLogger(KeyManagerController.class);
    @FXML
    private Button btnCreateKey;
    @FXML
    private ComboBox<Integer> cmbKeySize;
    @FXML
    private MenuItem cmnuDeleteKey;
    @FXML
    private URL location;
    @FXML
    private AnchorPane parentPane;
    @FXML
    private ResourceBundle resources;
    private FssKeyPair selectedKeyPair;
    @FXML
    private TableView<TMKeyPair> tblKeyPairs;
    @FXML
    private TextField txtKeyName;
    @FXML
    private TextArea txtPublicBytes;

    public KeyManagerController() {
    }

    @FXML
    void btnCreateKeyOnAction(ActionEvent event) {
        Platform.runLater(() -> parentPane.setDisable(true));
        try {
            if (txtKeyName.getText().trim().length() > 0) {
                KeyPairService kps = KeyPairService.getInstance();
                kps.createKeyPair(txtKeyName.getText().trim(), cmbKeySize.getSelectionModel().getSelectedItem());
                refreshTable();
            } else {
                AlertBoxHelper.showErrorBox(resources.getString("error.input.title"), resources.getString("error.keymanager.input"), String.format(resources.getString("error.keymanager.input.name.empty")));
            }
        } catch (GeneralSecurityException | IOException | DataLoadingException e) {
            LOGGER.error("Unable to load key pairs", e);
            AlertBoxHelper.showErrorBox(resources.getString("error.title"), resources.getString("error.keymanager.exception"), String.format(resources.getString("error.keymanager.exception.nosuchalgorithm"), e.getMessage()));
        } finally {
            Platform.runLater(() -> parentPane.setDisable(false));
        }
    }

    @FXML
    void cmnuDeleteKeyOnAction(ActionEvent event) {
        if (selectedKeyPair != null) {

            final AlertBoxSetting abs = new AlertBoxSetting();
            abs.setTitle(resources.getString("confirm.delete.title"));
            abs.setHeader(resources.getString("keymanager.confirm.key.delete.header"));
            abs.setContent(String.format(resources.getString("keymanager.confirm.key.delete.detail"), selectedKeyPair.getName(), selectedKeyPair.getSize()));
            abs.addButton(ButtonType.YES);
            abs.addButton(ButtonType.NO);
            final AlertBox alert = new AlertBox(abs);
            final Optional<ButtonType> alertRes = alert.showAndGet();
            if (alertRes.isPresent()) {
                if (alertRes.get() == ButtonType.YES) {
                    try {
                        KeyPairService.getInstance().removeKeyPair(selectedKeyPair);
                        refreshTable();
                    } catch (IOException | DataLoadingException e) {
                        LOGGER.error(String.format("Unable to delete key %s", selectedKeyPair.getId()), e);
                        AlertBoxHelper.showErrorBox(
                                resources.getString("error.keymanager.keypair.delete.title"),
                                resources.getString("error.keymanager.keypair.delete.header"),
                                String.format(resources.getString("error.keymanager.keypair.delete.detail"), selectedKeyPair.getName(), selectedKeyPair.getSize())
                        );
                    }
                }
            }
        }
    }

    @FXML
    void cmuCopyToClipboardOnAction(ActionEvent event) {
        StringSelection selection = new StringSelection(compileKeyPublicBytes(selectedKeyPair));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private String compileKeyPublicBytes(final FssKeyPair keyPair) {
        return PubKeySerializer.serialize(keyPair);
    }

    @FXML
    void initialize() {
        tblKeyPairs.setPlaceholder(new Label(resources.getString("ui.keymanager.tblKeyPairs.nodata")));
        tblKeyPairs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedKeyPair = newSelection.getData();
                String publicBytes = compileKeyPublicBytes(selectedKeyPair);
                txtPublicBytes.setText(publicBytes);
                cmnuDeleteKey.setDisable(false);
            } else {
                selectedKeyPair = null;
                cmnuDeleteKey.setDisable(true);
            }
        });


        cmbKeySize.getItems().clear();
        cmbKeySize.getItems().add(1024);
        cmbKeySize.getItems().add(2048);
        cmbKeySize.getItems().add(4096);
        cmbKeySize.getItems().add(8192);
        cmbKeySize.getSelectionModel().select(1);
        refreshTable();
    }

    @Override
    public void postShow() {

    }

    private void refreshTable() {
        tblKeyPairs.getItems().clear();
        tblKeyPairs.getColumns().clear();

        ColumnLoader.load(ProgramSettingsService.getInstance().getSettings().getKeyPairsTableColumnSettings(),
                tblKeyPairs, resources);

        try {
            for (FssKeyPair pair : KeyPairService.getInstance().getAllKeyPairs()) {
                tblKeyPairs.getItems().add(new TMKeyPair(pair));
            }
        } catch (IOException | DataLoadingException e) {
            LOGGER.error(String.format("Unable to load data"), e);
        }
    }

}
    
    