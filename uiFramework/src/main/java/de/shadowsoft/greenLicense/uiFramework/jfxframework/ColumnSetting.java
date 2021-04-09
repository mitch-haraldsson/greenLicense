package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import java.util.ArrayList;
import java.util.List;


public class ColumnSetting {
    private String id;
    private List<String> styleClass;
    private String title;
    private double width;

    public ColumnSetting() {
        this.title = "unnamed";
        this.width = 0;
        this.id = "noid";
        this.styleClass = new ArrayList<>();
    }

    public ColumnSetting(String id, String title, double width) {
        this(id, title, width, new ArrayList<>());
    }

    public ColumnSetting(String id, String title, double width, List<String> styleClass) {
        this.title = title;
        this.width = width;
        this.id = id;
        this.styleClass = styleClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(List<String> styleClass) {
        this.styleClass = styleClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
    
    