package net.swordie.ms.client.character;

public class Mannequin {
    private int value;
    private int baseProb;
    private int baseColor;
    private int addColor;

    public Mannequin(int value, int baseProb, int baseColor, int addColor) {
        this.value = value;
        this.baseProb = baseProb;
        this.baseColor = baseColor;
        this.addColor = addColor;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBaseProb() {
        return this.baseProb;
    }

    public void setBaseProb(int baseProb) {
        this.baseProb = baseProb;
    }

    public int getBaseColor() {
        return this.baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public int getAddColor() {
        return this.addColor;
    }

    public void setAddColor(int addColor) {
        this.addColor = addColor;
    }
}
