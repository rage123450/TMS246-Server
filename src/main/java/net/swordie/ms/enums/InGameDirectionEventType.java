package net.swordie.ms.enums;

import net.swordie.ms.util.Util;

/**
 * @author Sjonnie
 * Created on 8/31/2018.
 */
public enum InGameDirectionEventType {
    ForcedAction(0),
    Delay(1),
    EffectPlay(2),
    ForcedInput(3),
    PatternInputRequest(4),
    CameraMove(5),// automated send delay
    CameraOnCharacter(6),
    CameraZoom(7),// automated send delay
    CameraReleaseFromUserPoint(8),
    VansheeMode(9),
    FaceOff(10),
    Monologue(11),
    MonologueScroll(12),
    AvatarLookSet(13),
    RemoveAdditionalEffect(14),
    RemoveAdditionalEffect_2(15),
    ForcedMove(16),
    ForcedFlip(17),
    Unk18(18),
    InputUI(19),
    CloseUI(20),
    Monologue_2(21),
    Unk22(22),
    ;

    private int val;

    InGameDirectionEventType(int val) {
        this.val = val;
    }

    public static InGameDirectionEventType getByVal(byte val) {
        return Util.findWithPred(values(), v -> v.getVal() == val);
    }

    public int getVal() {
        return val;
    }
}
