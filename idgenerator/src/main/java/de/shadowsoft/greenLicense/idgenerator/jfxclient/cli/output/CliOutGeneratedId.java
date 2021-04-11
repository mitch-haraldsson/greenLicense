package de.shadowsoft.greenLicense.idgenerator.jfxclient.cli.output;

import de.shadowsoft.greenLicense.common.cli.CliOutBase;

public class CliOutGeneratedId extends CliOutBase {

    private String id;
    private String selector;

    public CliOutGeneratedId() {
        this.id = "";
        this.selector = "";
    }

    @Override
    public String formatOutput(final StringBuilder res) {
        res.append("Selector: ").append(selector).append("\n");
        res.append("Binding:\n").append(id);
        return res.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(final String selector) {
        this.selector = selector;
    }
}
    
    