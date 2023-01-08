package net.swordie.ms.client.jobs;

import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.LinkSkill;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.runestones.RuneStone;
import net.swordie.ms.client.character.skills.ForceAtom;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.*;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.BeastTamer;
import net.swordie.ms.client.jobs.adventurer.magician.Bishop;
import net.swordie.ms.client.jobs.adventurer.pirate.Jett;
import net.swordie.ms.client.jobs.adventurer.warrior.DarkKnight;
import net.swordie.ms.client.jobs.cygnus.BlazeWizard;
import net.swordie.ms.client.jobs.cygnus.Mihile;
import net.swordie.ms.client.jobs.cygnus.NightWalker;
import net.swordie.ms.client.jobs.cygnus.WindArcher;
import net.swordie.ms.client.jobs.legend.Evan;
import net.swordie.ms.client.jobs.legend.Mercedes;
import net.swordie.ms.client.jobs.legend.Phantom;
import net.swordie.ms.client.jobs.legend.Shade;
import net.swordie.ms.client.jobs.resistance.Xenon;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.*;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.loaders.containerclasses.SkillStringInfo;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;
import org.apache.log4j.Logger;
import org.python.modules._imp;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.jobs.adventurer.warrior.DarkKnight.FINAL_PACT_INFO;
import static net.swordie.ms.client.jobs.adventurer.warrior.Paladin.PARASHOCK_GUARD;


/**
 * Created on 1/2/2018.
 */
public abstract class Job {
    private static final Logger log = Logger.getLogger(Job.class);
    protected Char chr;
    protected Client c;

    public static final int HYPER_STAT_ARCANE_FORCE = 80000421;

    public static final int MONOLITH = 80011261;
    public static final int FURY_TOTEM = 80011824;
    public static final int ELEMENTAL_SYLPH = 80001518;
    public static final int FLAME_SYLPH = 80001519;
    public static final int THUNDER_SYLPH = 80001520;
    public static final int ICE_SYLPH = 80001521;
    public static final int EARTH_SYLPH = 80001522;
    public static final int DARK_SYLPH = 80001523;
    public static final int HOLY_SYLPH = 80001524;
    public static final int SALAMANDER_SYLPH = 80001525;
    public static final int ELECTRON_SYLPH = 80001526;
    public static final int UNDINE_SYLPH = 80001527;
    public static final int GNOME_SYLPH = 80001528;
    public static final int DEVIL_SYLPH = 80001529;
    public static final int ANGEL_SYLPH = 80001530;

    public static final int ELEMENTAL_SYLPH_2 = 80001715;
    public static final int FLAME_SYLPH_2 = 80001716;
    public static final int THUNDER_SYLPH_2 = 80001717;
    public static final int ICE_SYLPH_2 = 80001718;
    public static final int EARTH_SYLPH_2 = 80001719;
    public static final int DARK_SYLPH_2 = 80001720;
    public static final int HOLY_SYLPH_2 = 80001721;
    public static final int SALAMANDER_SYLPH_2 = 80001722;
    public static final int ELECTRON_SYLPH_2 = 80001723;
    public static final int UNDINE_SYLPH_2 = 80001724;
    public static final int GNOME_SYLPH_2 = 80001725;
    public static final int DEVIL_SYLPH_2 = 80001726;
    public static final int ANGEL_SYLPH_2 = 80001727;

    public static final int WHITE_ANGELIC_BLESSING = 80000155;
    public static final int WHITE_ANGELIC_BLESSING_2 = 80001154;
    public static final int LIGHTNING_GOD_RING = 80001262;
    public static final int LIGHTNING_GOD_RING_2 = 80011178;
    public static final int GUARD_RING = 80011149;
    public static final int SUN_RING = 80010067;
    public static final int RAIN_RING = 80010068;
    public static final int RAINBOW_RING = 80010069;
    public static final int SNOW_RING = 80010070;
    public static final int LIGHTNING_RING = 80010071;
    public static final int WIND_RING = 80010072;

    public static final int BOSS_SLAYERS = 91001022;
    public static final int UNDETERRED = 91001023;
    public static final int FOR_THE_GUILD = 91001024;

    public static final int REBOOT = 80000186;
    public static final int REBOOT2 = 80000187;

    public static final int MAPLERUNNER_DASH = 80001965;

    public static final int[] REMOVE_ON_STOP = new int[]{
            MAPLERUNNER_DASH
    };

    public static final int[] REMOVE_ON_WARP = new int[]{
            MAPLERUNNER_DASH
    };

    public static final int MAPLE_TREE_OF_PEACE = 80002593;

    public static final int TERMS_AND_CONDITIONS_LINK = 80001155; // Angelic Buster Link

    public static final int KNIGHTS_WATCH_LINK = 80001140; // Mihile Link

    // Common V Skills
    public static final int ROPE_LIFT = 400001000;
    public static final int DECENT_MYSTIC_DOOR_V = 400001001;
    public static final int DECENT_SHARP_EYES_V = 400001002;
    public static final int DECENT_HYPER_BODY_V = 400001003;
    public static final int DECENT_COMBAT_ORDERS_V = 400001004;
    public static final int DECENT_ADV_BLESSING_V = 400001005;
    public static final int DECENT_SPEED_INFUSION_V = 400001006;
    public static final int 實用的祈禱 = 400001020;
    public static final int ERDA_NOVA = 400001008;
    public static final int WILL_OF_ERDA = 400001009;
    public static final int ERDA_SHOWER = 400001036;

    // First Branch V Skills
    public static final int 幻靈武具 = 400011000;
    public static final int MANA_OVERLOAD = 400021000;
    public static final int GUIDED_ARROW = 400031000;
    public static final int GUIDED_ARROW_ATOM = 400031001;
    public static final int VENOM_BURST = 400041000;
    public static final int VENOM_BURST_ATTACK = 400041030;
    public static final int LOADED_DICE = 400051000;
    public static final int LUCKY_DICE = 400051001;

    // Second Branch V Skills
    public static final int 鋼鐵之軀 = 400011066;
    public static final int ETHEREAL_FORM = 400021060;
    public static final int VICIOUS_SHOT = 400031023;
    public static final int LAST_RESORT = 400041032;
    public static final int OVERDRIVE = 400051033;

    // Race V Skills
    public static final int FREUDS_WISDOM = 400001024;
    public static final int FREUDS_WISDOM_1 = 400001025;
    public static final int FREUDS_WISDOM_2 = 400001026;
    public static final int FREUDS_WISDOM_3 = 400001027;
    public static final int FREUDS_WISDOM_4 = 400001028;
    public static final int FREUDS_WISDOM_5 = 400001029;
    public static final int FREUDS_WISDOM_6 = 400001030;

    public static final int RESISTANCE_INFANTRY_1 = 400001019;
    public static final int RESISTANCE_INFANTRY_2 = 400001022;

    public static final int SENGOKU_FORCE_ASSEMBLE = 400001031;
    public static final int SENGOKU_FORCE_TAKEDA = 400001035;
    public static final int SENGOKU_FORCE_AYAME = 400001034;
    public static final int SENGOKU_FORCE_HARUAKI = 400001033;
    public static final int SENGOKU_FORCE_UESUGI = 400001032;

    public static final int MIGHT_OF_THE_NOVA = 400001014;
    public static final int MIGHT_OF_THE_NOVA_BUFF = 400001015;

    public static final int CONVERSION_OVERDRIVE = 400001037;
    public static final int CONVERSION_OVERDRIVE_ATTACK = 400001038;

    private static final int[] buffs = new int[]{
            BOSS_SLAYERS,
            UNDETERRED,
            FOR_THE_GUILD,

            // Branch V skill
            幻靈武具,
            MANA_OVERLOAD,
            ETHEREAL_FORM,
            WILL_OF_ERDA,
            鋼鐵之軀,
            LUCKY_DICE,
            LAST_RESORT,
            VICIOUS_SHOT,
            OVERDRIVE,
            GUIDED_ARROW,
            CONVERSION_OVERDRIVE,
            DECENT_SHARP_EYES_V,
            DECENT_HYPER_BODY_V,
            DECENT_COMBAT_ORDERS_V,
            DECENT_ADV_BLESSING_V,
            DECENT_SPEED_INFUSION_V,
            實用的祈禱,
            RESISTANCE_INFANTRY_1,
            RESISTANCE_INFANTRY_2,
            FREUDS_WISDOM_1,
            FREUDS_WISDOM_2,
            FREUDS_WISDOM_3,
            FREUDS_WISDOM_4,
            FREUDS_WISDOM_5,
            FREUDS_WISDOM_6,
            SENGOKU_FORCE_ASSEMBLE,

            FOR_THE_GUILD,
            MAPLERUNNER_DASH
    };

