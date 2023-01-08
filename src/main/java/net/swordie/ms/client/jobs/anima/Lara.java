package net.swordie.ms.client.jobs.anima;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.ForceAtom;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.info.SkillUseInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Summoned;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.AssistType;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;
import org.graalvm.nativeimage.c.struct.CField;

import java.util.HashMap;
import java.util.Map;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

public class Lara extends Job {

    //0轉
    public static final int 前往納林 = 160011074;
    public static final int 大自然夥伴 = 160010001;
    public static final int 形象變幻 = 160011075;
    public static final int 獨門咒語 = 160001005;

    //1轉
    public static final int 精氣散播 = 162001000;
    public static final int 小山靈 = 162000003;
    public static final int 小山靈_1 = 162001004;
    public static final int 山不敗 = 162001005;

    //2轉
    public static final int 龍脈讀取 = 162101000;
    public static final int 龍脈釋放 = 162101001;
    public static final int 釋放_波瀾之江 = 162100002;
    public static final int 釋放_波瀾之江_1 = 162101003;
    public static final int 釋放_波瀾之江_2 = 162101004;
    //3轉

    //4轉

    //超技能

    // V skills


    private static final int[] addedSkills = new int[]{
            前往納林,
            獨門咒語,
    };

    public Lara(Char chr) {
        super(chr);
        if (chr != null && chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isLara(id);
    }

    public void handleNatureFriend() {
        final int skilllv = (chr.getSkillLevel(80003070) > 0) ? chr.getSkillLevel(80003070) : chr.getSkillLevel(160010001);
        final int skillId = (chr.getSkillLevel(80003070) > 0) ? 80003070 : 160010001;
        if (chr.hasSkillOnCooldown(skillId)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillId);
        SkillInfo si = null;
        Option o1 = new Option();
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
        }
        if (!tsm.hasStat(CharacterTemporaryStat.大自然夥伴)) {
            chr.setSkillCustomInfo(80003070, 0L, 0L);
        } else {
            chr.addSkillCustomInfo(80003070, 1L);
        }
        if (chr.getSkillCustomValue0(80003070) >= si.getValue(x, skilllv)) {
            chr.addSkillCoolTime(skillId, System.currentTimeMillis());
            chr.write(UserLocal.skillCooltimeSetM(skillId, si.getValue(cooltime, skilllv)));
            tsm.removeStatsBySkill(80003070);
        } else {
            o1.nOption = 5;
            o1.tOption = si.getValue(time, skilllv);
            tsm.putCharacterStatValue(CharacterTemporaryStat.大自然夥伴, o1);
            tsm.sendSetStatPacket();
        }
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
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        if (hasHitMobs
                && attackInfo.skillId != CONVERSION_OVERDRIVE_ATTACK) {
            bonusConversionOverdriveAttack();
        }
        switch (attackInfo.skillId) {         

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
        switch (skillID) {
            case 山不敗:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(CharacterTemporaryStat.山不敗, o1);
                tsm.sendSetStatPacket();
                break;
        }
        if (chr.getSkillLevel(160010001) > 0 || chr.getSkillLevel(80003058) > 0) {
            handleNatureFriend();
        }

    }

    public void handleShootObj(Char chr, int skillId, int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {

        }
        super.handleShootObj(chr, skillId, slv);
    }


    public void handleRemoveCTS(CharacterTemporaryStat cts) {

    }

    @Override
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {
        super.handleHit(c, inPacket, hitInfo);
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        //cs.setPosMap(100000000); // default - 940202013
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
        int level = chr.getLevel();
        switch (level) {
            case 30:
                handleJobAdvance(JobConstants.JobEnum.LARA_2.getJobId());
                chr.addSpToJobByCurrentJob(7);
                break;
            case 60:
                handleJobAdvance(JobConstants.JobEnum.LARA_3.getJobId());
                chr.addSpToJobByCurrentJob(7);
                break;
            case 100:
                handleJobAdvance(JobConstants.JobEnum.LARA_4.getJobId());
                chr.addSpToJobByCurrentJob(5);
                break;
        }
    }

    @Override
    public void handleJobStart() {
        super.handleJobStart();

        handleJobAdvance(JobConstants.JobEnum.LARA_1.getJobId());

        handleJobEnd();
    }

    @Override
    public void handleJobEnd() {
        super.handleJobEnd();
        chr.forceUpdateSecondary(null, ItemData.getItemDeepCopy(1354020)); // 樸素的四玉飾品
        chr.addSpToJobByCurrentJob(8);
    }
}
