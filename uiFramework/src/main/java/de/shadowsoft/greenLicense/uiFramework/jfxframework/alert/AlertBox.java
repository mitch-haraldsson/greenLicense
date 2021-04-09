package de.shadowsoft.greenLicense.uiFramework.jfxframework.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertBox {

    private final AlertBoxSetting setting;

    public AlertBox(AlertBoxSetting setting) {
        this.setting = setting;
    }

    public void show() {
        showAndGet();
    }

    public Optional<ButtonType> showAndGet() {
        final Alert alert = new Alert(setting.getType());
        alert.setTitle(setting.getTitle());
        alert.setHeaderText(setting.getHeader());
        alert.setContentText(setting.getContent());
        if (setting.getButtons().size() > 0) {
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(setting.getButtons());
        }

        return alert.showAndWait();
    }
}
    
    