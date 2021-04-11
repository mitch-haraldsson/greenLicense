package de.shadowsoft.greenLicense.common.license.generator.core;

import de.shadowsoft.greenLicense.common.license.generator.Generator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdResult;

import java.io.IOException;

public class IdCreator {

    public byte[] createId(final IdResult res) throws InterruptedException, IOException {
        byte[] condensedResultBytes = new ResultCondenser().condense(res);
        Generator macGenerator = new ResultEncrypt(condensedResultBytes);
        Thread t = new Thread(macGenerator);
        t.start();
        t.join();
        return macGenerator.getResult();
    }
}
    
    