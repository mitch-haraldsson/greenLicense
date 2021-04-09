package de.shadowsoft.greenLicense.common.license.generator;


public class Selector {

    private final int id;
    private final String name;

    public Selector(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public final int getId() {
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
    
    