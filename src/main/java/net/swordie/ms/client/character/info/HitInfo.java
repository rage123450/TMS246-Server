package net.swordie.ms.client.character.info;

import net.swordie.ms.util.Position;

/**
 * Created on 1/11/2018.
 */
public class HitInfo {
    public int hpDamage;
    public int templateID;
    public int mobID;
    public int mpDamage;
    public byte type;
    public int SkillId;
    public int otherUserID;
    public boolean isCrit;
    public int action;
    public boolean isGuard;
    public int hitAction;
    public int reflectDamage;
    public int userSkillID;
    public short obstacle;
    public byte elemAttr;

    public int damagedTime;
    public int mobIdForMissCheck;
    public boolean isLeft;
    public int reducedDamage;
    public byte reflect;
    public byte guard;

    public int reflectMobID;
    public Position hitPos;
    public Position userHitPos;
    public byte stance; // mask: 0x1 if true, 0x2 if custom skillID. Default is 33110000 (jaguar boost)
    public int stanceSkillID;

    public int objid;

    @Override
    public String toString() {
        return "HitInfo {" +
                "hpDamage = " + hpDamage +
                //", templateID = " + templateID +
                ", mobID = " + mobID +
                ", mpDamage = " + mpDamage +
                ", type = " + type +
                ", SkillId = " + SkillId +
                ", otherUserID = " + otherUserID +
                ", isCrit = " + isCrit +
                ", action = " + action +
                ", isGuard = " + isGuard +
                ", hitAction = " + hitAction +
                ", stance = " + stance +
                ", reflectDamage = " + reflectDamage +
                ", userSkillID = " + userSkillID +
                '}';
    }
}
