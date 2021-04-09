package de.shadowsoft.greenLicense.uiFramework.jfxframework.alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertBoxHelper {
    public static void showInfoBox(String title, String header, String content) {
        showBox(title, header, content, Alert.AlertType.INFORMATION);
    }

    public static void showWarningBox(String title, String header, String content) {
        showBox(title, header, content, Alert.AlertType.WARNING);
    }

    public static void showErrorBox(String title, String header, String content) {
        showBox(title, header, content, Alert.AlertType.ERROR);
    }

    public static void showConfirmBox(String title, String header, String content) {
        showBox(title, header, content, Alert.AlertType.CONFIRMATION);
    }

    public static void showPlainBox(String title, String header, String content) {
        showBox(title, header, content, Alert.AlertType.NONE);
    }

    public static void showBox(String title, String header, String content, Alert.AlertType type) {
        Platform.runLater(() -> {
            AlertBoxSetting abs = new AlertBoxSetting();
            abs.setTitle(title);
            abs.setHeader(header);
            abs.setContent(content);
            abs.setType(type);
            AlertBox box = new AlertBox(abs);
            box.show();
        });
    }


}
    
    