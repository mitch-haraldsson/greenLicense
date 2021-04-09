package de.shadowsoft.greenLicense.idgenerator.core.generator;

import de.shadowsoft.greenLicense.common.license.generator.Selector;

public interface IdGenerator {
    byte[] generateId(final Selector selector);

}
    
    