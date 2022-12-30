package net.swordie.ms.enums;

/**
 * @author Sjonnie
 * Created on 3/19/2019.
 */
public enum WorldId {
    艾麗亞(0),
    普力特(1),
    琉德(2),
    優伊娜(3),
    愛麗西亞(4),
    殺人鯨(6),
    Reboot(45),
    Tespia(100),
    ;

    private int val;

    WorldId(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}