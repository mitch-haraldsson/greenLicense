package de.shadowsoft.greenLicense.common.license.generator.core;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SysInfo {

    public String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else return env.getOrDefault("HOSTNAME", "Unknown Computer");
    }

    public String getOs() {
        return System.getProperty("os.name");
    }
    public List<byte[]> getAllIps() throws SocketException {
        List<byte[]> bytes = new ArrayList<>();
        Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
        while (it.hasNext()) {
            NetworkInterface networkInterface = it.next();
            if (networkInterface != null && networkInterface.getInterfaceAddresses() != null) {
                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    if (!address.getAddress().isLoopbackAddress() && !address.getAddress().isLinkLocalAddress()) {
                        bytes.add(address.getAddress().getAddress());
                    }
                }
            }
        }
        return bytes;
    }

    public List<byte[]> getMacAddresses() throws SocketException {
        List<byte[]> bytes = new ArrayList<>();
        Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
        while (it.hasNext()) {
            NetworkInterface networkInterface = it.next();
            if (networkInterface != null) {
                if (networkInterface.getHardwareAddress() != null) {
                    bytes.add(networkInterface.getHardwareAddress());
                }
            }
        }
        return bytes;
    }
}
    
    