package net.swordie.ms.client.jobs.adventurer;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.ForceAtom;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.*;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Kinesis extends Job {
    public static final int 回歸_凱內西斯 = 140001290;

    public static final int 心靈推手 = 142001000;
    public static final int 心靈本能 = 142001007;
    public static final int ESP_加速器 = 142001003;
    public static final int 終極技_梅泰利爾 = 142001002;

    public static final int 心靈推手2 = 142100000;
    public static final int 心靈推手2_共享 = 142100001;
    public static final int 心靈領域 = 142101009;
    public static final int 心靈護盾 = 142101004;
    public static final int 純粹的力量 = 142100005;
    public static final int ULTIMATE_DEEP_IMPACT = 142101003;

    public static final int 心靈推手3 = 142110000;
    public static final int 心靈推手3_共享 = 142110001;
    public static final int 心靈護盾2_扭曲 = 142110009;
    public static final int 精神強化 = 142110008;
    public static final int 心靈遊動 = 142111010;
    public static final int 終極技_火車扔擲 = 142111007;
    public static final int KINETIC_COMBO = 142110011;
    public static final int MIND_TREMOR = 142111006;

    public static final int 心靈突破 = 142121004;
    public static final int ULTIMATE_PSYCHIC_SHOT = 142120002;
    public static final int 終極技_BPM = 142121005;
    public static final int 異界祝禱 = 142121016;
    public static final int 心靈填充 = 142121008;
    public static final int TELEPATH_TACTICS = 142121006;
    public static final int MIND_QUAKE = 142120003;
    public static final int 精神淨化 = 142121007;

    public static final int MENTAL_TEMPEST = 142121030;
    public static final int MENTAL_TEMPEST_END = 142120030;
    public static final int MENTAL_SHOCK = 142121031;
    public static final int 心靈超越 = 142121032;

    // V skills
    public static final int 心靈龍捲風 = 400021008;
    public static final int 終極_移動物質 = 400021048;


    private static final int MAX_PP = 30;

    private static final int[] nonOrbSkills = new int[]{
            終極技_梅泰利爾,
            ULTIMATE_DEEP_IMPACT,
            終極技_火車扔擲,
            終極技_BPM,
            ULTIMATE_PSYCHIC_SHOT,

            心靈推手,
            心靈推手2_共享,
            心靈推手2,
            心靈推手3_共享,
            心靈推手3,
            心靈領域,
            MENTAL_TEMPEST,
            KINETIC_COMBO,
    };

    public Kinesis(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isKinesis(id);
    }

    public int getPP() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(KinesisPsychicPoint)) {
            return tsm.getOption(KinesisPsychicPoint).nOption;
        }
        return 0;
    }

    public void addPP(int amount) {
        int pp = getPP() + amount > MAX_PP ? MAX_PP : getPP() + amount;
        sendPPPacket(pp);
    }

    public void substractPP(int amount) {
        int pp = getPP() - amount < 0 ? 0 : getPP() - amount;
        sendPPPacket(pp);
    }

    private void sendPPPacket(int pp) {
        Option o = new Option();
        o.nOption = pp;
        o.rOption = JobConstants.JobEnum.KINESIS_4.getJobId();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.putCharacterStatValue(KinesisPsychicPoint, o);
        tsm.sendSetStatPacket();
    }


    public void applyMindAreaDebuff(int skillId, Position position, List<Mob> mobList) {
        Skill skill = chr.getSkill(skillId);
        if (skill == null) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        Rect rect = position.getRectAround(si.getFirstRect());

        int multiplier = skillId == MIND_QUAKE ? 3 : 2;
        int mobCount = mobList.size();

        Option o1 = new Option();
        Option o2 = new Option();
        o1.rOption = skillId;
        o1.tOption = si.getValue(time, slv);
        o2.rOption = skillId;
        o2.tOption = si.getValue(time, slv);

        mobList.forEach(m -> {
            if (rect.hasPositionInside(m.getPosition())) {
                MobTemporaryStat mts = m.getTemporaryStat();


                o1.nOption = -(multiplier * mobCount > si.getValue(s, slv) ? si.getValue(s, slv) : multiplier * mobCount);
                o2.nOption = multiplier * mobCount > si.getValue(s, slv) ? si.getValue(s, slv) : multiplier * mobCount;
                mts.addStatOptionsAndBroadcast(MobStat.PDR, o1);
                mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                mts.addStatOptionsAndBroadcast(MobStat.PsychicGroundMark, o2);
            }
        });
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Client c, AttackInfo attackInfo) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        if (skill == null) {
            switch (attackInfo.skillId) {
                case 心靈推手3_共享:
                    skill = chr.getSkill(心靈推手3);
                    break;
                case 心靈推手2_共享:
                    skill = chr.getSkill(心靈推手2);
                    break;
            }
        }
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }
        if (hasHitMobs && chr.hasSkill(KINETIC_COMBO) && attackInfo.skillId != KINETIC_COMBO) {
            createKineticOrbForceAtom(skillID, slv, attackInfo);
        }
        if (attackInfo.skillId != 終極技_BPM &&
                attackInfo.skillId != 終極技_梅泰利爾 &&
                attackInfo.skillId != 終極技_火車扔擲 &&
                attackInfo.skillId != 終極_移動物質 &&
                attackInfo.skillId != ULTIMATE_PSYCHIC_SHOT
        ) {
            kinesisPPAttack(skillID, slv, si);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case 心靈推手:
            case 心靈推手2:
            case 心靈推手2_共享:
            case 心靈推手3:
            case 心靈推手3_共享:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skill);
                }
                break;
            case 心靈突破:
                int count = 0;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (mob.isBoss()) {
                        count += si.getValue(x, slv);
                    } else {
                        count++;
                    }
                }
                count = count > si.getValue(w, slv) ? si.getValue(w, slv) : count;
                if (count <= 0) {
                    return;
                }
                o1.nValue = count * si.getValue(indiePMdR, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePMdR, o1);//
                tsm.sendSetStatPacket();
                break;
            case MENTAL_SHOCK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (!mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case MENTAL_TEMPEST:
                if (!tsm.hasStatBySkillId(skillID)) {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = 5;
                    tsm.putCharacterStatValue(NotDamaged, o1);//發56 和 486
                    tsm.sendSetStatPacket();
                }
                break;
            case MENTAL_TEMPEST_END:
                tsm.removeStatsBySkill(MENTAL_TEMPEST);
                addPP(30);
                break;
            case ULTIMATE_DEEP_IMPACT:
                attackInfo.mobAttackInfo.forEach(s -> {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(s.mobId);
                    if (mob != null && !mob.isBoss()) {
                        mob.getTemporaryStat().removeBuffs();
                    }
                });
                break;
            case ULTIMATE_PSYCHIC_SHOT:
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = ULTIMATE_PSYCHIC_SHOT;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.PDR, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                }
                break;
        }

        super.handleAttack(c, attackInfo);
    }

    private void createKineticOrbForceAtom(int skillID, int slv, AttackInfo attackInfo) {
        if (Arrays.asList(nonOrbSkills).contains(skillID) || Arrays.asList(nonOrbSkills).contains(attackInfo.skillId)) {
            return;
        }
        Field field = chr.getField();
        SkillInfo si = SkillData.getSkillInfoById(KINETIC_COMBO);
        int proc = si.getValue(prop, chr.getSkillLevel(KINETIC_COMBO));
        ForceAtomEnum fae = ForceAtomEnum.KINESIS_ORB_REAL;
        Rect rect = chr.getRectAround(new Rect(-500, -300, 200, 100));
        if (!chr.isLeft()) {
            rect = rect.horizontalFlipAround(chr.getPosition().getX());
        }
        List<Integer> targetList = new ArrayList<>();
        List<ForceAtomInfo> faiList = new ArrayList<>();

        if (Util.succeedProp(proc) && field.getMobsInRect(rect).size() > 0) {
            Mob mob = Util.getRandomFromCollection(field.getMobsInRect(rect));
            targetList.add(mob.getObjectId());

            int ranStuff = new Random().nextInt(3);
            int fImpact = new Random().nextInt(31) + 20;
            int sImpact = new Random().nextInt(25) + 10;
            ForceAtomInfo fai = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc() + ranStuff, fImpact, sImpact,
                    new Random().nextInt(360), new Random().nextInt(400) + 400, Util.getCurrentTime(), 0, 0,
                    new Position());
            faiList.add(fai);
        }

        if (faiList.size() > 0 && targetList.size() > 0) {
            ForceAtom fa = new ForceAtom(chr.getId(), fae, targetList, KINETIC_COMBO, faiList);
            chr.createForceAtom(fa);
        }
    }

    private void kinesisPPAttack(int skillID, int slv, SkillInfo si) {
        if (si == null) {
            if (skillID == 0) {
                addPP(1);
            }
            return;
        }
        int ppRec = si.getValue(ppRecovery, slv);
        addPP(ppRec);
        int ppCons = si.getValue(ppCon, slv);
        if (chr.getTemporaryStatManager().hasStat(KinesisPsychicOver)) {
            ppCons = ppCons / 2;
        }
        if (skillID == 終極技_BPM) {
            ppCons = si.getValue(w, slv); // why nexon..
        }
        if (skillID == 心靈遊動) {
            ppCons = si.getValue(x, slv); // why nexon..
        }
        substractPP(ppCons);
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
        kinesisPPAttack(skillID, slv, si);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case 心靈填充:
                int add = (MAX_PP - getPP()) / 2;
                addPP(add);
                break;
            case 精神淨化:
                tsm.removeAllDebuffs();
                break;
            case ESP_加速器:
                o1.nValue = -5; // si.getValue(indieBooster, slv);
                si.getValue(indieBooster, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                break;
            case 心靈本能:
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                } else {
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(KinesisPsychicShield, o1);//KinesisPsychicEnergeShield
                }
                break;
            case 心靈護盾://改為被動
            case 心靈護盾2_扭曲://改為被動
                int psyArmorSLV = chr.getSkillLevel(心靈護盾);
                int t = SkillData.getSkillInfoById(心靈護盾).getValue(time, psyArmorSLV);
                int e = SkillData.getSkillInfoById(心靈護盾).getValue(er, psyArmorSLV);
                o1.nValue = si.getValue(indiePdd, slv);
                o1.nReason = skillID;
                o1.tTerm = t;
                tsm.putCharacterStatValue(IndieDEF, o1);
                o2.nValue = e;
                o2.nReason = skillID;
                o2.tTerm = t;
                tsm.putCharacterStatValue(IndieEVAR, o2);
                o3.nValue = si.getValue(stanceProp, slv);
                o3.nReason = skillID;
                o3.tTerm = t;
                tsm.putCharacterStatValue(IndieStance, o3);
                break;
            case 純粹的力量://改為被動
                o1.nValue = si.getValue(indieDamR, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                break;
            case 精神強化://改為被動
                o1.nValue = si.getValue(indieMadR, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMADR, o1);
                break;
            case 異界祝禱:
                o1.nValue = si.getValue(x, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);//116 IndieStatR
                break;
            case TELEPATH_TACTICS:
                o1.nValue = si.getValue(indieMad, slv);
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMADR, o1);
                o2.nValue = si.getValue(indieDamR, slv);
                o2.nReason = skillID;
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2);
                break;
            case 心靈遊動:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NewFlying, o1);
                break;
            case 心靈超越://發了4個stat 未處理完
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(KinesisPsychicOver, o1);
                break;
            case 心靈龍捲風: // TODO  Increment as the Tornado is growing.
                o1.nOption = 3;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PsychicTornado, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(心靈本能)) {
            hitInfo.hpDamage = (int) (hitInfo.hpDamage * (tsm.getOption(KinesisPsychicShield).nOption / 100D)); //KinesisPsychicEnergeShield
            substractPP(1);
        }
        if (getPP() <= 0) {
            tsm.removeStatsBySkill(心靈本能);
        }
        super.handleHit(c, inPacket, hitInfo);
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);

        Item item = ItemData.getItemDeepCopy(1353200); // Pawn Chess Piece
        item.setBagIndex(BodyPart.Shield.getVal());
        chr.getEquippedInventory().addItem(item);

        /*
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setLevel(10);
        cs.setMaxHp(400);
        cs.setHp(400);
        cs.setInt(58);
        cs.setAp(5);
         */
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();

        short level = chr.getLevel();
        switch (level) {
            case 30:
                handleJobAdvance(JobConstants.JobEnum.KINESIS_2.getJobId());
                break;
            case 60:
                handleJobAdvance(JobConstants.JobEnum.KINESIS_3.getJobId());
                break;
            case 100:
                handleJobAdvance(JobConstants.JobEnum.KINESIS_4.getJobId());
                break;
        }
    }

    @Override
    public void handleJobStart() {
        super.handleJobStart();

        handleJobEnd();
    }

    @Override
    public void handleJobEnd() {
        super.handleJobEnd();
        chr.forceUpdateSecondary(null, ItemData.getItemDeepCopy(1353200)); // Pawn Chess Piece
        handleJobAdvance(JobConstants.JobEnum.KINESIS_1.getJobId());
        chr.addSpToJobByCurrentJob(5);

    }
}