    public ScheduledFuture mpPerSecConsumptionTimer;

    // Custom Job Advancement
    // Skills that are necessary to be added for certain jobs.
    public static final int[] addedSkills = new int[]{

    };

    // Quests to be completed for certain jobs.
    public static final int[] questsToComplete = new int[]{

    };

    // Skills that can be extended with a buff extender
    public static final int[] skillsToExtend = new int[]{

    };

    public Job(Char chr) {
        this.chr = chr;
        this.c = chr.getClient();

        if (c != null && chr.getId() != 0 && c.getWorld().isReboot()) {
            if (!chr.hasSkill(REBOOT)) {
                Skill skill = SkillData.getSkillDeepCopyById(REBOOT);
                skill.setCurrentLevel(1);
                chr.addSkill(skill);
            }
        }
    }

    public void handleAttack(Client c, AttackInfo attackInfo) {
        Char chr = c.getChr();
        if (chr.isSkillInfoMode()) {
            SkillStringInfo ssi = StringData.getSkillStringById(attackInfo.skillId);
            if (ssi != null) {
                chr.chatMessage(ChatType.Mob, "Name: " + ssi.getName());
                chr.chatMessage(ChatType.Mob, "Desc: " + ssi.getDesc());
                chr.chatMessage(ChatType.Mob, "h: " + ssi.getH());
            }
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = SkillData.getSkillDeepCopyById(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }

        // Recovery Rune  HP Recovery
        if (tsm.getOptByCTSAndSkill(IgnoreMobDamR, RuneStone.LIBERATE_THE_RECOVERY_RUNE) != null) {
            SkillInfo recoveryRuneInfo = SkillData.getSkillInfoById(RuneStone.LIBERATE_THE_RECOVERY_RUNE);
            byte recoveryRuneSLV = 1; //Hardcode Skill Level to 1
            int healrate = recoveryRuneInfo.getValue(dotHealHPPerSecondR, recoveryRuneSLV);
            int healing = chr.getMaxHP() / (100 / healrate);
            chr.heal(healing);
        }

        if (tsm.hasStat(GuidedArrow) && !SkillConstants.isForceAtomSkill(attackInfo.skillId) && hasHitMobs) {
            guideGuidedArrowForceAtom(attackInfo);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case ERDA_SHOWER:
                chr.reduceSkillCoolTime(skillID, 1000 * attackInfo.mobAttackInfo.size());
                break;
            case ERDA_NOVA:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
                break;
            case MIGHT_OF_THE_NOVA:
                if (tsm.hasStatBySkillId(Bishop.HEAVENS_DOOR)) {
                    return;
                }
                si = SkillData.getSkillInfoById(MIGHT_OF_THE_NOVA_BUFF);
                o1.nOption = 1;
                o1.rOption = MIGHT_OF_THE_NOVA_BUFF;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ReviveOnce, o1);
                tsm.sendSetStatPacket();
                break;
            case CONVERSION_OVERDRIVE_ATTACK:
                si = SkillData.getSkillInfoById(CONVERSION_OVERDRIVE);
                slv = (byte) chr.getSkillLevel(CONVERSION_OVERDRIVE);
                chr.addSkillCoolTime(CONVERSION_OVERDRIVE_ATTACK, si.getValue(x, slv) * 1000);
                break;

            case RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE:
                // Attack of the Rune
                AffectedArea aa = AffectedArea.getAffectedArea(chr, attackInfo);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                chr.getField().spawnAffectedArea(aa);

                skill.setCurrentLevel(1);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skill);
                }

