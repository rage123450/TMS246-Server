package net.swordie.ms.handlers.user;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.ExtendSP;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.legend.Luminous;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.QuestConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.InstanceTableType;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.loaders.containerclasses.MakingSkillRecipe;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserStatHandler {

    private static final Logger log = Logger.getLogger(UserStatHandler.class);

    @Handler(op = InHeader.USER_SKILL_UP_REQUEST)
    public static void handleUserSkillUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int skillId = inPacket.decodeInt();
        int amount = inPacket.decodeInt();

        if (amount < 0) {
            chr.dispose();
            return;
        } else if (amount == 0) {
            amount = 1;
        }
        // separate skill/current skills for adding stuff to the base cache if everything is successful
        Skill skill = SkillData.getSkillDeepCopyById(skillId);
        Skill curSkill = chr.getSkill(skillId);
        if (skill == null) {
            chr.chatMessage("Skill does not exist. Contact the administrator with more information");
            return;
        }
        byte jobLevel = (byte) JobConstants.getJobLevel((short) skill.getRootId());
        if (JobConstants.isZero((short) skill.getRootId())) {
            jobLevel = JobConstants.getJobLevelByZeroSkillID(skillId);
        }
        Map<Stat, Object> stats;
        int rootId = skill.getRootId();
        if ((!JobConstants.isBeginnerJob((short) rootId) && !SkillConstants.isMatching(rootId, chr.getJob())) || SkillConstants.isSkillFromItem(skillId)) {
            chr.getOffenseManager().addOffense(String.format("Character %d tried adding an invalid skill (job %d, skill id %d)",
                    chr.getId(), rootId, skillId));
            return;
        }
        if (JobConstants.isBeginnerJob((short) rootId)) {
            stats = new HashMap<>();
            int spentSp = 0;
            for (Skill s : chr.getSkills()) {
                if (SkillConstants.isBeginnerSpAddableSkill(s.getSkillId())) {
                    int currentLevel = s.getCurrentLevel();
                    spentSp += currentLevel;
                }
            }
            int totalSp;
            if (JobConstants.isResistance((short) skill.getRootId())) {
                totalSp = Math.min(chr.getLevel(), GameConstants.RESISTANCE_SP_MAX_LV) - 1; // sp gained from 2~10
            } else {
                totalSp = Math.min(chr.getLevel(), GameConstants.BEGINNER_SP_MAX_LV) - 1; // sp gained from 2~7
            }
            if (totalSp - spentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                if (max == 0) {
                    // some beginner skills have no max level, default is 3
                    max = 3;
                }
                int newLevel = curLevel + amount > max ? max : curLevel + amount;
                skill.setCurrentLevel(newLevel);
            }
        } else if (JobConstants.isExtendSpJob(chr.getJob())) {
            ExtendSP esp = chr.getAvatarData().getCharacterStat().getExtendSP();
            int currentSp = esp.getSpByJobLevel(jobLevel);
            if (currentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = curLevel + amount > max ? max : curLevel + amount;
                skill.setCurrentLevel(newLevel);
                esp.setSpToJobLevel(jobLevel, currentSp - amount);
                stats = new HashMap<>();
                stats.put(Stat.sp, esp);
            } else {
                chr.getOffenseManager().addOffense(String.format("Character %d tried adding a skill without having the required amount of sp"
                                + " (required %d, has %d)",
                        chr.getId(), currentSp, amount));
                return;
            }
        } else {
            int currentSp = chr.getAvatarData().getCharacterStat().getSp();
            // check if character has enough skill points
            if (currentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = curLevel + amount > max ? max : curLevel + amount;
                skill.setCurrentLevel(newLevel);
                chr.getAvatarData().getCharacterStat().setSp(currentSp - amount);
                stats = new HashMap<>();
                stats.put(Stat.sp, chr.getAvatarData().getCharacterStat().getSp());
            } else {
                chr.getOffenseManager().addOffense(String.format("Character %d tried adding a skill without having the required amount of sp"
                                + " (required %d, has %d)",
                        chr.getId(), currentSp, amount));
                return;
            }
        }
        if (stats != null) {
            c.write(WvsContext.statChanged(stats));
            // Do one-off pair skill leveling for Luminous lol
            // Check if character is Luminous and if skill is pair skill
            if (JobConstants.isLuminous(chr.getJob()) &&
                    (skill.getSkillId() == Luminous.FLASH_SHOWER || skill.getSkillId() == Luminous.ABYSSAL_DROP)) {
                // Get opposite skill to level
                int otherId = skill.getSkillId() == Luminous.FLASH_SHOWER ? Luminous.ABYSSAL_DROP : Luminous.FLASH_SHOWER;
                Skill otherSkill = SkillData.getSkillDeepCopyById(otherId);
                if (otherSkill != null) {
                    otherSkill.setCurrentLevel(skill.getCurrentLevel());

                    chr.addSkill(skill);
                    c.write(WvsContext.changeSkillRecordResult(skill));

                    chr.addSkill(otherSkill);
                    c.write(WvsContext.changeSkillRecordResult(otherSkill));
                } else {
                    chr.chatMessage("Can't find other skill %d", otherId);
                }
            } else {
                chr.addSkill(skill);
                c.write(WvsContext.changeSkillRecordResult(skill));
            }
        } else {
            chr.getOffenseManager().addOffense(String.format("skill stats are null (%d)", skillId));
            chr.dispose();
        }
    }


    @Handler(op = InHeader.USER_ABILITY_UP_REQUEST)
    public static void handleUserAbilityUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (chr.getStat(Stat.ap) <= 0) {
            return;
        }
        inPacket.decodeInt(); // tick
        short stat = inPacket.decodeShort();
        Stat charStat = Stat.getByVal(stat);
        short amount = 1;
        boolean isHpOrMp = false;
        if (charStat == Stat.mmp || charStat == Stat.mhp) {
            isHpOrMp = true;
            amount = 20;
        }
        chr.addStat(charStat, amount);
        chr.addStat(Stat.ap, (short) -1);
        Map<Stat, Object> stats = new HashMap<>();
        if (isHpOrMp) {
            stats.put(charStat, chr.getStat(charStat));
        } else {
            stats.put(charStat, (short) chr.getStat(charStat));
        }
        stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats));
        chr.dispose();
    }

    @Handler(op = InHeader.USER_ABILITY_MASS_UP_REQUEST)
    public static void handleUserAbilityMassUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int type = inPacket.decodeInt();
        Stat charStat = null;
        short amount;
        if (type == 1) {
            charStat = Stat.getByVal(inPacket.decodeShort());
        } else if (type == 2) {
            inPacket.decodeInt();
            inPacket.decodeInt();
            inPacket.decodeInt();
            charStat = Stat.getByVal(inPacket.decodeShort());
        }
        inPacket.decodeInt();
        inPacket.decodeShort();
        amount = inPacket.decodeShort();
        short addStat = amount;
        if (chr.getStat(Stat.ap) < amount) {
            return;
        }
        boolean isHpOrMp = false;
        if (charStat == Stat.mmp || charStat == Stat.mhp) {
            isHpOrMp = true;
            addStat *= 20;
        }
        chr.addStat(charStat, addStat);
        chr.addStat(Stat.ap, (short) -amount);
        Map<Stat, Object> stats = new HashMap<>();
        if (isHpOrMp) {
            stats.put(charStat, chr.getStat(charStat));
        } else {
            stats.put(charStat, (short) chr.getStat(charStat));
        }
        stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats));
        chr.dispose();
    }

    @Handler(op = InHeader.USER_CHANGE_STAT_REQUEST)
    public static void handleUserChangeStatRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int mask = inPacket.decodeInt();
        List<Stat> stats = Stat.getStatsByFlag(mask); // should be in correct order
        inPacket.decodeInt();
        HashMap<Stat, Short> hashMap = new HashMap();
        for (Stat stat : stats) {
            hashMap.put(stat, inPacket.decodeShort()); // always short?
        }
        byte option = inPacket.decodeByte();
        if (hashMap.containsKey(Stat.hp)) {
            chr.heal(hashMap.get(Stat.hp));
        }
        if (hashMap.containsKey(Stat.mp)) {
            chr.healMP(hashMap.get(Stat.mp));
        }
