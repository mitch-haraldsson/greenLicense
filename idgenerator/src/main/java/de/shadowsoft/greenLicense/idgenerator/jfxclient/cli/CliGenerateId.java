package de.shadowsoft.greenLicense.idgenerator.jfxclient.cli;

import de.shadowsoft.greenLicense.common.cli.tool.ConsoleWriter;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.BasicIdGenerator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdGenerator;
import de.shadowsoft.greenLicense.common.tool.StringOps;
import de.shadowsoft.greenLicense.idgenerator.jfxclient.cli.output.CliOutGeneratedId;
import picocli.CommandLine;

import java.io.IOException;
import java.util.Base64;

@CommandLine.Command(name = "generate", description = "Generate system ID")
public class CliGenerateId implements Runnable {
    @CommandLine.Option(names = {"-h"}, description = "Bind to host name")
    private boolean host;
    @CommandLine.Option(names = {"-i"}, description = "Bind to IP address")
    private boolean ip;
    @CommandLine.Option(names = {"-m"}, description = "Bind to MAC address")
    private boolean mac;
    @CommandLine.Option(names = {"-o"}, description = "Bind to OS")
    private boolean os;
    @CommandLine.Option(names = {"-j", "--json"}, description = "Produce JSON output")
    private boolean useJson;


    @Override
    public void run() {
        CliOutGeneratedId res = new CliOutGeneratedId();
        final String selectorString = "0000" +
                (os ? "1" : "0") +
                (ip ? "1" : "0") +
                (host ? "1" : "0") +
                (mac ? "1" : "0");
        byte selector = Byte.parseByte(selectorString, 2);
        final IdGenerator generator = new BasicIdGenerator();
        try {
            res.setSelector(StringOps.toHex(selector));
            res.setId(new String(Base64.getEncoder().encode(generator.generateId(selector))));
        } catch (InterruptedException | IOException e) {
            res.setSuccess(false);
            res.addError("Unable to generate system ID", e);
        }
        ConsoleWriter.print(res, useJson);
    }
}
    
    