package net.swordie.ms.world.field.fieldeffect;

import net.swordie.ms.util.Util;

/**
 * Created on 3/26/2018.
 */
public enum FieldEffectType {
    Summon(0),
    Tremble(1),
    Object(2),
    ObjectDisable(3),
    Screen(4),
    PlaySound(5),
    MobHPTag(6),
    ChangeBGM(7),
    BGMVolumeOnly(8),
    BGMVolume(9),
    Unk10(10),
    Unk11(11),
    Unk12(12),
    Unk13(13),
    Unk14(14),
    RewardRoulette(15),
    TopScreen(16),
    ScreenDelayed(17),
    TopScreenDelayed(18),
    ScreenAutoLetterBox(19),
    FloatingUI(20),
    Unk21(21),
    Blind(22),
    GreyScale(23),
    OnOffLayer(24),
    Overlap(25),
    OverlapDetail(26),
    RemoveOverlapDetail(27),
    Unk28(28), // str
    ColorChange(29),
    StageClear(30),
    TopScreenWithOrigin(31),
    SpineScreen(32),
    OffSpineScreen(33),
    // 34 doesn't exist
    Unk33(35),
    Unk34(36),
    ;

    private byte val;

    FieldEffectType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static FieldEffectType getByVal(int val) {
        return Util.findWithPred(values(), v -> v.getVal() == val);
    }
}
