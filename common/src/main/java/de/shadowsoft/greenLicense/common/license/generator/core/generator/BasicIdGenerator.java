package de.shadowsoft.greenLicense.common.license.generator.core.generator;

import de.shadowsoft.greenLicense.common.license.generator.core.IdCreator;
import de.shadowsoft.greenLicense.common.license.generator.core.SysInfo;

import java.io.IOException;

public class BasicIdGenerator implements IdGenerator {

    @Override
    public byte[] generateId(final byte selector) throws InterruptedException, IOException {
        IdResult res = new IdResult();
        SysInfo info = new SysInfo();
        res.setSelector(selector);
        String selectorString = String.format("%8s", Integer.toBinaryString(selector & 0xFF)).replace(' ', '0');

        for (int i = 0; i < selectorString.length(); i++) {
            if (selectorString.charAt(i) == '1') {
                switch (i) {
                    case 7:
                        res.getMacs().addAll(info.getMacAddresses());
                        break;

                    case 6:
                        res.setHostname(new SysInfo().getComputerName().getBytes());
                        break;

                    case 5:
                        res.getIp().addAll(info.getAllIps());
                        break;

                    case 4:
                        res.setOs(new SysInfo().getOs().getBytes());
                        break;
                }
            }

        }
        return new IdCreator().createId(res);
    }
}
    
    