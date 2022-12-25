package net.swordie.ms.enums;

import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;

import java.util.Arrays;

/**
 * Created on 11/23/2017.
 */
public enum InvType {
    UNDEFINED(0),
    EQUIPPED(-1),
    EQUIP(1),
    CONSUME(2),
    INSTALL(3),
    ETC(4),
    CASH(5),
    CASH_EQUIP(6),
    HAIR(7),
    FACE(8),
    SKIN(9),
    ;

    private byte val;

    InvType(int val) {
        this((byte) val);
    }

    InvType(byte val) {
        this.val = val;
    }

    public byte getVal() {
        return val;
    }

    public static InvType getInvTypeByVal(int val) {
        return Arrays.stream(InvType.values()).filter(t -> t.getVal() == val).findFirst().orElse(null);
    }

    public static InvType getInvTypeByString(String subMap) {
        subMap = subMap.toLowerCase();
        InvType res = null;
        switch(subMap) {
            case "cash":
            case "pet":
                res = CASH;
                break;
            case "consume":
            case "special":
            case "use":
                res = CONSUME;
                break;
            case "etc":
                res = ETC;
                break;
            case "install":
            case "setup":
                res = INSTALL;
                break;
            case "eqp":
            case "equip":
                res = EQUIP;
                break;
            case "hair":
                res = HAIR;
                break;
            case "face":
                res = FACE;
                break;
        }
        return res;
    }

    public static InvType getInvTypeByItemID(int itemid) {
        byte type = (byte) (itemid / 1000000);
        ItemInfo item = ItemData.getItemInfoByID(itemid);
        if (type == 1 && item.isCash()) {
            return InvType.CASH_EQUIP;
        }
        if (type < 1 || type > 5) {
            return InvType.UNDEFINED;
        }
        return InvType.getByType(type);
    }

    public static InvType getByType(byte type) {
        for (InvType l : values()) {
            if (l.getVal() == type) {
                return l;
            }
        }
        return null;
    }

    public boolean isStackable() {
        return this != EQUIP && this != EQUIPPED && this != CASH;
    }
}
