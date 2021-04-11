package de.shadowsoft.greenLicense.common.license.generator.core.generator;


import java.io.IOException;

public interface IdGenerator {
    byte[] generateId(final byte selector) throws InterruptedException, IOException;

}
    
    