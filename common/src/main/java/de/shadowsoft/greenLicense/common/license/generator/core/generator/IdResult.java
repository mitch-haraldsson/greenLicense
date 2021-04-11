package de.shadowsoft.greenLicense.common.license.generator.core.generator;

import java.util.ArrayList;
import java.util.List;

public class IdResult {

    private byte[] hostname;
    private List<byte[]> ip;
    private List<byte[]> macs;
    private byte[] os;
    private byte selector;

    public IdResult() {
        selector = 0;
        hostname = null;
        macs = new ArrayList<>();
        os = null;
        ip = new ArrayList<>();
    }

    public byte[] getHostname() {
        return hostname;
    }

    public void setHostname(final byte[] hostname) {
        this.hostname = hostname;
    }

    public List<byte[]> getIp() {
        return ip;
    }

    public void setIp(final List<byte[]> ip) {
        this.ip = ip;
    }

    public List<byte[]> getMacs() {
        return macs;
    }

    public void setMacs(final List<byte[]> macs) {
        this.macs = macs;
    }

    public byte[] getOs() {
        return os;
    }

    public void setOs(final byte[] os) {
        this.os = os;
    }

    public byte getSelector() {
        return selector;
    }

    public void setSelector(final byte selector) {
        this.selector = selector;
    }
}
    
    