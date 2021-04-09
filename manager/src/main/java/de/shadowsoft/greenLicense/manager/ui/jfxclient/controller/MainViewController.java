package de.shadowsoft.greenLicense.manager.ui.jfxclient.controller;

import de.shadowsoft.greenLicense.manager.config.ImportExportService;
import de.shadowsoft.greenLicense.manager.exceptions.BadKeyException;
import de.shadowsoft.greenLicense.manager.exceptions.EncryptionException;
import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.license.LicenseCreator;
import de.shadowsoft.greenLicense.manager.license.LicenseCreatorV2;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.model.license.LicenseService;
import de.shadowsoft.greenLicense.manager.model.license.LicenseVersion;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.model.software.SoftwareService;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettings;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.config.ProgramSettingsService;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.initializer.KeyManagerInitializer;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.jfx.initializer.SoftwareManagerInitializer;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.comboview.CMSoftware;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview.TMIssuedLicenses;
import de.shadowsoft.greenLicense.manager.ui.jfxclient.view.tableview.TMLicenses;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.ColumnLoader;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBox;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxHelper;
import de.shadowsoft.greenLicense.uiFramework.jfxframework.alert.AlertBoxSetting;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController extends LicenseManagerController {
    private static final Logger LOGGER = LogManager.getLogger(MainViewController.class);
    @FXML
    private Button btnAddLicense;
    @FXML
    private Button btnNewValue;
    @FXML
    private ComboBox<CMSoftware> cmbSoftware;
    @FXML
    private MenuItem cmuDeleteLicense;
    @FXML
    private MenuItem cmuExportLicense;
    @FXML
    private MenuItem cmuShowLicense;
    @FXML
    private URL location;
    @FXML
    private MenuItem mnuExport;
    @FXML
    private MenuItem mnuImport;
    @FXML
    private MenuItem mnuKeyManager;
    @FXML
    private MenuItem mnuSoftwareManager;
    @FXML
    private ResourceBundle resources;
    @FXML
    private VBox sectionLicenseId;
    private Feature selectedFeature;
    private License selectedLicense;
    private Software selectedSoftware;
    @FXML
    private TableView<TMIssuedLicenses> tblIssuedLicenses;
    @FXML
    private TableView<TMLicenses> tblLicenseFeatures;
    @FXML
    private TextField txtLicenseId;
    @FXML
    private TextField txtLicenseName;
    @FXML
    private TextField txtNewValue;

    public MainViewController() {
    }

    @FXML
    void btnAddLicenseOnAction(ActionEvent event) {
        License license = new License();
        license.setName(txtLicenseName.getText());
        license.setSoftware(selectedSoftware.clone());
        if (license.getSoftware().getLicenseVersion().equals(LicenseVersion.LICENSE_V2)) {
            license.setLicenseId(txtLicenseId.getText());
        } else {
            license.setLicenseId("");
        }
        try {
            LicenseService.getInstance().addLicense(license);
            refreshIssuedLicenses();
        } catch (IOException | DataLoadingException e) {
            LOGGER.error(e);
            showLicenseLoadingError(e);
        }
    }

    @FXML
    void btnNewValueOnAction(ActionEvent event) {
        selectedFeature.setValue(txtNewValue.getText());
        refreshLicenseFeatureTable();
    }

    @FXML
    void cmuDeleteLicenseOnAction(ActionEvent event) {
        final AlertBoxSetting abs = new AlertBoxSetting();
        abs.setTitle(resources.getString("confirm.delete.title"));
        abs.setHeader(resources.getString("main.confirm.license.delete.header"));
        abs.setContent(String.format(resources.getString("main.confirm.license.delete.detail"), selectedLicense.getName(), selectedLicense.getSoftware().getName(), selectedLicense.getSoftware().getVersion()));
        abs.addButton(ButtonType.YES);
        abs.addButton(ButtonType.NO);
        final AlertBox alert = new AlertBox(abs);
        final Optional<ButtonType> alertRes = alert.showAndGet();
        if (alertRes.get() == ButtonType.YES) {
            try {
                Iterator<License> it = LicenseService.getInstance().getAllLicenses().iterator();
                while (it.hasNext()) {
                    License license = it.next();
                    if (license.getId().equals(selectedLicense.getId())) {
                        it.remove();
                    }
                }
                LicenseService.getInstance().save();
            } catch (IOException | DataLoadingException e) {
                LOGGER.error(String.format("Unable to delete license '%s'", selectedLicense.getId()), e);
                showLicenseLoadingError(e);
            } finally {
                refreshIssuedLicenses();
            }
        }
    }

    @FXML
    void cmuExportLicenseOnAction(ActionEvent event) {

        ProgramSettings settings = ProgramSettingsService.getInstance().getSettings();
        File initialDir = new File("./licenses");
        initialDir.mkdirs();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("License files", "*.lic")
                , new FileChooser.ExtensionFilter("All files", "*.*")
        );
        fileChooser.setInitialDirectory(new File(settings.getLastLicenseSavePath()));
        fileChooser.setInitialFileName(selectedLicense.getName() + ".lic");
        File targetFile = fileChooser.showSaveDialog(getStage().getOwner());
        if (targetFile != null) {
            settings.setLastLicenseSavePath(targetFile.getParentFile().getAbsolutePath());
            try {
                ProgramSettingsService.getInstance().save();
            } catch (IOException e) {
                LOGGER.error("Unable to save settings file", e);
                showSaveSettingsError(e);
            }
            LicenseCreator creator = new LicenseCreatorV2();
            try {
                byte[] license = creator.createLicense(selectedLicense);
                DataOutputStream os = new DataOutputStream(new FileOutputStream(targetFile));
                os.write(license);
                os.close();
                if (settings.isExportBase64()) {
                    os = new DataOutputStream(new FileOutputStream(new File(targetFile.getAbsolutePath() + settings.getBase64LicenseExtension())));
                    os.write(Base64.getEncoder().encode(license));
                    os.close();
                }
            } catch (GeneralSecurityException | NoSuchKeyPairException | IOException | DataLoadingException e) {
                LOGGER.error(String.format("Unable to create new license '%s'", selectedLicense.getId()), e);
                AlertBoxHelper.showErrorBox(
                        resources.getString("error.title"),
                        resources.getString("error.createlicense.header"),
                        e.getMessage()
                );
            }
        }
    }

    @FXML
    void cmuShowLicense(ActionEvent event) {
        if (selectedLicense != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("License name: ").append(selectedLicense.getName()).append("\n");
            builder.append("Program: ").append(selectedLicense.getSoftware().getName());
            builder.append(" (").append(selectedLicense.getSoftware().getVersion()).append(")").append("\n");
            builder.append("License features: ").append("\n");
            for (Feature feature : selectedLicense.getSoftware().getFeatures()) {
                builder.append("\t").append(feature.getName()).append(": ").append(feature.getValue()).append("\n");
            }
            AlertBoxHelper.showInfoBox(
                    resources.getString("license.info.title"),
                    String.format(resources.getString("license.info.header"), selectedLicense.getId()),
                    builder.toString()
            );
        }
    }

    private FileChooser createFileChooser() {
        FileChooser fc = new FileChooser();
        File initialDir = new File(ProgramSettingsService.getInstance().getSettings().getLastExportPath());
        if (!initialDir.exists()) {
            initialDir = new File("./");
        }
        fc.setInitialDirectory(initialDir);
        FileChooser.ExtensionFilter eflmd = new FileChooser.ExtensionFilter("License manager data (*.lmd)", "*.lmd");
        FileChooser.ExtensionFilter efall = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
        fc.getExtensionFilters().add(eflmd);
        fc.getExtensionFilters().add(efall);
        return fc;
    }

    @FXML
    void initialize() {
        ProgramSettingsService.getInstance().setResourceBundle(resources);
        refreshIssuedLicenses();
        refreshSoftware();
        initializeSoftwareList();
        initializeLicenseFeatureTable();
        initializeIssuedLicenseTable();
        btnNewValue.setDisable(true);
        setContextMenuEnabled(false);
        refreshVisibleElements();
    }

    private void initializeIssuedLicenseTable() {
        tblIssuedLicenses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedLicense = newSelection.getLicense();
                setContextMenuEnabled(true);
            } else {
                selectedFeature = null;
            }
        });
    }

    private void initializeLicenseFeatureTable() {
        tblLicenseFeatures.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedFeature = newSelection.getFeature();
                txtNewValue.setText(selectedFeature.getValue());
                btnNewValue.setDisable(false);
            } else {
                selectedFeature = null;
                btnNewValue.setDisable(true);
            }
        });
    }


    private void initializeSoftwareList() {
        cmbSoftware.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSoftware = newSelection.getData().clone();
                refreshLicenseFeatureTable();
            } else {
                selectedSoftware = null;
            }
            refreshVisibleElements();
        });
    }

    @FXML
    void mnuExportOnAction(ActionEvent event) {
        File file = createFileChooser().showSaveDialog(getStage().getOwner());
        if (file != null) {
            ImportExportService ies = new ImportExportService();
            try {
                ies.save(file, "$2a$07$b9ajO2LwTI0bVaNEmPugtuq7mJCOvaXGcDy3y7/b55X0woS5FeiyO");
                storeLastExportPath(file);
            } catch (IOException | EncryptionException | BadKeyException e) {
                LOGGER.error("Unable to export file", e);
                AlertBoxHelper.showErrorBox(resources.getString("error.export"),
                        resources.getString("error.export.failed"),
                        e.getMessage());
            }
        }
    }

    @FXML
    void mnuImportOnAction(ActionEvent event) {
        File file = createFileChooser().showOpenDialog(getStage().getOwner());
        if (file != null) {
            ImportExportService ies = new ImportExportService();
            try {
                ies.load(file, "$2a$07$b9ajO2LwTI0bVaNEmPugtuq7mJCOvaXGcDy3y7/b55X0woS5FeiyO");
                storeLastExportPath(file);
                refreshAll();
            } catch (IOException | EncryptionException | DataLoadingException e) {
                LOGGER.error("Unable to import file", e);
                AlertBoxHelper.showErrorBox(resources.getString("error.import"),
                        resources.getString("error.import.failed"),
                        e.getMessage());
            } catch (BadKeyException e) {
                LOGGER.error("Unable to import file. Padding exception. Probably wrong password.");
                AlertBoxHelper.showErrorBox(resources.getString("error.import"),
                        resources.getString("error.import.failed.password"),
                        "");
            }
        }
    }

    @FXML
    void mnuKeyManager(ActionEvent event) {
        LOGGER.info("Loading key manager...");
        new KeyManagerInitializer(new KeyManagerController()).showAndWait();
        refreshAll();
    }

    @FXML
    void mnuQuitOnAction(ActionEvent event) {
        final AlertBoxSetting abs = new AlertBoxSetting();
        abs.setTitle(resources.getString("confirm.exit.title"));
        abs.setHeader(resources.getString("application.confirm.exit.header"));
        abs.setContent("");
        abs.addButton(ButtonType.YES);
        abs.addButton(ButtonType.NO);
        final AlertBox alert = new AlertBox(abs);
        final Optional<ButtonType> alertRes = alert.showAndGet();
        if (alertRes.get() == ButtonType.YES) {
            try {
                ProgramSettingsService.getInstance().save();
            } catch (IOException e) {
                LOGGER.error("Unable to save settings file upon program exit", e);
                showSaveSettingsError(e);
            } finally {
                Platform.exit();
            }
        }
    }

    @FXML
    void mnuSoftwareManagerOnAction(ActionEvent event) {
        LOGGER.info("Loading software manager...");
        new SoftwareManagerInitializer(new SoftwareController()).showAndWait();
        refreshAll();
    }

    @Override
    public void postShow() {
    }

    private void refreshAll() {
        refreshIssuedLicenses();
        refreshSoftware();
        refreshLicenseFeatureTable();
    }

    private void refreshIssuedLicenses() {
        tblIssuedLicenses.getItems().clear();
        tblIssuedLicenses.getColumns().clear();

        ColumnLoader.load(ProgramSettingsService.getInstance().getSettings().getIssuedLicensesTableColumnSettings(),
                tblIssuedLicenses, resources);

        try {
            for (License license : LicenseService.getInstance().getAllLicenses()) {
                tblIssuedLicenses.getItems().add(new TMIssuedLicenses(license));
            }
        } catch (IOException | DataLoadingException e) {
            LOGGER.error(String.format("Unable to load data"), e);
            showLicenseLoadingError(e);
        }
    }

    private void refreshLicenseFeatureTable() {
        tblLicenseFeatures.getItems().clear();
        tblLicenseFeatures.getColumns().clear();


        ColumnLoader.load(ProgramSettingsService.getInstance().getSettings().getLicenseFeatureTableColumnSettings(),
                tblLicenseFeatures, resources);
        if (selectedSoftware != null) {
            for (Feature feature : selectedSoftware.getFeatures()) {
                tblLicenseFeatures.getItems().add(new TMLicenses(feature));
            }
        }
    }

    private void refreshSoftware() {
        Platform.runLater(() -> {
            final ObservableList<CMSoftware> data = FXCollections.observableArrayList();
            try {
                for (final Software software : SoftwareService.getInstance().getAllSoftware()) {
                    data.add(new CMSoftware(software));
                }
            } catch (IOException | DataLoadingException e) {
                AlertBoxHelper.showErrorBox(
                        resources.getString("error.title"),
                        resources.getString("error.loadsoftware.header"),
                        e.getMessage()
                );
            }
            cmbSoftware.setItems(data);
        });
    }

    private void refreshVisibleElements() {
        sectionLicenseId.setVisible(false);
        if (selectedSoftware != null) {
            if (selectedSoftware.getLicenseVersion() != null) {
                switch (selectedSoftware.getLicenseVersion()) {
                    case LICENSE_V2:
                        sectionLicenseId.setVisible(true);
                        break;

                    case LICENSE_V1:
                    default:
                }
            }
        }
    }

    private void setContextMenuEnabled(final boolean enabled) {
        cmuDeleteLicense.setDisable(!enabled);
        cmuExportLicense.setDisable(!enabled);
        cmuShowLicense.setDisable(!enabled);
    }

    private void showLicenseLoadingError(final Exception exception) {
        showLicenseLoadingError(exception.getMessage());
    }

    private void showLicenseLoadingError(final String errorMessage) {
        AlertBoxHelper.showErrorBox(
                resources.getString("error.title"),
                resources.getString("error.loadlicense.header"),
                errorMessage
        );
    }

    private void showSaveSettingsError(final Exception e) {
        showSaveSettingsError(e.getMessage());
    }

    private void showSaveSettingsError(String errorMessage) {
        AlertBoxHelper.showErrorBox(
                resources.getString("error.title"),
                resources.getString("error.settingsfile.save"),
                errorMessage
        );
    }

    private void storeLastExportPath(final File file) throws IOException {
        ProgramSettingsService.getInstance().getSettings().setLastExportPath(file.getParentFile().getAbsolutePath());
        ProgramSettingsService.getInstance().save();
    }
}
    
    