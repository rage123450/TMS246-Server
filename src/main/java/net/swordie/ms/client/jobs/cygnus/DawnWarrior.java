package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.info.SkillUseInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Summoned;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DawnWarrior extends Noblesse {

    public static final int ELEMENTAL_HARMONY_STR = 10000246;

    public static final int SOUL_ELEMENT = 11001022; //Buff  (Immobility Debuff)

    public static final int SOLAR_PIERCE = 11121203;

    public static final int SOUL_SPEED = 11101024; //Buff
    public static final int FALLING_MOON = 11101022; //Buff (Unlimited Duration)

    public static final int RISING_SUN = 11111022; //Buff (Unlimited Duration)
    public static final int TRUE_SIGHT = 11111023; //Buff (Mob Def Debuff & Final DmgUp Debuff)
    public static final int TRUE_SIGHT_PERSIST = 11120043;
    public static final int TRUE_SIGHT_GUARDBREAK = 11120045;
    public static final int TRUE_SIGHT_ENHANCE = 11120044;
    public static final int WILL_OF_STEEL = 11110025;

    public static final int EQUINOX_SLASH = 11121014;
    public static final int EQUINOX_CYCLE = 11121005; //Buff
    public static final int EQUINOX_CYCLE_MOON = 11121011;
    public static final int EQUINOX_CYCLE_SUN = 11121012;
    public static final int IMPALING_RAYS = 11121004; //Special Attack (Incapacitate Debuff)
    public static final int IMPALING_RAYS_EXPLOSION = 11121013;
    public static final int CALL_OF_CYGNUS_DW = 11121000; //Buff
    public static final int MASTER_OF_THE_SWORD = 11120009;

    public static final int SOUL_FORGE = 11121054;
    public static final int GLORY_OF_THE_GUARDIANS_DW = 11121053;

    // V Skills
    public static final int CELESTIAL_DANCE = 400011005;
    public static final int CELESTIAL_DANCE_MOON = 400011022;
    public static final int CELESTIAL_DANCE_SUN = 400011023;
    public static final int RIFT_OF_DAMNATION = 400011055;
    public static final int RIFT_OF_DAMNATION_ATTACK = 400011056;
    public static final int RIFT_OF_DAMNATION_SUMMON = 400011065;
    public static final int SOUL_ECLIPSE = 400011088;
    public static final int SOUL_ECLIPSE_BREAK = 400011089;


    private static final int[] addedSkills = new int[]{
            ELEMENTAL_HARMONY_STR,
            SOLAR_PIERCE,
    };

    private ScheduledFuture willOfSteelTimer;

    public DawnWarrior(Char chr) {
        super(chr);
        if (chr != null && chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
            if (willOfSteelTimer != null && !willOfSteelTimer.isDone()) {
                willOfSteelTimer.cancel(true);
            }
            willOfSteel();
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDawnWarrior(id);
    }


    private void willOfSteel() {
        int interval = 4;
        if (chr.hasSkill(WILL_OF_STEEL) && chr != null && chr.getField() != null) {
            Skill skill = chr.getSkill(WILL_OF_STEEL);
            int slv = skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            interval = si.getValue(w, slv);
            int heal = (int) ((double) (chr.getMaxHP() * si.getValue(y, slv)) / 100F);

            chr.heal(heal, true);
        }
        willOfSteelTimer = EventManager.addEvent(this::willOfSteel, interval, TimeUnit.SECONDS);
    }

    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Client c, AttackInfo attackInfo) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }
        if (tsm.hasStat(SoulMasterFinal)) {
            applySoulElementOnMob(attackInfo, slv);
        }
        if (tsm.hasStatBySkillId(CELESTIAL_DANCE)
                && !chr.hasSkillOnCooldown(CELESTIAL_DANCE_SUN)
                && !chr.hasSkillOnCooldown(CELESTIAL_DANCE_MOON)
                && attackInfo.skillId != CELESTIAL_DANCE_SUN
                && attackInfo.skillId != CELESTIAL_DANCE_MOON
                && attackInfo.skillId != SOUL_ECLIPSE
                && attackInfo.skillId != SOUL_ECLIPSE_BREAK) {
            if (tsm.getOption(PoseType).nOption == 1) { // Moon Attack
                chr.write(UserLocal.userBonusAttackRequest(CELESTIAL_DANCE_MOON));
            } else if (tsm.getOption(PoseType).nOption == 2) { // Sun Attack
                chr.write(UserLocal.userBonusAttackRequest(CELESTIAL_DANCE_SUN));
            }
            chr.addSkillCoolTime(CELESTIAL_DANCE_MOON, 5000);
            chr.addSkillCoolTime(CELESTIAL_DANCE_SUN, 5000);
        }
        if (attackInfo.bySummonedID == 0
                && chr.hasSkill(RISING_SUN)
                && chr.hasSkill(FALLING_MOON)
                && attackInfo.skillId != CELESTIAL_DANCE_MOON
                && attackInfo.skillId != CELESTIAL_DANCE_SUN
                && attackInfo.skillId != SOUL_ECLIPSE
                && attackInfo.skillId != SOUL_ECLIPSE_BREAK) {
            equinox(tsm);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case IMPALING_RAYS:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                o2.nOption = 1;
                o2.rOption = skill.getSkillId();
                o2.wOption = chr.getId();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                        mts.addStatOptionsAndBroadcast(MobStat.SoulExplosion, o2);
                    }
                }
                break;
            case RIFT_OF_DAMNATION_ATTACK:
            case RIFT_OF_DAMNATION_SUMMON:
                summonAndIncRift(attackInfo);
                break;
            case SOUL_ECLIPSE_BREAK:
                if (chr.getField().getSummonByChrAndSkillId(chr, SOUL_ECLIPSE) != null) {
                    chr.getField().removeLife(chr.getField().getSummonByChrAndSkillId(chr, SOUL_ECLIPSE));
                }
                break;
        }

        super.handleAttack(c, attackInfo);
    }

    private void summonAndIncRift(AttackInfo attackInfo) {
        Field field = chr.getField();
        Rect rect = chr.getRectAround(new Rect(-300, -300, 300, 300));
        int curFieldRiftCount = (int) field.getSummons().stream().filter(s -> s.getSkillID() == RIFT_OF_DAMNATION_SUMMON && s.getChr() == chr).count();
        SkillInfo si = SkillData.getSkillInfoById(RIFT_OF_DAMNATION);
        int slv = chr.getSkillLevel(RIFT_OF_DAMNATION);
        Summon riftInRect = field.getSummonByChrAndSkillIdInRect(chr, RIFT_OF_DAMNATION_SUMMON, rect);
        switch (attackInfo.skillId) {
            case RIFT_OF_DAMNATION_ATTACK:
                if (riftInRect == null) {
                    if (curFieldRiftCount >= 2 || chr.hasSkillOnCooldown(RIFT_OF_DAMNATION_SUMMON)) {
                        return;
                    }
                    Summon summon = Summon.getSummonByNoCTS(chr, RIFT_OF_DAMNATION_SUMMON, slv);
                    summon.setState(1);
                    summon.setFlip(!attackInfo.left);
                    summon.setMoveAbility(MoveAbility.Stop);
                    field.spawnAddSummon(summon);
                } else {
                    field.broadcastPacket(Summoned.stateChanged(riftInRect, 1, new HashMap<>()));
                }
                break;
            case RIFT_OF_DAMNATION_SUMMON:
                if (riftInRect != null) {
                    field.removeLife(riftInRect);
                    chr.addSkillCoolTime(attackInfo.skillId, si.getValue(z, slv) * 1000);
                }
                break;
        }
    }

    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (cts == GlimmeringTime) {
            tsm.removeStatsBySkill(EQUINOX_CYCLE_MOON);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_SUN);
        }
    }

    private void giveMoonBuffs() {
        if (!chr.hasSkill(FALLING_MOON)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Skill skill = chr.getSkill(FALLING_MOON);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        SkillInfo mosSI = SkillData.getSkillInfoById(MASTER_OF_THE_SWORD);
        int mosSLV = chr.getSkillLevel(MASTER_OF_THE_SWORD);
        if (tsm.getOption(PoseType).nOption != 1) {
            tsm.removeStatsBySkill(RISING_SUN);
        }
        o1.nOption = 1;
        o1.rOption = skill.getSkillId();
        if (tsm.hasStat(GlimmeringTime)) {
            o1.bOption = 1;
        }
        tsm.putCharacterStatValue(PoseType, o1);
        o2.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(indieCr, mosSLV) : si.getValue(indieCr, slv);
        o2.nReason = skill.getSkillId();
        tsm.putCharacterStatValue(IndieCr, o2);
        o3.nOption = 1;
        o3.rOption = skill.getSkillId();
        tsm.putCharacterStatValue(BuckShot, o3);
    }

    private void giveSunBuffs() {
        if (!chr.hasSkill(RISING_SUN)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();

        Skill skill = chr.getSkill(RISING_SUN);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        SkillInfo mosSI = SkillData.getSkillInfoById(MASTER_OF_THE_SWORD);
        int mosSLV = chr.getSkillLevel(MASTER_OF_THE_SWORD);
        if (tsm.getOption(PoseType).nOption != 2) {
            tsm.removeStatsBySkill(FALLING_MOON);
        }
        o1.nOption = 2;
        o1.rOption = skill.getSkillId();
        if (tsm.hasStat(GlimmeringTime)) {
            o1.bOption = 1;
        }
        tsm.putCharacterStatValue(PoseType, o1);
        o2.nReason = skill.getSkillId();
        o2.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(v, mosSLV) : si.getValue(indieDamR, slv);
        tsm.putCharacterStatValue(IndieDamR, o2); //Indie
        o3.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? -2 : si.getValue(indieBooster, slv);
        o3.nReason = skill.getSkillId();
        tsm.putCharacterStatValue(IndieBooster, o3);
    }

    private void equinox(TemporaryStatManager tsm) {
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        SkillInfo mosSI = SkillData.getSkillInfoById(MASTER_OF_THE_SWORD);
        //Rising Sun Skill Info
        Skill skillRS = chr.getSkill(RISING_SUN);
        int slvRS = (byte) skillRS.getCurrentLevel();
        SkillInfo siRS = SkillData.getSkillInfoById(skillRS.getSkillId());
        //Falling Moon Skill Info
        Skill skillFM = chr.getSkill(FALLING_MOON);
        int slvFM = (byte) skillFM.getCurrentLevel();
        SkillInfo siFM = SkillData.getSkillInfoById(skillFM.getSkillId());


        if (!tsm.hasStat(GlimmeringTime)) {
            return;
        }
        if (tsm.getOption(PoseType).nOption == 1) {
            //Switch to Rising Sun State
            o1.nOption = 2;
            o1.rOption = RISING_SUN;
            o1.bOption = 1;
            tsm.putCharacterStatValue(PoseType, o1);

            tsm.removeStatsBySkill(FALLING_MOON);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_SUN);

            o2.nReason = RISING_SUN;
            o2.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(v, slvRS) : siRS.getValue(indieDamR, slvRS);
            tsm.putCharacterStatValue(IndieDamR, o2); //Indie

            o3.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? -2 : siRS.getValue(indieBooster, slvRS);
            o3.nReason = RISING_SUN;
            o3.tTerm = siRS.getValue(time, slvRS);
            tsm.putCharacterStatValue(IndieBooster, o3);

            //Invisible Moon Buffs
            o4.nOption = 1;
            o4.rOption = EQUINOX_CYCLE_MOON;
            tsm.putCharacterStatValue(BuckShot, o4);

            o5.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(indieCr, slvFM) : siFM.getValue(indieCr, slvFM);
            o5.nReason = EQUINOX_CYCLE_MOON;
            tsm.putCharacterStatValue(IndieCr, o5);
        } else {
            //Switch to Falling Moon State
            o1.nOption = 1;
            o1.rOption = FALLING_MOON;
            o1.bOption = 1;
            tsm.putCharacterStatValue(PoseType, o1);

            tsm.removeStatsBySkill(RISING_SUN);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_MOON);

            o2.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(indieCr, slvFM) : siFM.getValue(indieCr, slvFM);
            o2.nReason = FALLING_MOON;
            o2.tTerm = siFM.getValue(time, slvFM);
            tsm.putCharacterStatValue(IndieCr, o2);

            o3.nOption = 1;
            o3.rOption = FALLING_MOON;
            tsm.putCharacterStatValue(BuckShot, o3);

            //Invisible Sun Buffs
            o4.nReason = EQUINOX_CYCLE_SUN;
            o4.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? mosSI.getValue(v, slvRS) : siRS.getValue(indieDamR, slvRS);
            tsm.putCharacterStatValue(IndieDamR, o4);

            o5.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? -2 : siRS.getValue(indieBooster, slvRS);
            o5.nReason = EQUINOX_CYCLE_SUN;
            o5.tTerm = siRS.getValue(time, slvRS);
            tsm.putCharacterStatValue(IndieBooster, o5);
        }
        tsm.sendSetStatPacket();
    }

    private void applySoulElementOnMob(AttackInfo attackInfo, int slv) {
        Option o1 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(SOUL_ELEMENT);
        o1.nOption = 1;
        o1.rOption = SOUL_ELEMENT;
        o1.tOption = si.getValue(subTime, slv);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(si.getValue(prop, slv))) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                MobTemporaryStat mts = mob.getTemporaryStat();
                if (!mob.isBoss()) {
                    mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }


    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, TemporaryStatManager tsm, int skillID, int slv, InPacket inPacket, SkillUseInfo skillUseInfo) {
        super.handleSkill(chr, tsm, skillID, slv, inPacket, skillUseInfo);
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        Summon summon;
        Field field = chr.getField();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case TRUE_SIGHT:
                // Mob Def = v
                // Final Dmg on mob = s
                Rect rect = new Rect(
                        new Position(
                                -500,
                                -500),
                        new Position(
                                +500,
                                +500)
                );
                Rect rectAround = chr.getPosition().getRectAround(rect);

                if (!chr.isLeft()) {
                    rectAround = rectAround.horizontalFlipAround(chr.getPosition().getX());
                }
                o1.nOption = -(si.getValue(v, slv) + (chr.hasSkill(TRUE_SIGHT_GUARDBREAK) ? 10 : 0));
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) + (chr.hasSkill(TRUE_SIGHT_PERSIST) ? 20 : 0);
                o2.nOption = si.getValue(s, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) + (chr.hasSkill(TRUE_SIGHT_PERSIST) ? 20 : 0);
                o3.nOption = chr.hasSkill(TRUE_SIGHT_ENHANCE) ? -10 : 0;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv) + (chr.hasSkill(TRUE_SIGHT_PERSIST) ? 20 : 0);
                for (Life life : chr.getField().getLifesInRect(rectAround)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (Util.succeedProp(si.getValue(prop, slv))) {
                            mts.addStatOptionsAndBroadcast(MobStat.PDR, o1);
                            mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                            mts.addStatOptionsAndBroadcast(MobStat.FinalDmgReceived, o2);
                            mts.addStatOptionsAndBroadcast(MobStat.TrueSight, o3);
                        }
                    }
                }
                break;
            case SOUL_ELEMENT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulMasterFinal, o1);
                break;
            case SOUL_SPEED:
                o1.nValue = -5; // si.getValue(x, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                break;
            case FALLING_MOON:
                giveMoonBuffs();
                break;
            case RISING_SUN:
                giveSunBuffs();
                break;
            case EQUINOX_CYCLE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(GlimmeringTime, o1);
                break;
            case CALL_OF_CYGNUS_DW:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case SOUL_FORGE:
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(LightOfSpirit, o3);
                break;
            case GLORY_OF_THE_GUARDIANS_DW:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                break;
            case CELESTIAL_DANCE:
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setAvatarLook(chr.getAvatarData().getAvatarLook());
                summon.setMoveAbility(MoveAbility.Walk);
                summon.setAttackActive(true);
                field.spawnSummon(summon);
                break;
            case RIFT_OF_DAMNATION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(RiftOfDamnation, o1);
                break;
            case SOUL_ECLIPSE:
                if (field.getSummonByChrAndSkillId(chr, skillID) == null) {
                    summon = Summon.getSummonByNoCTS(chr, skillID, slv);
                    summon.setMoveAbility(MoveAbility.Walk);
                    field.spawnSummon(summon);
                    o1.nValue = 1;
                    o1.nReason = skillID;
                    o1.tTerm = 1;
                    tsm.putCharacterStatValue(IndieNotDamaged, o1); // Invincibility
                }
                break;
        }
        tsm.sendSetStatPacket();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {
        super.handleHit(c, inPacket, hitInfo);
    }

    @Override
    public void cancelTimers() {
        if (willOfSteelTimer != null) {
            willOfSteelTimer.cancel(false);
        }
        super.cancelTimers();
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
        short level = chr.getLevel();
        switch (level) {
//            case 30:
//                handleJobAdvance(JobConstants.JobEnum.DAWN_WARRIOR_2.getJobId());
//                break;
//            case 60:
//                handleJobAdvance(JobConstants.JobEnum.DAWN_WARRIOR_3.getJobId());
//                break;
//            case 100:
//                handleJobAdvance(JobConstants.JobEnum.DAWN_WARRIOR_4.getJobId());
//                break;
            case 120:
                giveCallOfCygnus(CALL_OF_CYGNUS_DW);
                break;
        }
    }

    @Override
    public void handleJobStart() {
        super.handleJobStart();
    }

    @Override
    public void handleJobEnd() {
        super.handleJobEnd();

        Item sword = ItemData.getItemDeepCopy(1302077);
        chr.addItemToInventory(sword);
    }
}