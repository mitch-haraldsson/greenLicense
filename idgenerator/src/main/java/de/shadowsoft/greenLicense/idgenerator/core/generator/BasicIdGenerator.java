package de.shadowsoft.greenLicense.idgenerator.core.generator;

import de.shadowsoft.greenLicense.common.license.generator.Generator;
import de.shadowsoft.greenLicense.common.license.generator.Selector;
import de.shadowsoft.greenLicense.common.license.generator.mac.MacEncrypt;
import de.shadowsoft.greenLicense.idgenerator.core.Sysinfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicIdGenerator implements IdGenerator {
    private static final Logger LOGGER = LogManager.getLogger(BasicIdGenerator.class);

    @Override
    public byte[] generateId(final Selector selector) {
        byte[] result = null;
        try {
            switch (selector.getId()) {
                case 0x5C:
                    byte[] mac = getMacAddressesBytes();
                    if (mac != null) {
                        Generator macGenerator = new MacEncrypt(mac);
                        Thread t = new Thread(macGenerator);
                        t.start();
                        t.join();
                        result = macGenerator.getResult();
                    }
                    break;

                case 0xB6:
                    byte[] computerName = new Sysinfo().getComputername().getBytes();
                    Generator macGenerator = new MacEncrypt(computerName);
                    Thread t = new Thread(macGenerator);
                    t.start();
                    t.join();
                    result = macGenerator.getResult();
                    break;
            }
        } catch (InterruptedException e) {
            LOGGER.error("Unable to calculate ID", e);
        }
        return result;
    }

    private byte[] getMacAddressesBytes() {
        List<byte[]> bytes = new ArrayList<>();
        try {
            Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = it.next();
                if (networkInterface != null) {
                    if (networkInterface.getHardwareAddress() != null) {
                        bytes.add(networkInterface.getHardwareAddress());
                    }
                }
            }
            byte[] mac = null;
            for (byte[] ba : bytes) {
                mac = ArrayUtils.addAll(mac, ba);
            }
            return mac;
        } catch (SocketException e) {
            LOGGER.error("Unable to enumerate network adapters", e);
        }
        return null;
    }

}
    
    