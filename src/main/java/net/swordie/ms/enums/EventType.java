package net.swordie.ms.enums;

import java.util.Arrays;

public enum EventType {
    NormalBalrog(0, "Normal Balrog"),
    MysticBalrog(1, "Mystic Balrog"),
    EasyZakum(2, "Easy Zakum"),
    NormalZakum(3, "Normal Zakum"),
    ChaosZakum(4, "Chaos Zakum"),
    Queen(5, "Normal Crimson Queen"),
    CQueen(6, "Chaos Crimson Queen"),
    Clown(7, "Normal Pierre"),
    CClown(8, "Chaos Pierre"),
    VonBon(9, "Normal VonBon"),
    CVonBon(10, "Chaos VonBon"),
    Vellum(11, "Normal Vellum"),
    CVellum(12, "Chaos Vellum"),
    Dorothy(13, "Dorothy"),
    PinkBean(14, "PinkBean"),
    CPinkBean(15, "Chaos Pink Bean"),
    EZakum(16, "Zakum"),
    NZakum(17, "Normal Zakum"),
    CZakum(18, "Chaos Zakum"),
    Lotus(19, "Lotus"),
    CLotus(20, "Chaos Lotus"),
    Cygnus(21, "Cygnus"),
    CCygnus(22, "Chaos Cygnus"),
    EHorntail(23, "Easy Horntail"),
    Horntail(24, "Horntail"),
    CHorntail(25, "Chaos Horntail"),
    ;

    private int val;
    private String name;

    EventType(int val) {
        this.val = val;
    }

    EventType(int val, String name) {
        this.val = val;
        this.name = name;
    }

    public static EventType getByVal(int val) {
        return Arrays.stream(values()).filter(gdt -> gdt.getVal() == val).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

    public int getVal() {
        return val;
    }

}