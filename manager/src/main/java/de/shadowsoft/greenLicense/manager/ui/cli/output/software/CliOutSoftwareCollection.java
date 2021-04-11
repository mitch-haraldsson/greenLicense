package de.shadowsoft.greenLicense.manager.ui.cli.output.software;

import de.shadowsoft.greenLicense.common.cli.CliOutBase;

import java.util.ArrayList;
import java.util.List;

public class CliOutSoftwareCollection extends CliOutBase {

    private List<CliOutSoftware> software;

    public CliOutSoftwareCollection() {
        software = new ArrayList<>();
    }

    @Override
    public String formatOutput(final StringBuilder res) {
        for (CliOutSoftware swl : software) {
            res.append("\n");
            res.append(swl.getId()).append("\t");
            res.append(swl.getName()).append("\t");
            res.append(swl.getVersion()).append("\t");
            res.append(swl.getLicenseVersion());
        }
        return res.toString();
    }

    public List<CliOutSoftware> getSoftware() {
        return software;
    }

    public void setSoftware(final List<CliOutSoftware> software) {
        this.software = software;
    }
}
    
    