package de.shadowsoft.greenLicense.manager.ui.jfxclient.controller;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.common.license.LicenseVersion;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.comboview.CMKeyPair;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview.TMFeature;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview.TMSoftware;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.ColumnLoader;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBox;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxHelper;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxSetting;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SoftwareController extends LicenseManagerController {
    private static final Logger LOGGER = LogManager.getLogger(SoftwareController.class);
    @FXML
    private Button btnAddFeature;
    @FXML
    private Button btnAddSoftware;
    @FXML
    private ComboBox<LicenseVersion> cmbLicenseVersion;
    @FXML
    private ComboBox<CMKeyPair> cmbSoftwareKeypair;
    @FXML
    private MenuItem cmuCopyFeatureIdToClipboard;
    @FXML
    private MenuItem cmuDeleteFeature;
    @FXML
    private MenuItem cmuDeleteSoftware;
    @FXML
    private Label lblSelectedFeature;
    @FXML
    private URL location;
    @FXML
    private AnchorPane parentPane;
    private List<String> recentErrorList;
    @FXML
    private ResourceBundle resources;
    private Feature selectedFeature;
    private FssKeyPair selectedKeyPair;
    private LicenseVersion selectedLicenseVersion;
    private Software selectedSoftware;
    @FXML
    private Tab tabFeatures;
    @FXML
    private Tab tabSoftware;
    @FXML
    private TableView<TMFeature> tblFeatures;
    @FXML
    private TableView<TMSoftware> tblSoftware;
    @FXML
    private TextField txtFeatureDefaultValue;
    @FXML
    private TextField txtFeatureId;
    @FXML
    private TextField txtFeatureName;
    @FXML
    private TextField txtSoftwareName;
    @FXML
    private TextField txtVersion;

    @FXML
    void btnAddFeatureOnAction(ActionEvent event) {
        sanitizeFeatureInput();
        if (isValidFeatureInput()) {
            Feature feature = new Feature();
            if (txtFeatureId.getText().length() > 0) {
                feature.setId(txtFeatureId.getText());
            }
            feature.setName(txtFeatureName.getText());
            feature.setValue(txtFeatureDefaultValue.getText());
            selectedSoftware.getFeatures().add(feature);
            txtFeatureId.setText("");
            txtFeatureDefaultValue.setText("");
            txtFeatureName.setText("");
            try {
                if (!SoftwareService.getInstance().save()) {
                    showSoftwareLoadingError("Unable to save software");
                }
                refreshFeatureTable();
            } catch (IOException | DataLoadingException e) {
                LOGGER.error(String.format("Unable to save software changes", e));
                showSoftwareLoadingError(e);
            }
        } else {
            AlertBoxHelper.showErrorBox(
                    resources.getString("error.input.title"),
                    resources.getString("error.input.feature.add"),
                    buildErrorMessages().toString()
            );
            recentErrorList.clear();
        }
    }

    @FXML
    void btnAddSoftwareOnAction(ActionEvent event) {
        sanitizeSoftwareInput();
        if (isValidSoftwareInput()) {
            Software software = new Software();
            software.setKeyPairId(selectedKeyPair.getId());
            software.setVersion(txtVersion.getText());
            software.setName(txtSoftwareName.getText());
            software.setLicenseVersion(selectedLicenseVersion);
            try {
                SoftwareService.getInstance().addSoftware(software);
                txtVersion.setText("");
                txtSoftwareName.setText("");
                refreshSoftwareTable();
            } catch (IOException | DataLoadingException e) {
                LOGGER.error(String.format("Unable to add new Software %s - %s (KPID: %s)", software.getName(), software.getVersion(), software.getKeyPairId()), e);
                showSoftwareLoadingError(e);
            }
        } else {
            AlertBoxHelper.showErrorBox(
                    resources.getString("error.input.title"),
                    resources.getString("error.input.softwaremanager.add"),
                    buildErrorMessages().toString()
            );
            recentErrorList.clear();
        }
    }

    private StringBuilder buildErrorMessages() {
        StringBuilder errorMessages = new StringBuilder();
        boolean isFirst = true;
        for (String err : recentErrorList) {
            if (!isFirst) {
                errorMessages.append("\n");
            }
            isFirst = false;
            errorMessages.append("\n").append(err);
        }
        return errorMessages;
    }

    @FXML
    void cmuCopyFeatureIdToClipboardOnAction(ActionEvent event) {
        StringSelection selection = new StringSelection(selectedFeature.getId());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    @FXML
    void cmuDeleteFeatureOnAction(ActionEvent event) {
        if (selectedSoftware != null) {
            final AlertBoxSetting abs = new AlertBoxSetting();
            abs.setTitle(resources.getString("confirm.delete.title"));
            abs.setHeader(resources.getString("softwaremanager.confirm.feature.delete.header"));
            abs.setContent(String.format(resources.getString("softwaremanager.confirm.feature.delete.detail"), selectedFeature.getId(), selectedFeature.getName(), selectedFeature.getValue()));
            abs.addButton(ButtonType.YES);
            abs.addButton(ButtonType.NO);
            final AlertBox alert = new AlertBox(abs);
            final Optional<ButtonType> alertRes = alert.showAndGet();
            if (alertRes.get() == ButtonType.YES) {
                Iterator<Feature> it = selectedSoftware.getFeatures().iterator();
                while (it.hasNext()) {
                    Feature feature = it.next();
                    if (feature.getId().equals(selectedFeature.getId())) {
                        it.remove();
                    }
                }
                try {
                    SoftwareService.getInstance().save();
                } catch (IOException | DataLoadingException e) {
                    LOGGER.error(String.format("Unable to save software %s", selectedSoftware.getId()), e);
                    showSoftwareLoadingError(e);
                }
            }
        }
        refreshFeatureTable();
    }

    @FXML
    void cmuDeleteSoftwareOnAction(ActionEvent event) {
        if (selectedSoftware != null) {
            final AlertBoxSetting abs = new AlertBoxSetting();
            abs.setTitle(resources.getString("confirm.delete.title"));
            abs.setHeader(resources.getString("softwaremanager.confirm.software.delete.header"));
            abs.setContent(String.format(resources.getString("softwaremanager.confirm.software.delete.detail"), selectedSoftware.getName(), selectedSoftware.getVersion()));
            abs.addButton(ButtonType.YES);
            abs.addButton(ButtonType.NO);
            final AlertBox alert = new AlertBox(abs);
            final Optional<ButtonType> alertRes = alert.showAndGet();
            if (alertRes.get() == ButtonType.YES) {
                try {
                    SoftwareService.getInstance().remove(selectedSoftware);
                    refreshSoftwareTable();
                } catch (IOException | DataLoadingException e) {
                    LOGGER.error(String.format("Unable to delete software %s", selectedSoftware.getId()), e);
                    showSoftwareLoadingError(e);
                }
            }
        }
    }

    @FXML
    void initialize() {
        recentErrorList = new ArrayList<>();
        tabFeatures.setDisable(true);
        initializeCmbKeyPair();
        cmuDeleteSoftware.setDisable(true);
        lblSelectedFeature.setText("");
        initializeTblSoftware();
        initializeTblFeature();
        refreshSoftwareTable();
        refreshKeypairList();
        refreshFeatureTable();
        initializeLicenseVersion();
    }

    private void initializeCmbKeyPair() {
        cmbSoftwareKeypair.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedKeyPair = newSelection.getData();
            } else {
                selectedKeyPair = null;
            }
        });
    }

    private void initializeLicenseVersion() {
        cmbLicenseVersion.getItems().clear();
        cmbLicenseVersion.getItems().add(LicenseVersion.LICENSE_V1);
        cmbLicenseVersion.getItems().add(LicenseVersion.LICENSE_V2);
        cmbLicenseVersion.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedLicenseVersion = newSelection;
            } else {
                selectedLicenseVersion = null;
            }
        });
    }

    private void initializeTblFeature() {
        tblFeatures.setPlaceholder(new Label(resources.getString("ui.software.tblfeature.nodata")));
        tblFeatures.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedFeature = newSelection.getData();
                cmuDeleteFeature.setDisable(false);
                cmuCopyFeatureIdToClipboard.setDisable(false);
            } else {
                selectedFeature = null;
                cmuDeleteFeature.setDisable(true);
                cmuCopyFeatureIdToClipboard.setDisable(true);
            }
        });
    }

    private void initializeTblSoftware() {
        tblSoftware.setPlaceholder(new Label(resources.getString("ui.software.tblsoftware.nodata")));
        tblSoftware.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSoftware = newSelection.getData();
                tabFeatures.setDisable(false);
                cmuDeleteSoftware.setDisable(false);
            } else {
                selectedSoftware = null;
                tabFeatures.setDisable(true);
                cmuDeleteSoftware.setDisable(true);
            }
        });
    }

    private boolean isValidFeatureInput() {
        List<String> errors = new ArrayList<>();
        boolean hasErrors = false;
        if (txtFeatureName.getText().length() < 1) {
            errors.add(resources.getString("error.input.softwaremanager.addfeature.name.length"));
            hasErrors = true;
        }

        if (txtFeatureDefaultValue.getText().length() < 1) {
            errors.add(resources.getString("error.input.softwaremanager.addfeature.default.length"));
            hasErrors = true;
        }

        for (Feature feature : selectedSoftware.getFeatures()) {
            if (feature.getId().equals(txtFeatureId.getText())) {
                errors.add(resources.getString("error.input.softwaremanager.addfeature.id.exists"));
                hasErrors = true;
            }
        }

        recentErrorList.addAll(errors);

        return !hasErrors;
    }

    private boolean isValidSoftwareInput() {
        List<String> errors = new ArrayList<>();
        boolean hasErrors = false;
        if (txtSoftwareName.getText().length() < 1) {
            errors.add(resources.getString("error.input.softwaremanager.add.name.length"));
            hasErrors = true;
        }

        if (txtVersion.getText().length() < 1) {
            errors.add(resources.getString("error.input.softwaremanager.add.version.length"));
            hasErrors = true;
        }

        if (selectedKeyPair == null) {
            errors.add(resources.getString("error.input.softwaremanager.add.keypair.null"));
            hasErrors = true;
        }

        recentErrorList.addAll(errors);

        return !hasErrors;
    }

    @Override
    public void postShow() {

    }

    private void refreshFeatureTab() {
        if (selectedSoftware != null) {
            lblSelectedFeature.setText(String.format(resources.getString("ui.softwaremanager.features.selectedsoftware"),
                    selectedSoftware.getName(),
                    selectedSoftware.getVersion())
            );
        }
        refreshFeatureTable();
    }

    private void refreshFeatureTable() {
        tblFeatures.getItems().clear();
        tblFeatures.getColumns().clear();


        ColumnLoader.load(ProgramSettingsService.getInstance().getSettings().getFeatureTableColumnSettings(),
                tblFeatures, resources);

        if (selectedSoftware != null) {
            for (Feature feature : selectedSoftware.getFeatures()) {
                tblFeatures.getItems().add(new TMFeature(feature));
            }
        }
    }

    private void refreshKeypairList() {
        Platform.runLater(() -> {
            final ObservableList<CMKeyPair> data = FXCollections.observableArrayList();
            try {
                for (final FssKeyPair keyPair : KeyPairService.getInstance().getAllKeyPairs()) {
                    data.add(new CMKeyPair(keyPair));
                }
            } catch (IOException | DataLoadingException e) {
                AlertBoxHelper.showErrorBox(
                        resources.getString("error.title"),
                        resources.getString("error.loadkeypair.header"),
                        e.getMessage()
                );
            }
            cmbSoftwareKeypair.setItems(data);
            if (data.size() > 0) {
                cmbSoftwareKeypair.getSelectionModel().select(0);
                selectedKeyPair = cmbSoftwareKeypair.getSelectionModel().getSelectedItem().getData();
            }
        });
    }

    private void refreshSoftwareTab() {

    }

    private void refreshSoftwareTable() {
        tblSoftware.getItems().clear();
        tblSoftware.getColumns().clear();
        ColumnLoader.load(ProgramSettingsService.getInstance().getSettings().getSoftwareTableColumnSettings(), tblSoftware, resources);

        try {
            for (Software software : SoftwareService.getInstance().getAllSoftware()) {
                tblSoftware.getItems().add(new TMSoftware(software));
            }
        } catch (IOException | DataLoadingException e) {
            LOGGER.error(String.format("Unable to load data"), e);
            showSoftwareLoadingError(e);
        }
    }

    private void sanitizeFeatureInput() {
        txtFeatureId.setText(txtFeatureId.getText().trim());
        txtFeatureName.setText(txtFeatureName.getText().trim());
        txtFeatureDefaultValue.setText(txtFeatureDefaultValue.getText().trim());
    }

    private void sanitizeSoftwareInput() {
        txtSoftwareName.setText(txtSoftwareName.getText().trim());
        txtVersion.setText(txtVersion.getText().trim());
    }

    private void showSoftwareLoadingError(final Exception e) {
        showSoftwareLoadingError(e.getMessage());
    }

    private void showSoftwareLoadingError(final String errorMessage) {
        AlertBoxHelper.showErrorBox(
                resources.getString("error.title"),
                resources.getString("error.loadsoftware.header"),
                errorMessage
        );
    }

    @FXML
    void tabFeaturesOnAction(Event event) {
        refreshFeatureTab();
    }

    @FXML
    void tabSoftwareOnAction(Event event) {

    }
}