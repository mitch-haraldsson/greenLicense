package de.shadowsoft.greenLicense.manager.ui.cli.output.export;



import de.shadowsoft.greenLicense.core.cli.CliOutBase;

import java.util.ArrayList;
import java.util.List;

public class CliOutExportCollection extends CliOutBase {

    private List<CliOutExport> exports;

    public CliOutExportCollection() {
        exports = new ArrayList<>();
    }

    @Override
    public String formatOutput(final StringBuilder res) {
        for (CliOutExport export : exports) {

        }
        return res.toString();
    }

    public List<CliOutExport> getExports() {
        return exports;
    }

    public void setExports(final List<CliOutExport> exports) {
        this.exports = exports;
    }
}
    
    