package org.alpha_sync_java.domain.communication;

public class PlainBit implements URLBit {
    private String plainBit;

    public PlainBit(String plainBit) {
        this.plainBit = plainBit;
    }

    @Override
    public String toString() {
        return plainBit;
    }
}
