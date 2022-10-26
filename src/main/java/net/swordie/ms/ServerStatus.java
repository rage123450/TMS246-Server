package net.swordie.ms;

/**
 * Created on 11/3/2017.
 */
public enum ServerStatus {
    NORMAL(0),
    BUSY(1),
    FULL(2);

    private byte value;

    ServerStatus (int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
