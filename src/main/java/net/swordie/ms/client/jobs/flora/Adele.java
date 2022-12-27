package net.swordie.ms.client.jobs.flora;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.ForceAtom;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.*;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.CFamiliar;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.Wreckage;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * @author Sjonnie
 * Created on 6/25/2018.
 */
public class Adele extends Job {

    public static final int 再訪 = 150021000;
    public static final int 魔法迴路 = 150020079;
    public static final int 信念 = 150020006;//完成某任務會給

    public static final int 平砍 = 151001000;
    public static final int 懸浮 = 151001004;
    public static final int 碎片 = 151001001;


    public static final int 乙太 = 151100017;
    public static final int 乙太結晶 = 151100002;
    public static final int 創造 = 151101006;
    public static final int 奇蹟 = 151101013;
    public static final int 推進器 = 151101005;
    public static final int 刺擊 = 151101000;


    public static final int 雷普勇士的意志 = 151121006;
    public static final int 雷普的勇士 = 151121005;

    public static final int SPELL_BULLETS = 155001103;
    public static final int SPECTER_STATE = 155000007;
    public static final int CORRUPTION_COOLDOWN = 155001008;

    public static final int MASTER_CORRUPTION = 155101006;
    public static final int ENHANCED_SPECTRA = 155120034;

    // Hyper Skills


    // V skills


    private ScheduledFuture spectraEnergyTimer;
    List<CharacterTemporaryStat> spellCasts = Arrays.asList(AbyssalCast, GustCast, ScarletCast, BasicCast);
    private int[] addedSkills = new int[]{
            再訪,魔法迴路,懸浮
    };

    public Adele(Char chr) {
        super(chr);
        if (chr != null && chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
            if (spectraEnergyTimer != null && !spectraEnergyTimer.isDone()) {
                spectraEnergyTimer.cancel(true);
            }
            if (chr.hasSkill(乙太)){
                //System.out.println("自動獲得乙太");
                //spectraEnergyTimer = EventManager.addFixedRateEvent(this::自動獲得乙太, 0, 5000);
            }
        }
    }

    private void 改變乙太計量(boolean ) {

    }
    private void 自動獲得乙太() {
        if (chr == null || chr.getField() == null) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(乙太);
        SkillInfo si = SkillData.getSkillInfoById(乙太);
        int slv = (byte) skill.getCurrentLevel();
        o.nOption = si.getValue(z, slv);
        o.rOption = skill.getSkillId();
        tsm.putCharacterStatValue(CharacterTemporaryStat.乙太, o);
        tsm.sendSetStatPacket();
        //tsm.removeStatsBySkill(乙太);
    }
/*
    private void 乙太結晶(Char chr, int gain, final int skillid, final boolean refresh) {
        if (refresh) {
            if () {

            }
        }
    }*/

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isAdele(id);
    }

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
            slv = skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }
        if (hasHitMobs
                && !SkillConstants.isArkForceAtomAttack(attackInfo.skillId)
                && attackInfo.skillId != CONVERSION_OVERDRIVE_ATTACK) {
            bonusConversionOverdriveAttack();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Field field = chr.getField();
        switch (attackInfo.skillId) {
            case 碎片:
                if (hasHitMobs) {
                    Skill skill2 = chr.getSkill(乙太);
                    SkillInfo si2 = SkillData.getSkillInfoById(乙太);
                    int slv2 = (byte) skill2.getCurrentLevel();
                    o1.nOption = si2.getValue(s, slv);
                    o1.rOption = skill2.getSkillId();
                    tsm.putCharacterStatValue(CharacterTemporaryStat.乙太, o1);
                }
                break;
            case 刺擊:
                if (hasHitMobs) {
                    Skill skill2 = chr.getSkill(乙太);
                    SkillInfo si2 = SkillData.getSkillInfoById(乙太);
                    int slv2 = (byte) skill2.getCurrentLevel();
                    o1.nOption = si2.getValue(s, slv2);
                    o1.rOption = skill2.getSkillId();
                    tsm.putCharacterStatValue(CharacterTemporaryStat.乙太, o1);
                }
                break;
        }
        if (isBuff(attackInfo.skillId)) {
            tsm.sendSetStatPacket();
        }
        super.handleAttack(c, attackInfo);
    }


    @Override
    public int getFinalAttackSkill() {
        return 0;
    }

    @Override
    public void handleSkill(Char chr, TemporaryStatManager tsm, int skillID, int slv, InPacket inPacket, SkillUseInfo skillUseInfo) {
        super.handleSkill(chr, tsm, skillID, slv, inPacket, skillUseInfo);
        SkillInfo si = null;
        if (chr.getSkill(skillID) != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        switch (skillID) {
            case 雷普的勇士:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case 雷普勇士的意志:
                tsm.removeAllDebuffs();
                break;
            case 懸浮:
                o1.nValue = 1;
                o1.nReason = skillID;
                o1.tTerm = si.getValue(time, slv) / 1000;
                tsm.putCharacterStatValue(IndieFloating, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) / 1000 - 1;
                tsm.putCharacterStatValue(NewFlying, o2);
                break;
            case 創造:
                o1.nOption = si.getValue(w, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(CharacterTemporaryStat.創造, o1);
                break;
            case 奇蹟:
                o1.nOption = si.getValue(damPlus, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(CharacterTemporaryStat.奇蹟, o1);
                break;
            case 推進器:
                o1.nOption = si.getValue(mpCon, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void createEther(List<Wreckage> wreckageList) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Field field = chr.getField();
        for (Wreckage wreckage : wreckageList) {

        }
        field.removeWreckage(chr, wreckageList);
    }

    @Override
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {
        super.handleHit(c, inPacket, hitInfo);
    }

    @Override
    public void cancelTimers() {
        if (spectraEnergyTimer != null) {
            spectraEnergyTimer.cancel(false);
        }
        super.cancelTimers();
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        // cs.setPosMap(100000000); // default: 402090000
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
        short level = chr.getLevel();
        switch (level) {
            case 10:
                handleJobAdvance(JobConstants.JobEnum.ADELE_1.getJobId());
                break;
            case 30:
                handleJobAdvance(JobConstants.JobEnum.ADELE_2.getJobId());
                break;
            case 60:
                handleJobAdvance(JobConstants.JobEnum.ADELE_3.getJobId());
                break;
            case 100:
                handleJobAdvance(JobConstants.JobEnum.ADELE_4.getJobId());
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
        handleJobAdvance(JobConstants.JobEnum.ADELE_1.getJobId());
        chr.forceUpdateSecondary(null, ItemData.getItemDeepCopy(1353600)); // Initial Path (2ndary)
        chr.addSpToJobByCurrentJob(3);
    }
}
