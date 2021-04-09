package de.shadowsoft.greenLicense.uiFramework.jfxframework;

import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.ResourceBundle;

public class ColumnLoader {

    public static void load(final List<ColumnSetting> columns, final TableView<?> tableView, final ResourceBundle resource) {
        Platform.runLater(() -> {
            for (ColumnSetting col : columns) {
                TableColumn tblCol = new TableColumn(resource.getString(col.getTitle()));
                tblCol.setMinWidth(col.getWidth());
                tblCol.setCellValueFactory(
                        new PropertyValueFactory<TMBase, String>(col.getId()));
                tblCol.getStyleClass().addAll(col.getStyleClass());
                tableView.getColumns().add(tblCol);
            }
        });
    }

}
    
    