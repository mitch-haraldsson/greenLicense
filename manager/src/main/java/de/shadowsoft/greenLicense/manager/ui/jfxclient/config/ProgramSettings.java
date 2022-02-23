package de.shadowsoft.greenLicense.manager.ui.jfxclient.config;


import de.shadowsoft.greenLicense.uiFramework.jfxframework.ColumnSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProgramSettings {

    private String base64LicenseExtension;
    private boolean exportBase64;
    private List<ColumnSetting> featureTableColumnSettings;
    private List<ColumnSetting> issuedLicensesTableColumnSettings;
    private List<ColumnSetting> keyPairsTableColumnSettings;
    private String lastExportPath;
    private String lastLicenseSavePath;
    private List<ColumnSetting> licenseFeatureTableColumnSettings;
    private Locale locale;
    private List<ColumnSetting> softwareTableColumnSettings;

    public ProgramSettings() {
        initSoftwareTableColumnSettings();
        initIssuedLicensesTableColumnSettings();
        initLicenseFeatureTableColumnSettings();
        initKeyPairsTableColumnSettings();
        initFeatureTableColumnSettings();
        locale = new Locale("en", "US");
        lastLicenseSavePath = "./licenses";
        lastExportPath = "./";
        exportBase64 = true;
        base64LicenseExtension = ".b64";
    }

    public String getBase64LicenseExtension() {
        return base64LicenseExtension;
    }

    public void setBase64LicenseExtension(final String base64LicenseExtension) {
        this.base64LicenseExtension = base64LicenseExtension;
    }

    public List<ColumnSetting> getFeatureTableColumnSettings() {
        return featureTableColumnSettings;
    }

    public void setFeatureTableColumnSettings(final List<ColumnSetting> featureTableColumnSettings) {
        this.featureTableColumnSettings = featureTableColumnSettings;
    }

    public List<ColumnSetting> getIssuedLicensesTableColumnSettings() {
        return issuedLicensesTableColumnSettings;
    }

    public void setIssuedLicensesTableColumnSettings(final List<ColumnSetting> issuedLicensesTableColumnSettings) {
        this.issuedLicensesTableColumnSettings = issuedLicensesTableColumnSettings;
    }

    public List<ColumnSetting> getKeyPairsTableColumnSettings() {
        return keyPairsTableColumnSettings;
    }

    public void setKeyPairsTableColumnSettings(final List<ColumnSetting> keyPairsTableColumnSettings) {
        this.keyPairsTableColumnSettings = keyPairsTableColumnSettings;
    }

    public String getLastExportPath() {
        return lastExportPath;
    }

    public void setLastExportPath(final String lastExportPath) {
        this.lastExportPath = lastExportPath;
    }

    public String getLastLicenseSavePath() {
        return lastLicenseSavePath;
    }

    public void setLastLicenseSavePath(final String lastLicenseSavePath) {
        this.lastLicenseSavePath = lastLicenseSavePath;
    }

    public List<ColumnSetting> getLicenseFeatureTableColumnSettings() {
        return licenseFeatureTableColumnSettings;
    }

    public void setLicenseFeatureTableColumnSettings(final List<ColumnSetting> licenseFeatureTableColumnSettings) {
        this.licenseFeatureTableColumnSettings = licenseFeatureTableColumnSettings;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public String getProgramTitleBarPrefix() {
        return "FSS License Manager - ";
    }

    public List<ColumnSetting> getSoftwareTableColumnSettings() {
        return softwareTableColumnSettings;
    }

    public void setSoftwareTableColumnSettings(final List<ColumnSetting> softwareTableColumnSettings) {
        this.softwareTableColumnSettings = softwareTableColumnSettings;
    }

    private void initFeatureTableColumnSettings() {
        featureTableColumnSettings = new ArrayList<>();
        featureTableColumnSettings.add(new ColumnSetting("id", "softwaremanager.featuretable.id", 0));
        featureTableColumnSettings.add(new ColumnSetting("name", "softwaremanager.featuretable.name", 0));
        featureTableColumnSettings.add(new ColumnSetting("defaultValue", "softwaremanager.featuretable.defaultvalue", 0));
    }

    private void initIssuedLicensesTableColumnSettings() {
        issuedLicensesTableColumnSettings = new ArrayList<>();
        issuedLicensesTableColumnSettings.add(new ColumnSetting("name", "main.issuedlicenses.name", 0));
        issuedLicensesTableColumnSettings.add(new ColumnSetting("softwareName", "main.issuedlicenses.software.name", 0));
        issuedLicensesTableColumnSettings.add(new ColumnSetting("softwareVersion", "main.issuedlicenses.software.version", 0));
    }

    private void initKeyPairsTableColumnSettings() {
        keyPairsTableColumnSettings = new ArrayList<>();
        keyPairsTableColumnSettings.add(new ColumnSetting("name", "keymanager.table.name", 0));
        keyPairsTableColumnSettings.add(new ColumnSetting("size", "keymanager.table.size", 0));
    }

    private void initLicenseFeatureTableColumnSettings() {
        licenseFeatureTableColumnSettings = new ArrayList<>();
        licenseFeatureTableColumnSettings.add(new ColumnSetting("name", "main.licensefeatures.name", 0));
        licenseFeatureTableColumnSettings.add(new ColumnSetting("value", "main.licensefeatures.value", 0));
    }

    private void initSoftwareTableColumnSettings() {
        softwareTableColumnSettings = new ArrayList<>();
        softwareTableColumnSettings.add(new ColumnSetting("name", "softwaremanager.softwaretable.name", 0));
        softwareTableColumnSettings.add(new ColumnSetting("version", "softwaremanager.softwaretable.version", 0));
        softwareTableColumnSettings.add(new ColumnSetting("keyPair", "softwaremanager.softwaretable.keypair", 0));
    }

    public boolean isExportBase64() {
        return exportBase64;
    }

    public void setExportBase64(final boolean exportBase64) {
        this.exportBase64 = exportBase64;
    }
}
    
    