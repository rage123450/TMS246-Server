package net.swordie.ms.life.mob.boss.demian.stigma;

import java.util.Arrays;

/**
 * Created on 18-8-2019.
 *
 * @author Asura
 */
public enum StigmaDeliveryType {
    Start(0),
    Success(1),
    Cancel(2),
    ;

    private int val;

    StigmaDeliveryType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static StigmaDeliveryType getValBy(int val) {
        return Arrays.stream(values()).filter(v -> v.getVal() == val).findFirst().orElse(null);
    }
}
