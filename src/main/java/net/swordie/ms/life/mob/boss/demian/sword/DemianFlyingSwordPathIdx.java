package net.swordie.ms.life.mob.boss.demian.sword;

import java.util.Arrays;

/**
 * Created on 17-8-2019.
 *
 * @author Asura
 */
public enum DemianFlyingSwordPathIdx {
    Creation(0),
    Bouncing1(1),
    Bouncing2(2),
    Targeting(8),
    ;

    private short val;

    DemianFlyingSwordPathIdx(int val) {
        this.val = (short) val;
    }

    public short getVal() {
        return val;
    }

    public static DemianFlyingSwordPathIdx getByVal(short val) {
        return Arrays.stream(values()).filter(p -> p.getVal() == val).findFirst().orElse(null);
    }
}
