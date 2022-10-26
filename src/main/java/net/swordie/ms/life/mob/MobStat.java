package net.swordie.ms.life.mob;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public enum MobStat {
    IndiePDR(0),
    IndieMDR(1),
    IndieUNK(2),
    IndieUNK2(3),
    UNK4(4),
    PAD(5),
    PDR(6),
    MAD(7),
    MDR(8),
    ACC(9),
    EVA(10),
    Speed(11),
    Stun(12),

    Freeze(13),
    Poison(14),
    Seal(15),
    Darkness(16),
    PowerUp(17),
    MagicUp(18),
    PGuardUp(19),
    MGuardUp(20),

    PImmune(21),
    MImmune(22),
    Web(23),
    HardSkin(24),
    Ambush(25),
    Venom(26),
    Blind(27),
    SealSkill(28),

    Dazzle(29),
    PCounter(30), // nOption = % of dmg, mOption = % chance
    MCounter(31),
    RiseByToss(32),
    BodyPressure(33),
    Weakness(34),
    Showdown(35),
    MagicCrash(36),

    DamagedElemAttr(37),
    Dark(-1),
    Mystery(-1),
    Unk205_33(38), // could be swapped with AddDamParty
    AddDamParty(39),
    HitCriDamR(40), // *
    Fatality(41),
    Lifting(42),
    DeadlyCharge(43),

    Smite(44),
    AddDamSkill(45),
    Incizing(46),
    DodgeBodyAttack(47),
    DebuffHealing(48),
    FinalDmgReceived(49),
    BodyAttack(50),
    TempMoveAbility(51),

    FixDamRBuff(52),
    SpiritGate(53),
    ElementDarkness(54),
    AreaInstallByHit(55),
    BMageDebuff(56),
    JaguarProvoke(57),
    JaguarBleeding(58),
    DarkLightning(59),
    PinkBeanFlowerPot(60),

    BattlePvPHelenaMark(61),
    PsychicLock(62),
    PsychicLockCoolTime(63),
    PsychicGroundMark(63),

    PowerImmune(64),
    PsychicForce(65),
    MultiPMDR(66),
    ElementResetBySummon(67),

    BahamutLightElemAddDam(68),
    UmbralBrand(69),
    //
    BossPropPlus(81),
    Unk65(-1),
    MultiDamSkill(82),
    RWLiftPress(83),
    RWChoppingHammer(84),
    TimeBomb(85),
    Treasure(86),
    AddEffect(87),

    Unknown1(74),
    Unknown2(75),
    Invincible(76),
    Unknown75(77),
    Unknown76(78),
    Curseweaver(79),
    Unknown77(80), // *
    Unknown78(81),
    Unknown79(82),
    Unknown80(83),
    Unknown81(84),
    Unk205_85(85),
    Unk205_86(86),
    Explosion(87), // *
    HangOver(88),
    Unknown84(89),
    BurnedInfo(90), // *
    InvincibleBalog(91),
    ExchangeAttack(92),

    ExtraBuffStat(93),
    LinkTeam(94), // *
    SoulExplosion(95),
    SeperateSoulP(96), // applied to origin
    SeperateSoulC(97), // applied to the Copy
    Ember(98),
    TrueSight(99),
    Laser(100),
    Unk199_97(101),
    Unk188_97(102),
    Unk199_99(103),
    Unk199_100(104),
    Unk199_101(105),
    Unk199_102(106),
    Unk199_103(107),
    Unk199_104(108),
    Unk205_109(109),
    Unk205_110(110),

    No(-1),
    ;

    public static final int LENGTH = 5;
    private int val, pos, bitPos;

    MobStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    MobStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

    public int getPos() {
        return pos;
    }

    public int getVal() {
        return val;
    }

    public boolean isMovementAffectingStat() {
        switch(this) {
            case Speed:
            case Stun:
            case Freeze:
            case RiseByToss:
            case Lifting:
            case Smite:
            case TempMoveAbility:
            case RWLiftPress:
                return true;
            default:
                return false;
        }
    }

    public int getBitPos() {
        return bitPos;
    }

    public static void main(String[] args) {
//        int change = 39;
//        for (OutHeader header : values()) {
//            int val = header.getValue();
//            if (val >= SET_FIELD.getValue()) {
//                val += change;
//            }
//            System.out.printf("%s(%d),%n", header, val);
//        }
        File file = new File(ServerConstants.DIR + "\\src\\main\\java\\net\\swordie\\ms\\life\\mob\\MobStat.java");
        int change = 1;
        MobStat checkOp = null;
        try(Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains(",") && line.contains("(")) {
                    String[] split = line.split("[()]");
                    String name = split[0];
                    if (!Util.isNumber(split[1])) {
                        System.out.println(line);
                        continue;
                    }
                    int val = Integer.parseInt(split[1]);
                    MobStat ih = Arrays.stream(MobStat.values()).filter(o -> o.toString().equals(name.trim())).findFirst().orElse(null);
                    if (ih != null) {
                        MobStat start = Unknown77;
                        if (ih.ordinal() >= start.ordinal() && ih.ordinal() < LinkTeam.ordinal()) {
                            if (line.contains("*")) {
                                checkOp = ih;
                            }
                            val += change;
                            System.out.println(String.format("%s(%d), %s", name, val, start == ih ? "// *" : ""));
                        } else {
                            System.out.println(line);
                        }
                    } else {
                        System.out.println(line);
                    }
                } else {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (checkOp != null) {
            System.err.println(String.format("Current op (%s) contains a * (= updated). Be sure to check for overlap.", checkOp));
        }
    }
}