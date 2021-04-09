package de.shadowsoft.greenLicense.uiFramework.jfxframework.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;

public class AlertBoxSetting {

    private Alert.AlertType type;
    private String title;
    private String header;
    private String content;
    private final List<ButtonType> buttons;

    public AlertBoxSetting() {
        type = Alert.AlertType.INFORMATION;
        title = "";
        header = "";
        content = "";
        buttons = new ArrayList<>();
    }

    public void addButton(ButtonType button) {
        buttons.add(button);
    }

    public List<ButtonType> getButtons() {
        return buttons;
    }

    public Alert.AlertType getType() {
        return type;
    }

    public void setType(Alert.AlertType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
    
    