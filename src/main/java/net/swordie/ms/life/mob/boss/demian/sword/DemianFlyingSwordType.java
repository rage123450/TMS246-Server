package net.swordie.ms.life.mob.boss.demian.sword;

import java.util.Arrays;

/**
 * Created on 17-8-2019.
 *
 * @author Asura
 */
public enum DemianFlyingSwordType {
    MainSword(0),
    SecondSword(1), // Smaller 2nd sword that is spawned if 7/7 stigma is reached.
    ;

    private byte val;

    DemianFlyingSwordType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static DemianFlyingSwordType getValBy(int val) {
        return Arrays.stream(values()).filter(v -> v.getVal() == val).findFirst().orElse(null);
    }
}