                // Buff of the Rune
                si = SkillData.getSkillInfoById(RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE_BUFF); //Buff Info
                slv = (byte) skill.getCurrentLevel();
                o1.nReason = RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE_BUFF;
                o1.nValue = si.getValue(indieDamR, slv); //50% DamR
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);

                tsm.sendSetStatPacket();
                break;
        }
    }

    public void handleKeyDownSkill(Char chr, int skillID, InPacket inPacket) {
        if (chr != null) {
            chr.cancelKeyDownTimer();
            chr.setKeyDownTimer(EventManager.addFixedRateEvent(() -> chr.applyMpCon(skillID, chr.getSkillLevel(skillID)), 0, 1, TimeUnit.SECONDS));
        }
    }

    public void handleCancelKeyDownSkill(Char chr, int skillID) {
        if (chr != null) {
            chr.cancelKeyDownTimer();
            chr.setSkillCooldown(skillID, chr.getSkillLevel(skillID));
        }
    }

    public void handleShootObj(Char chr, int skillId, int slv) {
        chr.setSkillCooldown(skillId, slv);
    }

    public void handleSkill(Char chr, TemporaryStatManager tsm, int skillID, int slv, InPacket inPacket, SkillUseInfo skillUseInfo) {
        Option o1 = new Option();
        Option o2 = new Option();
        if (chr.isSkillInfoMode()) {
            SkillStringInfo ssi = StringData.getSkillStringById(skillID);
            if (ssi != null) {
                chr.chatMessage(ChatType.Mob, "Name: " + ssi.getName());
                chr.chatMessage(ChatType.Mob, "Desc: " + ssi.getDesc());
                chr.chatMessage(ChatType.Mob, "h: " + ssi.getH());
            }
        }
        Skill skill = SkillData.getSkillDeepCopyById(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        Summon summon;
        Field field;
        if (inPacket != null && isBuff(skillID)) {
            handleJoblessBuff(c, inPacket, skillID, slv);
        } else {
            if (chr.hasSkill(skillID) && si.getVehicleId() > 0 && skillID != Mercedes.SYLVIDIAS_FLIGHT && skillID != Evan.DRAGON_MASTER) {
                TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.RideVehicle);
                if (tsm.hasStat(RideVehicle)) {
                    tsm.removeStat(RideVehicle, false);
                }
                tsb.setNOption(si.getVehicleId());
                tsb.setROption(skillID);
                tsm.putCharacterStatValue(RideVehicle, tsb.getOption());
                tsm.sendSetStatPacket();
            } else if (SkillConstants.isHomeTeleportSkill(skillID)) {
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
            } else {
                field = c.getChr().getField();
                int noviceSkill = SkillConstants.getNoviceSkillFromRace(skillID);
                if (noviceSkill == 1085 || noviceSkill == 1087 || noviceSkill == 1090 || noviceSkill == 1179) {
                    summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                    summon.setMoveAction((byte) 4);
                    summon.setAssistType(AssistType.Heal);
                    summon.setFlyMob(true);
                    field.spawnSummon(summon);
                }
                // TOOD: make sure user owns skill
                switch (skillID) {
                    case MONOLITH://輪迴
                        summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                        field = c.getChr().getField();
                        summon.setMoveAbility(MoveAbility.Stop);
                        field.spawnSummon(summon);
                        field.setFrenzyTotem(true);
                        break;
                    case FURY_TOTEM:
                        summon = Summon.getSummonByNoCTS(c.getChr(), skillID, slv);
                        field = c.getChr().getField();
                        summon.setMoveAbility(MoveAbility.Stop);
                        field.spawnSummon(summon);
                        field.setFuryTotem(true);
                        break;
                    case WHITE_ANGELIC_BLESSING:
                    case WHITE_ANGELIC_BLESSING_2:
                    case LIGHTNING_GOD_RING:
                    case LIGHTNING_GOD_RING_2:
                    case GUARD_RING:
                    case SUN_RING:
                    case RAIN_RING:
                    case RAINBOW_RING:
                    case SNOW_RING:
                    case LIGHTNING_RING:
                    case WIND_RING:
                        summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                        summon.setMoveAction((byte) 4);
                        summon.setAssistType(AssistType.Heal);
                        summon.setFlyMob(true);
                        field.spawnSummon(summon);
                        break;
                    case ELEMENTAL_SYLPH:
                    case FLAME_SYLPH:
                    case THUNDER_SYLPH:
                    case ICE_SYLPH:
                    case EARTH_SYLPH:
                    case DARK_SYLPH:
                    case HOLY_SYLPH:
                    case SALAMANDER_SYLPH:
                    case ELECTRON_SYLPH:
                    case UNDINE_SYLPH:
                    case GNOME_SYLPH:
                    case DEVIL_SYLPH:
                    case ANGEL_SYLPH:
                    case ELEMENTAL_SYLPH_2:
                    case FLAME_SYLPH_2:
                    case THUNDER_SYLPH_2:
                    case ICE_SYLPH_2:
                    case EARTH_SYLPH_2:
                    case DARK_SYLPH_2:
                    case HOLY_SYLPH_2:
                    case SALAMANDER_SYLPH_2:
                    case ELECTRON_SYLPH_2:
                    case UNDINE_SYLPH_2:
                    case GNOME_SYLPH_2:
                    case DEVIL_SYLPH_2:
                    case ANGEL_SYLPH_2:
                        summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                        field.spawnSummon(summon);
                        break;

                    case MAPLE_TREE_OF_PEACE:
                        summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                        summon.setMoveAbility(MoveAbility.Stop);
                        field.spawnSummon(summon);
                        break;
                    case TERMS_AND_CONDITIONS_LINK:
                        o1.nReason = skillID;
                        o1.nValue = si.getValue(SkillStat.indieDamR, slv);
                        o1.tTerm = si.getValue(SkillStat.time, slv);
                        tsm.putCharacterStatValue(CharacterTemporaryStat.IndieDamR, o1);
                        break;
                    case KNIGHTS_WATCH_LINK:
                        o2.nReason = skillID;
                        o2.nValue = si.getValue(indieStance, slv);
                        o2.tTerm = si.getValue(time, slv);
                        tsm.putCharacterStatValue(IndieStance, o2);
                        break;
                }
            }
        }
    }

    public int alterCooldownSkill(int skillId) {
        Skill skill = chr.getSkill(skillId);
        if (skill == null) {
            return -1;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = skill.getCurrentLevel();
        int cdInSec = si.getValue(SkillStat.cooltime, slv);
        int cdInMillis = cdInSec > 0 ? cdInSec * 1000 : si.getValue(SkillStat.cooltimeMS, slv);
        int cooldownReductionR = chr.getHyperPsdSkillsCooltimeR().getOrDefault(skillId, 0);
        if (cooldownReductionR > 0) {
            return (int) (cdInMillis - ((double) (cdInMillis * cooldownReductionR) / 100));
        }
        return -1;
    }


    /**
     * Gets called when Character receives a debuff from a Mob Skill
     *
     * @param chr The Character
     */
    public void handleMobDebuffSkill(Char chr) {

    }

    /**
     * Handles multi job skills. e.g. Decent Skills, V Skills
     *
     * @param c
     * @param inPacket
     * @param skillID
     * @param slv
     */
    public void handleJoblessBuff(Client c, InPacket inPacket, int skillID, int slv) {
        Char chr = c.getChr();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Summon summon;
        Field field;
        boolean sendStat = true;
        switch (skillID) {
            case BOSS_SLAYERS:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieBDR, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBDR, o1);
                break;
            case UNDETERRED:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o1);
                break;
            case FOR_THE_GUILD:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                break;
            case MAPLERUNNER_DASH:
                o1.nReason = o2.nReason = skillID;
                o1.tTerm = o2.tTerm = si.getValue(time, slv);
                o1.nValue = si.getValue(indieForceJump, slv);
                tsm.putCharacterStatValue(IndieForceJump, o1);
                o2.nValue = si.getValue(indieForceSpeed, slv);
                tsm.putCharacterStatValue(IndieForceSpeed, o2);
                break;

            case DECENT_SHARP_EYES_V:
                // Short nOption is split in  2 bytes,  first one = CritDmg  second one = Crit%
                int cr = si.getValue(x, slv);
                int crDmg = si.getValue(y, slv);
                o1.nOption = (cr << 8) + crDmg;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = cr;
                o1.yOption = crDmg;
                tsm.putCharacterStatValue(SharpEyes, o1);
                break;
            case DECENT_HYPER_BODY_V:
                o2.nValue = si.getValue(x, slv);
                o2.nReason = skillID;
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o2);
                o3.nValue = si.getValue(y, slv);
                o3.nReason = skillID;
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMMPR, o3);
                break;
            case DECENT_COMBAT_ORDERS_V:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CombatOrders, o1);
                break;
            case DECENT_ADV_BLESSING_V:
                o1.nOption = si.getValue(z, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEF, o1);
                o3.nValue = si.getValue(x, slv);
                o3.nReason = skillID;
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o3);
                tsm.putCharacterStatValue(IndieMAD, o3);
                o4.nValue = si.getValue(indieMhp, slv);
                o4.nReason = skillID;
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o4);
                tsm.putCharacterStatValue(IndieMMP, o4);
                break;
            case DECENT_SPEED_INFUSION_V:
                o1.nReason = skillID;
                o1.nValue = -1;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                break;
            case 實用的祈禱:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(HolySymbol, o1);
                break;

            case LUCKY_DICE:
                if (chr.getQuestManager().getQuestById(GameConstants.LOADED_DICE_SELECTION) == null) {
                    chr.getScriptManager().createQuestWithQRValue(GameConstants.LOADED_DICE_SELECTION, "1");
                }
                int diceThrow1 = Integer.parseInt(chr.getScriptManager().getQRValue(GameConstants.LOADED_DICE_SELECTION));

                chr.write(UserPacket.effect(Effect.skillAffectedSelect(skillID, slv, diceThrow1, false)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffectedSelect(skillID, slv, diceThrow1, false)));

                if (diceThrow1 < 2) {
                    chr.reduceSkillCoolTime(skillID, (1000 * si.getValue(cooltime, slv)) / 2);
                    return;
                }

                o1.nOption = diceThrow1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);

                tsm.throwDice(diceThrow1);
                tsm.putCharacterStatValue(Dice, o1);
                break;
            case MANA_OVERLOAD:
                if (tsm.hasStat(ManaOverload)) {
                    tsm.removeStatsBySkill(MANA_OVERLOAD);
                } else {
                    o1.nOption = 10;
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(ManaOverload, o1);
                    o2.nReason = skillID;
                    o2.nValue = si.getValue(z, slv);
                    tsm.putCharacterStatValue(IndiePMdR, o2);
                }
                break;
            case GUIDED_ARROW:
                int faKey = chr.getNewForceAtomKey();
                o1.nOption = si.getValue(z, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = faKey;
                tsm.putCharacterStatValue(GuidedArrow, o1);
                tsm.sendSetStatPacket();

                ForceAtomEnum fae = ForceAtomEnum.GUIDED_ARROW;
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(faKey, fae.getInc(), 41, 3,
                        90, 840, Util.getCurrentTime(), 0, 0,
                        new Position());
                ForceAtom fa = new ForceAtom(false, 0, chr.getId(), fae,
                        true, 0, skillID, forceAtomInfo, si.getFirstRect(), 0, 300,
                        new Position(), skillID, new Position(), 0);
                fa.setMaxRecreationCount(si.getValue(z, slv));
                chr.createForceAtom(fa);
                break;
            case ETHEREAL_FORM:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = si.getValue(s, slv); // RGB
                o1.yOption = si.getValue(y, slv);
                tsm.putCharacterStatValue(EtherealForm, o1);
                break;
            case WILL_OF_ERDA:
                o1.nOption = 100;
                o1.rOption = skillID;
                o1.tOption = 3;
                tsm.putCharacterStatValue(AsrR, o1);
                tsm.removeAllDebuffs();
                break;
            case 鋼鐵之軀:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = 0;
                tsm.putCharacterStatValue(ImpenetrableSkin, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieAsrR, slv);
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o2);
                o3.nReason = skillID;
                o3.nValue = 100;
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieNoKnockBack, o3);
                break;
            case 幻靈武具://46 37 530
                o1.nOption = si.getValue(z, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(WeaponAura, o1);
                o2.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                o2.nReason = skillID;
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o2);
                o3.nValue = si.getValue(indiePMdR, slv);
                o3.nReason = skillID;
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePMdR, o3);
                break;
            case LAST_RESORT:
                int nOption = tsm.hasStat(LastResort) ? tsm.getOption(LastResort).nOption : 0;
                long remainingTime = tsm.getRemainingTime(LastResort, LAST_RESORT);
                tsm.removeStatsBySkill(LAST_RESORT);
                switch (nOption) {
                    case 0:
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        tsm.putCharacterStatValue(LastResort, o1);
                        o2.nValue = si.getValue(x, slv);
                        o2.nReason = skillID;
                        o2.tTerm = si.getValue(time, slv);
                        tsm.putCharacterStatValue(IndieNegativeEVAR, o2);
                        o3.nValue = si.getValue(z, slv);
                        o3.nReason = skillID;
                        o3.tTerm = si.getValue(time, slv);
                        tsm.putCharacterStatValue(IndieHitDamageInclHPR, o3);
                        o4.nReason = skillID;
                        o4.nValue = si.getValue(y, slv);
                        o4.tTerm = si.getValue(time, slv);
                        tsm.putCharacterStatValue(IndiePMdR, o4);
                        break;
                    case 1:
                        o1.nOption = 2;
                        o1.rOption = skillID;
                        o1.tOption = (int) ((remainingTime) / 2);
                        o1.setInMillis(true);
                        tsm.putCharacterStatValue(LastResort, o1);
                        o2.nValue = si.getValue(w, slv);
                        o2.nReason = skillID;
                        o2.tTerm = (int) ((remainingTime) / 2);
                        o2.setInMillis(true);
                        tsm.putCharacterStatValue(IndieNegativeEVAR, o2);
                        o3.nValue = si.getValue(s, slv);
                        o3.nReason = skillID;
                        o3.tTerm = (int) ((remainingTime) / 2);
                        o3.setInMillis(true);
                        tsm.putCharacterStatValue(IndieHitDamageInclHPR, o3);
                        o4.nReason = skillID;
                        o4.nValue = si.getValue(q, slv);
                        o4.tTerm = (int) ((remainingTime) / 2);
                        o4.setInMillis(true);
                        tsm.putCharacterStatValue(IndiePMdR, o4);
                        break;
                }
                break;
            case VICIOUS_SHOT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ViciousShot, o1);
                break;
            case OVERDRIVE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Overdrive, o1);
                break;

            case RESISTANCE_INFANTRY_1:
            case RESISTANCE_INFANTRY_2:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;

            //Fall through intended for all Freuds Wisdom Skill cases
            case FREUDS_WISDOM_6:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
            case FREUDS_WISDOM_5:
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieBDR, slv);
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBDR, o2);
            case FREUDS_WISDOM_4:
                o3.nReason = skillID;
                o3.nValue = si.getValue(indiePad, slv);
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o3);
                tsm.putCharacterStatValue(IndieMAD, o3);
            case FREUDS_WISDOM_3:
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieAllStat, slv);
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAllStat, o4);
            case FREUDS_WISDOM_2:
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieStance, slv);
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStance, o5);
            case FREUDS_WISDOM_1:
                for (int skillId : chr.getSkillCoolTimes().keySet()) {
                    if (SkillData.getSkillInfoById(skillId) != null && !SkillData.getSkillInfoById(skillId).isNotCooltimeReset()) {
                        chr.reduceSkillCoolTime(skillId, (long) (chr.getRemainingCoolTime(skillId) * 0.1F));
                    }
                }
                o6.nOption = tsm.hasStat(FreudBlessing) ? tsm.getOption(FreudBlessing).nOption + 1 : 1;
                o6.rOption = skillID;
                o6.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(FreudBlessing, o6);

                int cooldown = 25000; // in ms
                if (skillID == FREUDS_WISDOM_6) {
                    cooldown = 240000;
                }
                chr.addSkillCoolTime(FREUDS_WISDOM, Util.getCurrentTimeLong() + cooldown); // value isn't included in SkillId
                break;
            case SENGOKU_FORCE_ASSEMBLE:
                summonSengokuForces();
                break;
            case CONVERSION_OVERDRIVE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ConversionOverdrive, o1);
                break;
            default:
                sendStat = false;
        }
        if (sendStat) {
            tsm.sendSetStatPacket();
        }
    }

    private void guideGuidedArrowForceAtom(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Field field = chr.getField();
        int faKey = tsm.getOption(GuidedArrow).xOption;
        Rect rect = chr.getRectAround(new Rect(-350, -150, 100, 50));
        if (!chr.isLeft()) {
            rect = rect.horizontalFlipAround(chr.getPosition().getX());
        }
        Mob mob = (Mob) attackInfo.mobAttackInfo.stream().map(mai -> field.getLifeByObjectID(mai.mobId)).filter(Objects::nonNull).findFirst().orElse(null);
        if (mob == null) {
            if (field.getMobsInRect(rect).size() <= 0) {
                return;
            }
            mob = Util.getRandomFromCollection(field.getMobsInRect(rect));
        }
        chr.getField().broadcastPacket(FieldPacket.guideForceAtom(chr.getId(), faKey, mob.getObjectId()));
    }

    private void setOverdriveCooldown() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(OVERDRIVE);
        int slv = chr.getSkillLevel(OVERDRIVE);
        o1.nOption = -si.getValue(y, slv);
        o1.rOption = OVERDRIVE;
        o1.tOption = si.getValue(cooltime, slv) - si.getValue(time, slv);
        tsm.putCharacterStatValue(Overdrive, o1);
        tsm.sendSetStatPacket();
    }

    public void addMissingSkills(Char chr) {
    }

    private void summonSengokuForces() {
        if (!chr.hasSkill(SENGOKU_FORCE_ASSEMBLE) || !JobConstants.isSengoku(chr.getJob())) {
            return;
        }
        List<Integer> summonList = new ArrayList<Integer>() {{
            add(SENGOKU_FORCE_UESUGI);
            add(SENGOKU_FORCE_AYAME);
            add(SENGOKU_FORCE_HARUAKI);
            add(SENGOKU_FORCE_TAKEDA);
        }};

        Skill skill = chr.getSkill(SENGOKU_FORCE_ASSEMBLE);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        Field field = chr.getField();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.tTerm = si.getValue(time, slv);

        for (int i = 0; i < 2; i++) {
            int summonId = Util.getRandomFromCollection(summonList);
            summonList.remove((Object) summonId);
            Summon summon = Summon.getSummonBy(chr, summonId, slv);
            summon.setMoveAbility(MoveAbility.FixVMove);
            summon.setAssistType(AssistType.TeleportToMobs);
            field.spawnSummon(summon);
            o.nReason = summonId;

            switch (summonId) {
                case SENGOKU_FORCE_UESUGI: // Ied
                    o.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                    tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o);
                    break;
                case SENGOKU_FORCE_AYAME: // crDmg
                    o.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                    tsm.putCharacterStatValue(IndieCrDmg, o);
                    break;
                case SENGOKU_FORCE_HARUAKI: // dmg reduce
                    o.nValue = si.getValue(indieDamReduceR, slv);
                    tsm.putCharacterStatValue(IndieDamReduceR, o);
                    break;
                case SENGOKU_FORCE_TAKEDA: // flat att/matt
                    o.nValue = si.getValue(indiePad, slv);
                    tsm.putCharacterStatValue(IndieMAD, o);
                    tsm.putCharacterStatValue(IndiePAD, o);
                    break;
            }
        }
        tsm.sendSetStatPacket();
    }

    public void bonusConversionOverdriveAttack() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!JobConstants.isFlora(chr.getJob()) || !chr.hasSkill(CONVERSION_OVERDRIVE) || !tsm.hasStat(ConversionOverdrive) || chr.hasSkillOnCooldown(CONVERSION_OVERDRIVE_ATTACK)) {
            return;
        }

        chr.write(UserLocal.userBonusAttackRequest(CONVERSION_OVERDRIVE_ATTACK));
    }

    /**
     * Handles ForceAtom Collision, recreates the force atom automatically if the curRecreationCount is below maxRecreationCount
     *
     * @param faKey
     * @param mobObjId
     * @param position
     * @param inPacket
     */
    public void handleForceAtomCollision(int faKey, int skillId, int mobObjId, Position position, InPacket inPacket) {
        ForceAtom forceAtom = chr.getForceAtomByKey(faKey);
        if (forceAtom == null) {
            return;
        }
        if (forceAtom.getCurRecreationCount(faKey) < forceAtom.getMaxRecreationCount(faKey) && Util.succeedProp(forceAtom.getRecreationChance(faKey))) {
            chr.recreateforceAtom(faKey, forceAtom.recreate(faKey, chr, mobObjId, position));
        } else {
            chr.removeForceAtomByKey(faKey);
        }
    }

    public void handleGuidedForceAtomCollision(int faKey, int skillId, Position position) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        ForceAtom forceAtom = chr.getForceAtomByKey(faKey);
        switch (skillId) {
            case GUIDED_ARROW:
                if (forceAtom.getCurRecreationCount(faKey) < forceAtom.getMaxRecreationCount(faKey)) {
                    forceAtom.incrementCurRecreationCount(faKey);
                } else {
                    tsm.removeStatsBySkill(skillId);
                    chr.removeForceAtomByKey(faKey);
                }
                break;
        }
    }

    /**
     * Handles when specific CTSs are removed.
     *
     * @param cts The Character Temporary Stat
     */
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option removeOpt = tsm.getOption(cts);
        int skillId = cts.isIndie() ? removeOpt.nReason : removeOpt.rOption;
        if (Arrays.asList(isAuraCTS).contains(cts)) {
            AffectedArea aura = chr.getFollowAffectedAreaBySkillID(skillId);
            if (aura != null && aura.getOwner() == chr) {
                chr.getField().removeAura(aura);
            }
        }

        if (cts == FreudBlessing && tsm.getOption(FreudBlessing).nOption != 6) {
            SkillInfo si = SkillData.getSkillInfoById(FREUDS_WISDOM);
            int slv = chr.getSkillLevel(FREUDS_WISDOM);
            chr.addSkillCoolTime(FREUDS_WISDOM, Util.getCurrentTimeLong() + (si.getValue(y, slv) * 1000));
        } else if (cts == Overdrive && tsm.getOption(Overdrive).nOption > 0) {
            EventManager.addEvent(this::setOverdriveCooldown, 50, TimeUnit.MILLISECONDS);
        } else if (cts == GuidedArrow) {
            chr.removeForceAtomByKey(tsm.getOption(GuidedArrow).xOption);
        }
    }

    /**
     * Called when a player is right-clicking a buff, requesting for it to be disabled.
     *
     * @param chr     The client
     * @param skillID
     */
    public void handleSkillRemove(Char chr, int skillID) {

    }

    /**
     * Handled when a mob dies
     *
     * @param mob The Mob that has died.
     */
    public void handleMobDeath(Mob mob) {

    }

    /**
     * Handled when a MobStat is removed
     *
     * @param mobStat
     */
    public void handleRemoveMobStat(MobStat mobStat, Mob mob) {

    }

    /**
     * Handled when a mob is hit
     *
     * @param chr
     * @param mob
     * @param damage
     */
    public void handleMobDamaged(Char chr, Mob mob, long damage) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        MobTemporaryStat mts = mob.getTemporaryStat();

        // Jett - Gravity Crush
        if (mts.hasCurrentMobStatBySkillId(Jett.GRAVITY_CRUSH)) {
            Option o = mts.getOptByMobStatAndSkill(MobStat.Stun, Jett.GRAVITY_CRUSH);
            if (o.chr != null && o.summon != null
                    && chr.getField() == o.chr.getField()
                    && chr.isInAPartyWith(o.chr)
                    && chr.getField().getSummonByChrAndSkillId(o.chr, Jett.GRAVITY_CRUSH) != null) {
                ((Jett) o.chr.getJobHandler()).incGravityCrushDmg(mob, damage);
            }
        }
    }

    /**
     * Handles the 'initial' part of a hit, the initial packet processing.
     *
     * @param inPacket The packet to be processed
     */
    public void handleHit(InPacket inPacket) {
        if (chr.isInvincible()) {
            return;
        }

        HitInfo hitInfo = new HitInfo();
        // cryto.username
        inPacket.decodeInt(); // Unknown
        hitInfo.damagedTime = inPacket.decodeInt();
        //hitInfo.attackIdx = inPacket.decodeByte(); // -1 attack idx = body (touch) attack
        hitInfo.type = inPacket.decodeByte();
        inPacket.decodeByte();
        hitInfo.elemAttr = inPacket.decodeByte();
        hitInfo.hpDamage = inPacket.decodeInt();
        inPacket.decodeByte(); // Hardcoded 0 crit
        inPacket.decodeByte(); // Unknown
        boolean knockBack;
        if (hitInfo.type <= AttackIndex.Counter.getVal()) {
            hitInfo.obstacle = inPacket.decodeShort();
        } else if (hitInfo.type > AttackIndex.Counter.getVal()) {
            //hitInfo.templateID = inPacket.decodeInt();
            inPacket.decodeArr(36);
            hitInfo.mobID = inPacket.decodeInt();
            inPacket.decodeByte();
            inPacket.decodeInt();
            hitInfo.mobIdForMissCheck = inPacket.decodeInt();
            hitInfo.isLeft = inPacket.decodeByte() != 0;
            hitInfo.SkillId = inPacket.decodeInt();
            hitInfo.reducedDamage = inPacket.decodeInt(); // pdmg
            hitInfo.reflect = inPacket.decodeByte();// deftype
            hitInfo.guard = inPacket.decodeByte();
            if (hitInfo.guard == 2) {
                knockBack = true;
            }
            if (hitInfo.guard == 2 || hitInfo.SkillId != 0) {
                hitInfo.isGuard = inPacket.decodeByte() != 0; //pPhysical
                hitInfo.reflectMobID = inPacket.decodeInt(); //pID
                hitInfo.hitAction = inPacket.decodeByte(); //pType
                hitInfo.hitPos = inPacket.decodePosition();
                hitInfo.userHitPos = inPacket.decodePosition();
                if (hitInfo.isGuard) {
                    hitInfo.reflectDamage = inPacket.decodeInt();
                }
            }
            if (inPacket.getUnreadAmount() == 1L) {
                hitInfo.stance = inPacket.decodeByte();
                if ((hitInfo.stance == 1) && (inPacket.getUnreadAmount() >= 4L)) {
                    hitInfo.stanceSkillID = inPacket.decodeInt();
                }
                if ((hitInfo.stance < 0) || (hitInfo.stance > 2)) {
                    hitInfo.stance = 0;
                }
            }
        }

        handleHit(c, inPacket, hitInfo);
        handleHit(c, hitInfo);
    }

    /**
     * Handles the 'middle' part of hit processing, namely the job-specific stuff like Magic Guard,
     * and puts this info in <code>hitInfo</code>.
     *
     * @param c        The client
     * @param inPacket packet to be processed
     * @param hitInfo  The hit info that should be altered if necessary
     */
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Field field = chr.getField();

        // Mages - Ethereal Form
        if (chr.hasSkill(ETHEREAL_FORM) && tsm.hasStat(EtherealForm)) {
            int mpDmg = tsm.getOption(EtherealForm).nOption;
            int hpDmg = tsm.getOption(EtherealForm).yOption;
            int remainingMP = chr.getMP() - mpDmg;
            if (chr.getMP() > 0) {
                hitInfo.mpDamage = remainingMP < 0 ? chr.getMP() : mpDmg;
                hitInfo.hpDamage = remainingMP < 0 ? (hpDmg - chr.getMP() > 0 ? hpDmg - chr.getMP() : 0) : 0;
            } else {
                hitInfo.hpDamage = hpDmg;
            }
        }

        // Wind Archer - Gale Barrier
        if (chr.hasSkill(WindArcher.GALE_BARRIER) && tsm.hasStat(GaleBarrier) && hitInfo.hpDamage > 0) {
            double hpDmgR = (((double) hitInfo.hpDamage) / chr.getMaxHP()) * 100;
            ((WindArcher) chr.getJobHandler()).diminishGaleBarrier((int) hpDmgR);
            hitInfo.hpDamage = 0;
        }

        // General - Damage Reduce
        if (hitInfo.hpDamage > 0) {
            long totalDmgReduceR = chr.getTotalStat(BaseStat.dmgReduce);

            if (totalDmgReduceR > 0) {
                hitInfo.hpDamage -= (int) ((hitInfo.hpDamage * totalDmgReduceR / 100D) > hitInfo.hpDamage ? hitInfo.hpDamage : (hitInfo.hpDamage * totalDmgReduceR / 100D));
            }
        }

        // Mihile - Shield of Light
        if (tsm.hasStat(RhoAias)) {
            // Hack Checks for Party members being in the shield rect.
            Char mihileChr = chr.getField().getCharByID(tsm.getOption(RhoAias).xOption);
            if (mihileChr != null) {
                ((Mihile) mihileChr.getJobHandler()).hitShieldOfLight();
            }
        }

        // Warrior V - Impenetrable Skin
        if (tsm.hasStat(ImpenetrableSkin) && chr.hasSkill(鋼鐵之軀)) {
            Skill skill = chr.getSkill(鋼鐵之軀);
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int slv = skill.getCurrentLevel();
            int count = tsm.getOption(ImpenetrableSkin).xOption;
            if (count < si.getValue(y, slv)) {
                count++;

                o1.nOption = 100;
                o1.rOption = skill.getSkillId();
                o1.tOption = (int) (tsm.getRemainingTime(ImpenetrableSkin, 鋼鐵之軀));
                o1.xOption = count;
                o1.setInMillis(true);
                tsm.putCharacterStatValue(ImpenetrableSkin, o1, true);
                o2.nValue = si.getValue(x, slv) * count;
                o2.nReason = skill.getSkillId();
                o2.tTerm = (int) (tsm.getRemainingTime(ImpenetrableSkin, 鋼鐵之軀));
                o2.setInMillis(true);
                tsm.putCharacterStatValue(IndieDamR, o2, true);
                tsm.sendSetStatPacket();
            }
        }

        // Bishop - Holy Magic Shell
        if (tsm.hasStat(HolyMagicShell)) {
            if (tsm.getOption(HolyMagicShell).xOption > 0) {
                Option o = new Option();
                o.nOption = tsm.getOption(HolyMagicShell).nOption;
                o.rOption = tsm.getOption(HolyMagicShell).rOption;
                o.tOption = (int) tsm.getRemainingTime(HolyMagicShell, o.rOption);
                o.xOption = tsm.getOption(HolyMagicShell).xOption - 1;
                o.setInMillis(true);
                tsm.putCharacterStatValue(HolyMagicShell, o);
                tsm.sendSetStatPacket();
            } else {
                tsm.removeStatsBySkill(Bishop.HOLY_MAGIC_SHELL);
            }
        }

        // Mihile - Soul Link
        if (tsm.hasStat(CharacterTemporaryStat.MichaelSoulLink) && chr.getId() != tsm.getOption(CharacterTemporaryStat.MichaelSoulLink).cOption) {
            Party party = chr.getParty();

            PartyMember mihileInParty = party.getPartyMemberByID(tsm.getOption(CharacterTemporaryStat.MichaelSoulLink).cOption);
            if (mihileInParty != null) {
                Char mihileChr = mihileInParty.getChr();
                Skill skill = mihileChr.getSkill(Mihile.SOUL_LINK);
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int slv = skill.getCurrentLevel();

                int hpDmg = hitInfo.hpDamage;
                int mihileDmgTaken = (int) (hpDmg * ((double) si.getValue(SkillStat.q, slv) / 100));

                hitInfo.hpDamage = hitInfo.hpDamage - mihileDmgTaken;
                mihileChr.damage(mihileDmgTaken);
            } else {
                tsm.removeStatsBySkill(Mihile.SOUL_LINK);
                tsm.removeStatsBySkill(Mihile.ROYAL_GUARD);
                tsm.removeStatsBySkill(Mihile.ENDURING_SPIRIT);
                tsm.sendResetStatPacket();
            }
        }

        // Paladin - Parashock Guard
        if (tsm.hasStat(KnightsAura) && chr.getId() != tsm.getOption(KnightsAura).nOption) {
            Party party = chr.getParty();

            PartyMember paladinInParty = party.getPartyMemberByID(tsm.getOption(KnightsAura).nOption);
            if (paladinInParty != null) {
                Char paladinChr = paladinInParty.getChr();
                Skill skill = paladinChr.getSkill(PARASHOCK_GUARD);
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int slv = skill.getCurrentLevel();

                int dmgReductionR = si.getValue(y, slv);
                int dmgReduceAmount = (int) (hitInfo.hpDamage * ((double) dmgReductionR / 100));
                hitInfo.hpDamage = hitInfo.hpDamage - dmgReduceAmount;
            }
        }

        // Magic Guard
        if (chr.getTotalStat(BaseStat.magicGuard) > 0) {
            int dmgPerc = chr.getTotalStat(BaseStat.magicGuard);
            int dmg = hitInfo.hpDamage;
            int mpDmg = (int) (dmg * (dmgPerc / 100D));
            mpDmg = chr.getStat(Stat.mp) - mpDmg < 0 ? chr.getStat(Stat.mp) : mpDmg;
            hitInfo.hpDamage = dmg - mpDmg;
            hitInfo.mpDamage = mpDmg;
        }

        // Miss or Evade
        if (hitInfo.hpDamage <= 0) {
            // Hypogram Field  Support or Fusion
            if (chr.getParty() != null) {
                for (AffectedArea aa : field.getAffectedAreas().stream().filter(aa -> aa.getSkillID() == Xenon.HYPOGRAM_FIELD_SUPPORT || aa.getSkillID() == Xenon.HYPOGRAM_FIELD_FUSION).collect(Collectors.toList())) {
                    boolean isInsideAA = aa.getRect().hasPositionInside(chr.getPosition());
                    if (!isInsideAA) {
                        continue;
                    }
                    Option supportOption = tsm.getOptByCTSAndSkill(IndieMHPR, Xenon.HYPOGRAM_FIELD_SUPPORT);
                    if (supportOption != null) {
                        Char xenonChr = chr.getParty().getPartyMemberByID(supportOption.wOption).getChr();
                        if (xenonChr != null && xenonChr.getField() == chr.getField() && xenonChr != chr) {
                            ((Xenon) xenonChr.getJobHandler()).incrementSupply(1);
                        }
                    }
                    Option fusionOption = tsm.getOptByCTSAndSkill(IndieDamR, Xenon.HYPOGRAM_FIELD_FUSION);
                    if (fusionOption != null) {
                        Char xenonChr = chr.getParty().getPartyMemberByID(fusionOption.wOption).getChr();
                        if (xenonChr != null && xenonChr.getField() == chr.getField() && xenonChr != chr) {
                            ((Xenon) xenonChr.getJobHandler()).incrementSupply(1);
                        }
                    }
                }
            }
        }
    }

    /**
     * The 'final' part of the hit process. Assumes the correct info (wrt buffs for example) is
     * already in <code>hitInfo</code>.
     *
     * @param c       The client
     * @param hitInfo The completed hitInfo
     */
    public void handleHit(Client c, HitInfo hitInfo) {
        Char chr = c.getChr();
        hitInfo.hpDamage = Math.max(0, hitInfo.hpDamage); // to prevent -1 (dodges) healing the player.
//        if (chr.getStat(Stat.mhp) > hitInfo.hpDamage)
//            hitInfo.hpDamage = (int) (hitInfo.hpDamage - ((hitInfo.hpDamage / 200) * chr.getAvatarData().getCharacterStat().getPierce()));
        if (chr.getStat(Stat.hp) <= hitInfo.hpDamage) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();

            // Global Revives ---------------------------------------

            // Global - Door (Bishop)
            if (tsm.hasStatBySkillId(Bishop.HEAVENS_DOOR)) {
                Bishop.reviveByHeavensDoor(chr);
            } else if (tsm.hasStatBySkillId(MIGHT_OF_THE_NOVA_BUFF)) {
                Bishop.reviveByHeavensDoor(chr);
            }

            // Global - Shade Link Skill (Shade)
            // TODO


            // Class Revives ----------------------------------------

            // Dark Knight - Final Pact
            else if (JobConstants.isDarkKnight(chr.getJob()) && chr.hasSkill(FINAL_PACT_INFO)) {
                ((DarkKnight) chr.getJobHandler()).reviveByFinalPact();
            }

            // Night Walker - Darkness Ascending
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, NightWalker.DARKNESS_ASCENDING) != null) {
                ((NightWalker) chr.getJobHandler()).reviveByDarknessAscending();
            }

            // Blaze Wizard - Phoenix Run
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, BlazeWizard.PHOENIX_RUN) != null) {
                ((BlazeWizard) chr.getJobHandler()).reviveByPhoenixRun();
            }

            // Shade - Summon Other Spirit
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, Shade.SUMMON_OTHER_SPIRIT) != null) {
                ((Shade) chr.getJobHandler()).reviveBySummonOtherSpirit();
            }

            // Beast Tamer - Bear Reborn		TODO
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, BeastTamer.BEAR_REBORN) != null) {
                ((BeastTamer) chr.getJobHandler()).reviveByBearReborn();
            }

            // Zero - Rewind
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, Zero.REWIND) != null) {
                ((Zero) chr.getJobHandler()).reviveByRewind();
            }

            // Phantom - Final Feint
            else if (tsm.getOptByCTSAndSkill(ReviveOnce, Phantom.FINAL_FEINT) != null) {
                ((Phantom) chr.getJobHandler()).reviveByFinalFeint();
            }


        }

        int curHP = chr.getStat(Stat.hp);
        int newHP = curHP - hitInfo.hpDamage;
        if (newHP <= 0) {
            curHP = 0;
        } else {
            curHP = newHP;
        }
        chr.setStatAndSendPacket(Stat.hp, curHP);