//        c.write(WvsContext.statChanged(newStats));
    }

    @Handler(op = InHeader.USER_REQUEST_INSTANCE_TABLE)
    public static void handleUserRequestInstanceTable(Char chr, InPacket inPacket) {
        String requestStr = inPacket.decodeString();
        int type = inPacket.decodeInt();
        int subType = inPacket.decodeInt();

        InstanceTableType itt = InstanceTableType.getByStr(requestStr);
        if (itt == null) {
            log.error(String.format("Unknown instance table type request %s, type %d, subType %d", requestStr, type, subType));
            return;
        }
        int value;
        switch (itt) {
            // HyperSkills: both have the same requestStr. level = type * 5
            case HyperActiveSkill:
            case HyperPassiveSkill:
                if (subType == InstanceTableType.HyperActiveSkill.getSubType()) {
                    value = SkillConstants.getHyperActiveSkillSpByLv(type * 5);
                } else {
                    value = SkillConstants.getHyperPassiveSkillSpByLv(type * 5);
                }
                break;
            case HyperStatIncAmount:
                // type == level
                value = SkillConstants.getHyperStatSpByLv((short) type);
                break;
            case NeedHyperStatLv:
                // type == skill lv
                value = SkillConstants.getNeededSpForHyperStatSkill(type);
                break;
            case Skill_9200:
            case Skill_9201:
            case Skill_9202:
            case Skill_9203:
            case Skill_9204:
                // type == recommendSkillLevel - 1
                // subType == making skill level -1
                value = MakingSkillRecipe.getSuccessProb(Integer.parseInt(requestStr), type + 1, chr.getMakingSkillLevel(Integer.parseInt(requestStr)));
                break;
            default:
                log.error(String.format("Unhandled instance table type request %s, type %d, subType %d", itt, type, subType));
                return;
        }

        chr.write(WvsContext.resultInstanceTable(requestStr, type, subType, true, value));
    }

    @Handler(op = InHeader.USER_HYPER_SKILL_UP_REQUEST)
    public static void handleUserHyperSkillUpRequest(Char chr, InPacket inPacket) {
        // TODO: verify hyper skill is of the character's class
        inPacket.decodeInt(); // tick
        int skillID = inPacket.decodeInt();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        if (si == null) {
            log.error(String.format("Character %d attempted assigning hyper SP to a skill with null skillinfo (%d).", chr.getId(), skillID));
            chr.dispose();
            return;
        }
        if (si.getHyper() == 0 && si.getHyperStat() == 0 ||
                (!SkillConstants.isMatching(si.getRootId(), chr.getJob()) && !SkillConstants.isHyperstatSkill(skillID))) {
            log.error(String.format("Character %d attempted assigning hyper SP to a wrong skill (skill id %d, player job %d)", chr.getId(), skillID, chr.getJob()));
            chr.dispose();
            return;
        }
        Skill skill = chr.getSkill(skillID, true);
        short level = chr.getLevel();
        if (si.getHyper() != 0) { // Passive/Active hyper
            boolean active = si.getHyper() == 2;
            int totalHyperSp;
            int spentSp;
            if (active) {
                totalHyperSp = SkillConstants.getTotalActiveHyperSpByLevel(level);
                spentSp = chr.getSpentActiveHyperSkillSp();
            } else {
                totalHyperSp = SkillConstants.getTotalPassiveHyperSpByLevel(level);
                spentSp = chr.getSpentPassiveHyperSkillSp();
            }
            int availableSp = totalHyperSp - spentSp;
            if ((availableSp <= 0 || skill.getCurrentLevel() != 0) && !chr.isGM()) {
                log.error(String.format("Character %d attempted assigning hyper skill SP without having it, or too much. Available SP %d, current %d (%d, job %d)",
                        chr.getId(), availableSp, skill.getCurrentLevel(), skillID, chr.getJob()));
                chr.dispose();
                return;
            }
        } else if (si.getHyperStat() != 0) {
            int totalHyperSp = SkillConstants.getTotalHyperStatSpByLevel(level);
            int spentSp = chr.getSpentHyperSp();
            int availableSp = totalHyperSp - spentSp;
            int neededSp = SkillConstants.getNeededSpForHyperStatSkill(skill.getCurrentLevel() + 1);
            if (skill.getCurrentLevel() >= skill.getMaxLevel() || availableSp < neededSp) {
                log.error(String.format("Character %d attempted assigning too many hyper stat levels. Available SP %d, needed %d, current %d (%d, job %d)",
                        chr.getId(), availableSp, neededSp, skill.getCurrentLevel(), skillID, chr.getJob()));
                chr.dispose();
                return;
            }
        } else { // not hyper stat and not hyper skill
            log.error(String.format("Character %d attempted assigning hyper stat to an improper skill. (%d, job %d)", chr.getId(), skillID, chr.getJob()));
            chr.dispose();
            return;
        }
        chr.removeFromBaseStatCache(skill);
        skill.setCurrentLevel(skill.getCurrentLevel() + 1);
        chr.addToBaseStatCache(skill);
        chr.addSkill(skill);
        chr.write(WvsContext.changeSkillRecordResult(skill));
    }

    @Handler(op = InHeader.USER_HYPER_SKILL_RESET_REQUEST)
    public static void handleUserHyperSkillResetRequest(Char chr, InPacket inPacket) {
        // TODO
        chr.chatMessage("Todo: implement hyper skill reset");
        chr.dispose();
    }

    @Handler(op = InHeader.USER_HYPER_STAT_SKILL_UP_REQUEST)
    public static void handleUserHyperStatSkillUpRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        inPacket.decodeInt(); // 分頁
        int skillId = inPacket.decodeInt();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        if (si == null) {
            log.error(String.format("Character %d attempted assigning hyper SP to a skill with null skillinfo (%d).", chr.getId(), skillId));
            chr.dispose();
            return;
        }
        if (!SkillConstants.isHyperstatSkill(skillId)) {
            log.error(String.format("Character %d attempted assigning hyper stat SP to a wrong skill (skill id %d, player job %d)", chr.getId(), skillId, chr.getJob()));
            chr.dispose();
            return;
        }
        Skill skill = chr.getSkill(skillId, true);
        short level = chr.getLevel();
        if (si.getHyperStat() != 0) {
            if (skillId == Job.HYPER_STAT_ARCANE_FORCE && !chr.getQuestManager().hasQuestCompleted(QuestConstants.FIFTH_JOB_QUEST)) {
                chr.getOffenseManager().addOffense("Tried adding to AF hyper stat without having the proper quest completed.");
                chr.dispose();
                return;
            }
            int totalHyperSp = SkillConstants.getTotalHyperStatSpByLevel(level);
            int spentSp = chr.getSpentHyperSp();
            int availableSp = totalHyperSp - spentSp;
            int neededSp = SkillConstants.getNeededSpForHyperStatSkill(skill.getCurrentLevel() + 1);
            if (skill.getCurrentLevel() >= skill.getMaxLevel() || availableSp < neededSp) {
                chr.getOffenseManager().addOffense(
                        String.format("Character %d attempted assigning too many hyper stat levels. Available SP %d, " +
                                        "needed %d, current %d (%d, job %d)", chr.getId(), availableSp, neededSp,
                                        skill.getCurrentLevel(), skillId, chr.getJob()));
                chr.dispose();
                return;
            }
        } else { // not hyper stat and not hyper skill
            chr.getOffenseManager().addOffense(
                    String.format("Character %d attempted assigning hyper stat to an improper skill. (%d, job %d)",
                    chr.getId(), skillId, chr.getJob()));
            chr.dispose();
            return;
        }
        chr.removeFromBaseStatCache(skill);
        skill.setCurrentLevel(skill.getCurrentLevel() + 1);
        chr.addToBaseStatCache(skill);
        chr.addSkill(skill);
        chr.write(WvsContext.changeSkillRecordResult(skill));
        if (skillId == Job.HYPER_STAT_ARCANE_FORCE) {
            chr.getJobHandler().giveHyperAfBuff();
        }
    }

    @Handler(op = InHeader.USER_HYPER_STAT_SKILL_RESET_REQUEST)
    public static void handleUserHyperStatSkillResetRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        if (chr.getMoney() < GameConstants.HYPER_STAT_RESET_COST) {
            chr.chatMessage("Not enough money for this operation.");
            chr.write(WvsContext.receiveHyperStatSkillResetResult(chr.getId(), true, false));
        } else {
            chr.deductMoney(GameConstants.HYPER_STAT_RESET_COST);
            List<Skill> skills = new ArrayList<>();
            for (int skillID = 80000400; skillID <= 80000418; skillID++) {
                Skill skill = chr.getSkill(skillID);
                if (skill != null) {
                    skill.setCurrentLevel(0);
                    skills.add(skill);
                    chr.addSkill(skill);
                }
            }
            chr.write(WvsContext.changeSkillRecordResult(skills, true, false, false, false));
            chr.write(WvsContext.receiveHyperStatSkillResetResult(chr.getId(), true, true));
        }
    }
}
