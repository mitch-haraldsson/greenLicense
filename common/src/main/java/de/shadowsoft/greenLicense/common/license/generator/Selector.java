package de.shadowsoft.greenLicense.common.license.generator;


public class Selector {

    private final byte id;
    private final String name;

    public Selector(final byte id, final String name) {
        this.id = id;
        this.name = name;
    }

    public final byte getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    @Override
    public final String toString() {
        return name;
    }

}
    
    