//        Map<Stat, Object> stats = new HashMap<>();
//        stats.put(Stat.hp, curHP);

        int curMP = chr.getStat(Stat.mp);
        int newMP = curMP - hitInfo.mpDamage;
        if (newMP < 0) {
            // should not happen
            curMP = 0;
        } else {
            curMP = newMP;
        }
        chr.setStatAndSendPacket(Stat.mp, curMP);
//        stats.put(Stat.mp, curMP);

//        c.write(WvsContext.statChanged(stats));
        chr.getField().broadcastPacket(UserRemote.hit(chr, hitInfo), chr);
        if (chr.getParty() != null) {
            chr.getParty().broadcast(UserRemote.receiveHP(chr), chr);
        }
        if (curHP <= 0) {
            // TODO Add more items for protecting exp and whatnot
            /*
            c.write(UserLocal.openUIOnDead(true, chr.getBuffProtectorItem() != null,
                    false, false, false,
                    ReviveType.NORMAL, 0));

             */
            c.write(UserLocal.openUIOnDead(chr, 1));
        } else if (hitInfo.mobID != 0 && (hitInfo.hpDamage > 0 || hitInfo.mpDamage > 0)) {
            Life life = chr.getField().getLifeByObjectID(hitInfo.mobID);
            if (life instanceof Mob) {
                ((Mob) life).applyHitDiseaseToPlayer(chr, hitInfo.type);
            }
        }
    }

    public abstract boolean isHandlerOfJob(short id);

    public SkillInfo getInfo(int skillID) {
        return SkillData.getSkillInfoById(skillID);
    }

    protected Char getChar() {
        return chr;
    }

    public abstract int getFinalAttackSkill();

    /**
     * The 'start' of the handler when leveling up.
     */
    public void handleLevelUp() {
        short level = chr.getLevel();
        short jobId = chr.getJob();

        Map<Stat, Object> stats = new HashMap<>();
        if (level > 10) {
            chr.addStat(Stat.ap, 5);
            stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        } else if (level < 10) {
            if (level >= 6) {
                chr.addStat(Stat.str, 4);
                chr.addStat(Stat.dex, 1);
            } else {
                chr.addStat(Stat.str, 5);
            }
            stats.put(Stat.str, (short) chr.getStat(Stat.str));
            stats.put(Stat.dex, (short) chr.getStat(Stat.dex));
        }
        if (level >= 50) {
            chr.addHonorExp(700 + ((chr.getLevel() - 50) / 10) * 100);
        }
        int sp = SkillConstants.getBaseSpByLevel(level);
        // Double SP gain on levels ending with 3/6/9 after level 100
        if ((level % 10) % 3 == 0 && level > 100) {
            sp *= 2;
        }
        chr.addSpToJobByCurrentLevel(sp);
        if (JobConstants.isExtendSpJob(chr.getJob())) {
            stats.put(Stat.sp, chr.getAvatarData().getCharacterStat().getExtendSP());
        } else {
            stats.put(Stat.sp, (short) chr.getAvatarData().getCharacterStat().getSp());
        }
        int linkSkill = SkillConstants.getLinkSkillByJob(chr.getJob());
        byte linkSkillLevel = (byte) SkillConstants.getLinkSkillLevelByCharLevel(level);
        int linkSkillID = SkillConstants.getOriginalOfLinkedSkill(linkSkill);
        if (linkSkillID != 0 && linkSkillLevel > 0) {
            Skill skill = chr.getSkill(linkSkillID, true);
            if (skill.getCurrentLevel() != linkSkillLevel) {
                chr.addSkill(linkSkillID, linkSkillLevel, 3);
            }
            Account acc = chr.getAccount();
            LinkSkill ls = acc.getLinkSkillByLinkSkillId(linkSkill);
            if (ls == null) {
                ls = new LinkSkill(chr.getId(), 0, linkSkill, linkSkillLevel);
                acc.getLinkSkills().add(ls);
            } else if (ls.getLevel() < linkSkillLevel) {
                ls.setLinkSkillID(linkSkill);
                ls.setLevel(linkSkillLevel);
                ls.setOriginID(chr.getId());
            }
        }

        // Add server-sided increments for hp/mp then modify based on job passives
        int[][] incVal = GameConstants.getIncValArray(chr.getJob());
        if (incVal != null) {
            chr.addStat(Stat.mhp, incVal[0][1]);
            stats.put(Stat.mhp, chr.getStat(Stat.mhp));
            if (!JobConstants.isNoManaJob(chr.getJob())) {
                chr.addStat(Stat.mmp, incVal[3][0]);
                stats.put(Stat.mmp, chr.getStat(Stat.mmp));
            }
            chr.recalcStats(EnumSet.of(BaseStat.mhp, BaseStat.mmp));
        } else {
            chr.chatMessage("Unhandled HP/MP job " + chr.getJob());
        }

        if (c.getWorld().isReboot()) {
            Skill skill = SkillData.getSkillDeepCopyById(REBOOT2);
            skill.setCurrentLevel(level);
            chr.addSkill(skill);
        }

        boolean jobAdvance = false;
        String message = "";
        switch (level) {
            case 10:
                message += "#b[Level 10] 1st Job Advancement#k\r\n\r\n";
                message += "You've reached level 10, and are ready for your #b[1st Job Advancement]#k!\r\n\r\n";
                message += "Complete the #r[Job Advancement]#k quest and unlock your 1st job advancement!\r\n";
                jobAdvance = true;
                break;
            case 20:
                if (chr.getJob() == JobConstants.JobEnum.THIEF.getJobId() && chr.getSubJob() == 1) { // dual blade
                    message += "#b[Level 20] 1.5th Job Advancement#k\r\n\r\n";
                    message += "Congratulations on reaching level 20!#k\r\n\r\n";
                    message += "You've unlocked your next job advancement!\r\n\r\n";
                    jobAdvance = true;
                }
                break;
            case 30: {
                message += "#b[Level 30] 2nd Job Advancement#k\r\n\r\n";
                message += "Congratulations on reaching level 30!#k\r\n\r\n";
                message += "You've unlocked your next job advancement!\r\n\r\n";
                message += "I've given you an equipment box to help further your progress!\r\n\r\n";
                chr.addCharacterPotentials();
                chr.chatMessage("Character potential unlocked.");
                chr.getQuestManager().completeQuest(12396);
                chr.addHonorExp(1000);
                // check for explorer, else you will advance to something random
                if (!JobConstants.isExplorer(jobId) && !JobConstants.isDualBlade(jobId) && !JobConstants.isBeastTamer(jobId) && !JobConstants.isEvan(jobId)) {
                    short nextJobId = (short) (jobId + 10);
                    chr.addSpToJobByCurrentJob(5);
                    handleJobAdvance(nextJobId);
                } else if (JobConstants.isPathFinder(jobId)) {
                    short nextJobId = (JobConstants.JobEnum.PATHFINDER_2.getJobId());
                    chr.addSpToJobByCurrentJob(5);
                    handleJobAdvance(nextJobId);
                }
                if (JobConstants.isKaiser(jobId) || (JobConstants.isKanna(jobId))) {
                    chr.addSpToJobByCurrentJob(5);
                }
                if (JobConstants.isJett(jobId)) {
                    short nextJobId = (JobConstants.JobEnum.JETT_2.getJobId());
                    handleJobAdvance(nextJobId);
                    chr.addSkill(228, 1, 1);
                    chr.addSkill(1227, 1, 1);
                }
                if (JobConstants.isCannonShooter(jobId)) { // Cannonneer
                    short nextJobId = (JobConstants.JobEnum.CANNONEER.getJobId());
                    handleJobAdvance(nextJobId);
                }
                if (JobConstants.isDemonAvenger(jobId)) {
                    short nextJobId = (JobConstants.JobEnum.DEMON_AVENGER_2.getJobId());
                    chr.addSkill(31011000, 20, 20);
                    chr.addSkill(31011001, 20, 20);
                    chr.addSkill(31010002, 10, 10);
                    chr.addSkill(31010003, 15, 15);
                    handleJobAdvance(nextJobId);
                    chr.addSpToJobByCurrentJob(5);
                }
                chr.addCharacterPotentials();
                chr.chatMessage("Character potential unlocked.");
                chr.getQuestManager().completeQuest(12396);
                chr.addHonorExp(1000);
                jobAdvance = true;
                break;
            }
            case 60: {
                message += "#b[Level 60] 3rd Job Advancement#k\r\n\r\n";
                message += "Congratulations on reaching level 60!#k\r\n\r\n";
                message += "You've unlocked your next job advancement!\r\n\r\n";
                if (!JobConstants.isDualBlade(jobId) && !JobConstants.isBeastTamer(jobId) && !JobConstants.isEvan(jobId)) {
                    short nextJobId = (short) (jobId + 1);
                    handleJobAdvance(nextJobId);
                }
                if (JobConstants.isJett(jobId)) {
                    short nextJobId = (JobConstants.JobEnum.JETT_3.getJobId());
                    handleJobAdvance(nextJobId);
                }
                chr.addSpToJobByCurrentJob(10);
                jobAdvance = true;
                break;
            }
            case 100: {
                message += "#b[Level 100] 4th Job Advancement#k\r\n\r\n";
                message += "Congratulations on reaching level 100!#k\r\n\r\n";
                message += "You've unlocked your next job advancement!\r\n\r\n";
                message += "I've given you an equipment box to help further your progress!\r\n\r\n";
                if (!JobConstants.isDualBlade(jobId) && !JobConstants.isBeastTamer(jobId) && !JobConstants.isEvan(jobId)) {
                    short nextJobId = (short) (jobId + 1);
                    handleJobAdvance(nextJobId);
                }
                if (JobConstants.isJett(jobId)) {
                    short nextJobId = (JobConstants.JobEnum.JETT_4.getJobId());
                    handleJobAdvance(nextJobId);
                }
                chr.addSpToJobByCurrentJob(3);
                jobAdvance = true;
                break;
            }
            case 200: {
                message += "#b[Level 200] V Matrix Unlocked#k\r\n\r\n";
                message += "Congratulations on reaching level 200!#k\r\n\r\n";
                message += "You've unlocked the V Matrix!\r\n\r\n";
                message += "I've given you some Nodestones to help you on your adventure!\r\n\r\n";
                chr.getQuestManager().completeQuest(QuestConstants.FIFTH_JOB_QUEST);
                jobAdvance = true;
                break;
            }
                /* OLD EXAMPLE MESSAGE
                String message = "#b[Guide] 2nd Job Advancement#k\r\n\r\n";
                message += "You've reached level 30, and are ready for your #b[2nd Job Advancement]#k!\r\n\r\n";
                message += "Complete the #r[Job Advancement]#k quest to unlock your 2nd job advancement!\r\n";
                chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));

                message = "#b[Guide] Ability#k\r\n\r\n";
                message += "You've reached level 30 and can now unlock #b[Abilities]#k!\r\n\r\n";
                message += "Accept the quest #bFirst Ability - The Eye Opener#k from the Quest Notifier!\r\n";
                chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
                */
        }

        if (jobAdvance && (JobConstants.isExplorer(chr.getJob())) || JobConstants.isCygnusKnight(chr.getJob())) {
            chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
        }

        handleLevelUpEnd(stats);
    }

    /**
     * The 'end' of the handler when leveling up.
     * Generally where the data gets sent.
     */
    public void handleLevelUpEnd(Map<Stat, Object> stats) {
        chr.write(WvsContext.statChanged(stats));

//        for (Map.Entry<Stat, Object> stat : stats.entrySet()) {
//            Object value = stat.getValue();
//            if (stat.getValue().getClass() == ExtendSP.class) {
//                ExtendSP sp = (ExtendSP) stat.getValue();
//                value = sp.getTotalSp();
//            }
//
//            chr.chatMessage("key: %s, value: %s",  stat.getKey().toString(), value.toString());
//        }

        chr.heal(chr.getMaxHP());
        chr.healMP(chr.getMaxMP());
    }

    public boolean isBuff(int skillID) {
        return Arrays.stream(buffs).anyMatch(b -> b == skillID);
    }

    public void cancelTimers() {
        if (mpPerSecConsumptionTimer != null && !mpPerSecConsumptionTimer.isDone()) {
            mpPerSecConsumptionTimer.cancel(true);
        }
    }

    public void giveHyperAfBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(HYPER_STAT_ARCANE_FORCE)) {
            Option o = new Option();
            int slv = chr.getSkillLevel(HYPER_STAT_ARCANE_FORCE);
            o.nValue = slv <= 10 ? slv * 5 : 50 + (slv - 10) * 10; // in wz, under level
            tsm.putCharacterStatValue(CharacterTemporaryStat.IndieArcaneForce, o);
            tsm.sendSetStatPacket();
        }
    }

    /**
     * Set character info on a newly created character before logging into the game.
     *
     * @param chr The character to modify.
     */
    public void setCharCreationStats(Char chr) {
        CharacterStat characterStat = chr.getAvatarData().getCharacterStat();

        // standardize all classes to level 10 with 70 total ap
        // hp and mp will depend on main stat
        characterStat.setLevel(10);
        characterStat.setAp(54);
        characterStat.setStr(4);
        characterStat.setDex(4);
        characterStat.setInt(4);
        characterStat.setLuk(4);
        characterStat.setMaxHp(100);
        characterStat.setHp(100);
        characterStat.setMaxMp(100);
        characterStat.setMp(100);

        // player hub map
        characterStat.setPosMap(GameConstants.PLAYER_START_MAP);

    }

    /**
     * Advances the job to the specified job.
     * Also increases AP by 5.
     *
     * @param jobId The job to advance to.
     */
    public void handleJobAdvance(short jobId) {
        int apToAdd = 5;

        chr.setJob(jobId);
        chr.setStatAndSendPacket(Stat.job, chr.getJob());
        chr.addStatAndSendPacket(Stat.ap, apToAdd);

        chr.chatMessage("[自動轉職] 轉職成功為 %s!", JobConstants.getJobEnumById(jobId).getJobName());
        chr.chatMessage("[自動轉職] 你得到 (%d) 能力點.", apToAdd);

        if (CustomConstants.MAX_SKILLS) {
            setMaxSkillsToJob(jobId);
            chr.chatMessage("[自動轉職] 技能自動加滿.");
        }
    }

    /**
     * Maximize all skills from the given jobId.
     *
     * @param jobId The given job to maximize skills.
     */
    public void setMaxSkillsToJob(short jobId) {
        List<Skill> listOfSkills = new ArrayList<>();
        for (Skill skill : SkillData.getSkillsByJob(jobId)) {
            byte maxLevel = (byte) skill.getMaxLevel();
            skill.setCurrentLevel(maxLevel);
            skill.setMasterLevel(maxLevel);
            listOfSkills.add(skill);
            chr.addSkill(skill);
            chr.write(WvsContext.changeSkillRecordResult(listOfSkills, true, false, false, false));
        }
    }

    /**
     * Starts the creation of a new job.
     * Start script for multi path jobs.
     * Job Advance for single path jobs.
     */
    public void handleJobStart() {
    }

    /**
     * Ends the creation of a new job. Items, stats and skills are generally given here.
     */
    public void handleJobEnd() {
        BaseStat mainStat = GameConstants.getMainStatForJob(chr.getJob());
        if (mainStat != null) {
            switch (mainStat) {
                case str:
                    chr.setStatAndSendPacket(Stat.mhp, 500);
                    chr.setStatAndSendPacket(Stat.mmp, 200);
                    break;
                case inte:
                    chr.setStatAndSendPacket(Stat.mhp, 200);
                    chr.setStatAndSendPacket(Stat.mmp, 500);
                    break;
                case dex:
                case luk:
                    chr.setStatAndSendPacket(Stat.mhp, 350);
                    chr.setStatAndSendPacket(Stat.mmp, 350);
                    break;
            }
            chr.heal(chr.getMaxHP());
            chr.healMP(chr.getMaxMP());
        }
    }
}
