package net.swordie.ms.enums;

import java.util.Arrays;

/**
 * Created on 6/7/2018.
 */
public enum UserEffectType {
    LevelUp(0),
    SkillUse(1),
    SkillUseBySummoned(2),
    Unk3(3), // new
    SkillAffected(4),
    SkillAffected_Ex(5),
    SkillAffected_Select(6),
    SkillSpecialAffected(7),
    Quest(8),
    Pet(9),
    SkillSpecial(10),
    Resist(11),
    ProtectOnDieItemUse(12),
    Unk4(13),
    PlayPortalSE(14),
    JobChanged(15),
    QuestComplete(16),
    IncDecHPEffect(17),
    BuffItemEffect(18),
    SquibEffect(19),
    MonsterBookCardGet(20),
    LotteryUse(21),
    ItemLevelUp(22),
    ItemMaker(23),
    FieldMesoItemConsumed(24), // new
    ExpItemConsumed(25),
    FieldExpItemConsumed(26),
    ReservedEffect(27),
    unk27(28), // old unknown
    UpgradeTombItemUse(29),
    BattlefieldItemUse(30),

    unk30(30), // old unknown
    AvatarOriented(31),
    AvatarOrientedRepeat(32),
    AvatarOrientedMultipleRepeat(33),
    IncubatorUse(34),
    PlaySoundWithMuteBGM(35),
    PlayExclSoundWithDownBGM(36),
    SoulStoneUse(37),
    IncDecHPEffect_EX(38), // correct with 202.3
    IncDecHPRegenEffect(39),
    EffectUOL(40),
    PvPRage(41),
    PvPChampion(42),
    PvPGradeUp(43),
    PvPRevive(44),
    PvPJobEffect(45),
    FadeInOut(46),
    MobSkillHit(47),
    unk48(48), // new
    BlindEffect(49),
    BossShieldCount(50),
    ResetOnStateForOnOffSkill(51),
    JewelCraft(52),
    ConsumeEffect(53),
    PetBuff(54),
    LotteryUIResult(55),
    LeftMonsterNumber(56),
    ReservedEffectRepeat(57),
    RobbinsBomb(58),
    SkillMode(59),
    ActQuestComplete(60),
    Point(61),
    SpeechBalloon(62),
    TextEffect(63),
    SkillPreLoopEnd(64),
    Aiming(65),
    Unk66(66),
    PickUpItem(67),
    BattlePvP_IncDecHp(68),
    CatchEffect(69),
    FailCatchEffect(70),
    BiteAttack_ReceiveSuccess(71),
    BiteAttack_ReceiveFail(72),
    IncDecHPEffect_Delayed(73),
    // new
    Unk74(74),
    Unk75(75),
    BlackMageEffect(76), // Black Mage  Effect (100017)
    Unk77(77),
    Unk78(78),
    RedChat(79),
    Unk80(80),
    Unk81(81),
    Unk82(82),
    Unk83(83),
    UpgradePotionMsg(84),
    Unk85(85),
    FamiliarEscape(86),
    Unk87(87),
    Unk88(88),
    SomeUpgradeEffectOnUser(89), // Looks like V skill or somehshit
    ;

    private byte val;

    UserEffectType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static UserEffectType getByVal(int val) {
        return Arrays.stream(values()).filter(uet -> uet.getVal() == val).findAny().orElse(null);
    }
}
