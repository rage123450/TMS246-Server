package net.swordie.ms.life.mob.boss.demian.sword;

/**
 * Created on 17-8-2019.
 *
 * @author Asura
 */
public enum DemianFlyingSwordNodeType {
    Bouncing(1),
    Targeting(2),
    ;

    private byte val;

    DemianFlyingSwordNodeType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}
