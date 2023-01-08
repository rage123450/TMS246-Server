package net.swordie.ms.client.jobs.flora;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.*;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.*;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Randomizer;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * @author Sjonnie
 * Created on 6/25/2018.
 */
public class Adele extends Job {
    //0轉
    public static final int 再訪 = 150021000;
    public static final int 魔法迴路 = 150020079;
    public static final int 信念 = 150020006;//完成某任務會給

    //1轉
    public static final int 碎片 = 151001001;
    public static final int 懸浮 = 151001004;

    //2轉
    public static final int 乙太 = 151100017;
    public static final int 刺擊 = 151101000;
    public static final int 穿刺 = 151101001;
    public static final int 乙太結晶 = 151100002;
    public static final int 魔劍共鳴 = 151101003;
    public static final int 魔劍共鳴_1 = 151101004;
    public static final int 魔劍共鳴_2 = 151101010;
    public static final int 推進器 = 151101005;
    public static final int 創造 = 151101006;
    public static final int 創造_1 = 151101007;
    public static final int 創造_2 = 151101008;
    public static final int 創造_3 = 151101009;
    public static final int 奇蹟 = 151101013;

    //3轉
    public static final int 十字斬 = 151111000;
    public static final int 劍域 = 151111001;
    public static final int 回歸 = 151111002;
    public static final int 追蹤 = 151111003;
    public static final int 羽翼 = 151111004;
    public static final int 高潔精神 = 151111005;

    //4轉
    public static final int 高級乙太 = 151120012;
    public static final int 切割 = 151121000;
    public static final int 死亡標記 = 151121001;
    public static final int 踐踏 = 151121002;
    public static final int 綻放 = 151121003;
    public static final int 護堤 = 151121004;
    public static final int 雷普的勇士 = 151121005;
    public static final int 雷普勇士的意志 = 151121006;

    // Hyper Skills
    public static final int 狂風 = 151121040;
    public static final int 魔力爆裂 = 151121041;
    public static final int 神之種族 = 151121042;
    public static final int 魔劍共鳴_額外治癒 = 151120034;

    // V skills
    public static final int 無限 = 400011108;
    public static final int 復原 = 400011109;

    private int 乙太量;
    private int 乙太劍;
    public int 激活乙太劍;

    public final int max = chr.getSkillLevel(151120012) > 0 ? 400 : 300;
    private List<Summon> summonList = new ArrayList<>();


    private ScheduledFuture spectraEnergyTimer;
    private ScheduledFuture MagicSwordTimer;
    List<CharacterTemporaryStat> spellCasts = Arrays.asList(AbyssalCast, GustCast, ScarletCast, BasicCast);
    private int[] addedSkills = new int[]{
            再訪,魔法迴路,懸浮
    };

    public Adele(Char chr) {
        super(chr);
        if (chr != null && chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
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
                spectraEnergyTimer = EventManager.addEvent((Runnable) this::自動獲得乙太, 10, TimeUnit.SECONDS);//好像是接受到958後自動發不用寫這個
            }

        }
    }
    private void 自動獲得乙太() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        乙太量(5);
        spectraEnergyTimer = EventManager.addEvent((Runnable) this::自動獲得乙太, 10, TimeUnit.SECONDS);
    }
    private void 乙太量(int amount) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (乙太量 < max) {
            乙太量 = tsm.getOption(CharacterTemporaryStat.乙太).nOption;
            乙太量 += amount;
            乙太量 = Math.min(max, 乙太量);
            更新乙太量();
        }
    }

    private void 更新乙太量() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.nOption = 乙太量;
        tsm.putCharacterStatValue(CharacterTemporaryStat.乙太, o);
        tsm.sendSetStatPacket();
    }

    private void 乙太處理器(Char chr, int gain, final int skillid, final boolean refresh) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        if (refresh) {
            if (乙太劍 > 0 && 乙太劍 <= 2) {
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 2));
            } else if (乙太劍 > 0 && 乙太劍 <= 4) {
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 2));
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 4));
            } else if (乙太劍 > 0 && 乙太劍 <= 6) {
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 2));
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 4));
                chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 6));
            }
        } else {
            if (chr.getSkillLevel(151100017) > 0) {
                if ((乙太量 >= 100 && 乙太劍 < 2) || (乙太量 >= 200 && 乙太劍 < 4) || (乙太量 >= 300 && 乙太劍 < 6)) {
                    乙太劍 += 2;
                    if (chr.getJob() != 15112 && 乙太劍 > 4) {
                        乙太劍 = 4;
                    }
                    if (tsm.hasStat(CharacterTemporaryStat.創造)) {
                        if (乙太劍 == 1 || 乙太劍 < 0) {
                            乙太劍 = 2;
                            chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 2));
                        } else {
                            chr.getField().broadcastPacket(SkillPacket.CreateSwordReadyObtacle(chr, 15112, 乙太劍));
                        }
                    }
                }
                乙太量(gain);
                更新乙太量();
            }
        }
    }

    private void 乙太結晶(Char chr, Position pos, boolean nocooltime) {
        Field field = chr.getField();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(乙太結晶);
        SkillInfo si = SkillData.getSkillInfoById(乙太結晶);
        int slv = (byte) skill.getCurrentLevel();
        int size = 0;
        boolean spawn = true;
        for (Summon summon : new ArrayList<Summon>(chr.getSummons())) {
            if (summon.getSkillID() == 乙太結晶 && summon.getPosition().getX() - 350 <= pos.getX() && summon.getPosition().getX() + 350 >= pos.getX() && summon.getPosition().getY() - 70 <= pos.getY() && summon.getPosition().getY() + 70 >= pos.getY()) {
                spawn = false;
            }
        }
        if ((chr.getSkillCustomValue(乙太結晶) == null)) {
            if (!nocooltime) {
                chr.setSkillCustomInfo(乙太結晶, 0L, 0L);
            }
        }
        if (!nocooltime) {
            chr.setSkillCustomInfo(乙太結晶, 0L, 4000L);
        }
        if (chr.getSkillCustomValues().get(乙太結晶).canCancel(System.currentTimeMillis()) || nocooltime && spawn) {
            for (Summon summon : new ArrayList<Summon>(chr.getSummons())) {
                if (summon.getSkillID() == 乙太結晶) {
                    ++size;
                }
                if (size > 7) {
                    chr.getField().removeLife(summon);
                    //summon.broadcastLeavePacket();
                    //chr.getField().broadcastPacket(Summoned.summonedRemoved(summon, LeaveType.ANIMATION));
                }
            }
            Summon summon = Summon.getSummonByNoCTS(chr, 乙太結晶, slv);;
            summon.setPosition(pos);
            summon.setMoveAction((byte) 4);
            summon.setCurFoothold((short) 0);
            summon.setMoveAbility(MoveAbility.Stop);
            summon.setAssistType(AssistType.None);
            summon.setEnterType(EnterType.NoAnimation);
            summon.setFlyMob(false);
            summon.setBeforeFirstAttack(true);
            summon.setAttackActive(true);
            summon.setSummonTerm(30);
            summonList.add(summon);

            if (summonList.size() > 7) {
                chr.getField().removeLife(summonList.get(0));
                summonList.remove(0);
            }

            field.spawnAddSummon(summon);
            if (!nocooltime) {
                chr.setSkillCustomInfo(乙太結晶, 0L, 4000L);
            }
        }
    }

    private boolean is觸發技能(int skillid){
        switch(skillid) {
            case 刺擊:
            case 十字斬:
            case 切割:
            case 踐踏:
                return true;
            default:
                return false;
        }
    }

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
        Summon summon = chr.getField().getSummonByChrAndSkillId(chr, 乙太結晶);
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
        Field field = chr.getField();
        if (chr.getSkillCustomValue(奇蹟) == null) {
            chr.setSkillCustomInfo(奇蹟, 0L,8000L);
        }
        if (tsm.hasStat(CharacterTemporaryStat.奇蹟) && hasHitMobs && is觸發技能(attackInfo.skillId) && chr.getSkillCustomValues().get(奇蹟).canCancel(System.currentTimeMillis())) {
            chr.addStat(Stat.mp, -chr.getTemporaryStatManager().getOption(CharacterTemporaryStat.奇蹟).yOption);
            chr.getField().broadcastPacket(SkillPacket.CreateSubObtacle(chr, 碎片));
            chr.setSkillCustomInfo(奇蹟, 0L,8000L);
        }
        if (chr.getSkillLevel(乙太) > 0 && hasHitMobs && is觸發技能(attackInfo.skillId) ) {
            乙太處理器(chr, 12, skillID, false);
        }
        if (chr.getSkillCustomValue(創造) == null) {
            if (chr.getJob() == 15112) {
                chr.setSkillCustomInfo(創造, 0L, 1500L);
            } else if (chr.getJob() == 15111) {
                chr.setSkillCustomInfo(創造, 0L, 5500L);
            } else {
                chr.setSkillCustomInfo(創造, 0L, 9500L);
            }
        }
        if (tsm.hasStat(CharacterTemporaryStat.創造) && 乙太劍 > 0 && hasHitMobs && is觸發技能(attackInfo.skillId) && chr.getSkillCustomValues().get(創造).canCancel(System.currentTimeMillis())) {
            for (int i = 1; i <= 乙太劍; ++i) {
                chr.getField().broadcastPacket(SkillPacket.AutoAttackObtacleSword(chr, i * 10 , i == 1 ? 乙太劍 : 0));
            }
            if (chr.getJob() == 15112) {
                chr.setSkillCustomInfo(創造, 0L, 1500L);
            } else if (chr.getJob() == 15111) {
                chr.setSkillCustomInfo(創造, 0L, 5500L);
            } else {
                chr.setSkillCustomInfo(創造, 0L, 9500L);
            }
        }
        if (summon != null && attackInfo != null) {
            switch (attackInfo.skillId) {
                case 回歸: {
                    if (!Randomizer.isSuccess(40) || !hasHitMobs) {
                        break;
                    }
                    乙太結晶(chr, summon.getPosition(), false);
                    break;
                }
                case 追蹤: {
                    if (!Randomizer.isSuccess(15) || !hasHitMobs) {
                        break;
                    }
                    乙太結晶(chr, summon.getPosition(), false);
                    break;
                }
                case 無限: {
                    if (!Randomizer.isSuccess(5) || !hasHitMobs) {
                        break;
                    }
                    乙太結晶(chr, summon.getPosition(), false);
                    break;
                }
                case 綻放: {
                    if (!Randomizer.isSuccess(50) || !hasHitMobs) {
                        break;
                    }
                    乙太結晶(chr, summon.getPosition(), false);
                    break;
                }
                case 創造_1:
                case 創造_2:
                case 創造_3: {
                    if (!Randomizer.isSuccess(30) || !hasHitMobs) {
                        break;
                    }
                    乙太結晶(chr, summon.getPosition(), false);
                    break;
                }
            }
        }
        switch (attackInfo.skillId) {
            case 魔劍共鳴:
            case 魔劍共鳴_1:
                si = SkillData.getSkillInfoById(魔劍共鳴_2);
                int amount = 1;
                if (tsm.hasStat(CharacterTemporaryStat.魔劍共鳴)) {
                    amount = tsm.getOption(CharacterTemporaryStat.魔劍共鳴).xOption;
                    if (amount < 3) {
                        amount++;
                    }
                }
                o1.nValue = si.getValue(SkillStat.z, slv) * amount;
                o1.nReason = 魔劍共鳴_2;
                o1.tTerm = si.getValue(SkillStat.time, slv);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieIgnoreMobpdpR, o1);
                o2.nOption = amount; //idk
                o2.xOption = amount;
                o2.yOption = si.getValue(SkillStat.y, slv) * amount;
                o2.rOption = 魔劍共鳴_2;
                o2.tOption = si.getValue(SkillStat.time, slv) * 2;
                tsm.putCharacterStatValue(CharacterTemporaryStat.魔劍共鳴, o2);
                tsm.sendSetStatPacket();
                break;
            case 死亡標記:
                if (hasHitMobs) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.SecondAtomLockOn);
                        tsb.setNOption(1);
                        tsb.setROption(skillID);
                        tsb.setXOption(mob.getObjectId());
                        tsb.setYOption(skillID);
                        tsb.setExpireTerm(1080);
                        tsm.putCharacterStatValue(CharacterTemporaryStat.死亡標記, tsb.getOption());
                        tsm.sendSetStatPacket();
                    }
                    /*
                    chr.setSkillCustomInfo(死亡標記, summon.getObjectId(), 0L);
                    if (chr.getSkillCustomValue0(死亡標記) != (long) summon.getObjectId()) {
                        chr.removeSkillCustomInfo(死亡標記);
                    }

                     */
                }
                break;
            case 狂風:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(SkillStat.time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
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
            case 碎片:
                chr.write(SkillPacket.CreateSubObtacle(chr , skillID));
                break;
            case 乙太結晶:
                Position pos = inPacket.decodePosition();
                乙太結晶(chr, pos, false);
                if (chr.getSkillLevel(魔劍共鳴_額外治癒) <= 0) {
                    乙太處理器(chr, -15, skillID, false);
                }
                break;
            case 雷普的勇士:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                tsm.sendSetStatPacket();
                break;
            case 雷普勇士的意志:
                tsm.removeAllDebuffs();
                tsm.sendSetStatPacket();
                break;
            case 懸浮:
                o1.nReason = skillID;
                o1.nValue = 50;
                o1.tTerm = si.getValue(SkillStat.time, slv); // 1900/1000
                tsm.putCharacterStatValue(IndieFloating, o1);
                o2.nOption = 50;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) / 1000;
                tsm.putCharacterStatValue(NewFlying, o2);
                tsm.sendSetStatPacket();
                break;
            case 創造:
                if (tsm.hasStat(CharacterTemporaryStat.創造)) {
                    tsm.removeStat(CharacterTemporaryStat.創造, true);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(w, slv);
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(CharacterTemporaryStat.創造, o1);
                    tsm.sendSetStatPacket();
                }
                break;
            case 奇蹟:
                if (tsm.hasStat(CharacterTemporaryStat.奇蹟)) {
                    tsm.removeStat(CharacterTemporaryStat.奇蹟, true);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;//unk
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(CharacterTemporaryStat.奇蹟, o1);
                    tsm.sendSetStatPacket();
                }
                break;
            case 推進器:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                tsm.sendSetStatPacket();
                break;
            case 魔劍共鳴:
                /*
                final int objectId = inPacket.decodeInt();
                final Summon summon = chr.getField().getSummonByChrAndSkillId(chr, objectId);
                summon.getMobTemplateId();
                if (summon != null) {
                    summon.broadcastLeavePacket();
                }
                */
                chr.getField().removeLife(skillUseInfo.objectId, true);
                if (chr.getSkillLevel(151120034) > 0) {
                    乙太處理器(chr, 20, skillID, false);
                    break;
                }
                break;
            case 神之種族:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tTerm = si.getValue(SkillStat.time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                tsm.sendSetStatPacket();
                break;
            case 高潔精神:
                o1.rOption = skillID;
                o1.nOption = 10; //Sniff
                o1.tOption = si.getValue(SkillStat.time, slv);
                o1.xOption = si.getValue(SkillStat.x, slv);
                o1.yOption = si.getValue(SkillStat.y, slv);
                tsm.putCharacterStatValue(CharacterTemporaryStat.高潔精神, o1);
                tsm.sendSetStatPacket();
                break;
            case 追蹤:
                chr.getField().broadcastPacket(SkillPacket.RemoveSubObtacle(chr, ((乙太劍 - 1) * 10)));
                chr.getField().broadcastPacket(SkillPacket.RemoveSubObtacle(chr, (乙太劍 * 10)));
                乙太劍 -= 2;
                if (乙太劍 <= 0) {
                    乙太劍 = 0;
                }
                if (激活乙太劍 <= 0) {
                    激活乙太劍 = 0;
                }
                乙太處理器(chr, -100, skillID, false);
                ++激活乙太劍;
                MagicSword magicSword = MagicSword.getMagicSwordBy(chr, skillID, 激活乙太劍, 40000, false);
                chr.getField().spawnMagicSword(chr, magicSword, false, this);
                ++激活乙太劍;
                MagicSword magicSword2 = MagicSword.getMagicSwordBy(chr, skillID, 激活乙太劍, 40000, false);
                chr.getField().spawnMagicSword(chr, magicSword2, false, this);
                break;
            case 羽翼:
                乙太處理器(chr, -30, skillID, false);
                break;
            case 復原:
                o1.nReason = skillID;
                o1.nValue = si.getValue(SkillStat.indieDamR, slv);
                o1.tTerm = si.getValue(SkillStat.time, slv);
                tsm.putCharacterStatValue(CharacterTemporaryStat.IndieDamR, o1);
                o2.rOption = skillID;
                o2.nOption = 1;
                o2.tOption = si.getValue(SkillStat.time, slv);
                tsm.putCharacterStatValue(CharacterTemporaryStat.高潔精神, o1);
                tsm.sendSetStatPacket();
                break;
            case 魔力爆裂://inpacket 264 outpacket 1449*7
                if (skillUseInfo.spawnCrystals) break;
                List<Rect> shardRects = new ArrayList<>();
                for (Tuple<Integer, Position> shard : skillUseInfo.shardsPositions) {
                    Position shardPosition = shard.getRight();
                    Rect shardRect = shardPosition.getRectAround(si.getLastRect());
                    shardRects.add(shardRect);
                    AffectedArea aa = AffectedArea.getPassiveAA(chr, 魔力爆裂, slv);
                    aa.setSkillID(魔力爆裂);
                    aa.setPosition(shard.getRight());
                    aa.setDelay((short) 3);
                    aa.setDuration(1590);
                    aa.setRect(shard.getRight().getRectAround(si.getLastRect()));
                    aa.setHitMob(shard.getLeft() != 0);
                    chr.getField().spawnAffectedArea(aa);
                }
                chr.write(UserLocal.adeleShardBreakerResult(魔力爆裂, shardRects));
                break;
        }
        if (tsm.hasStat(CharacterTemporaryStat.創造)){
            乙太處理器(chr, 0, 0, true);
        }
    }

    public void handleKeyDownSkill(Char chr, int skillID, InPacket inPacket) {
        super.handleKeyDownSkill(chr, skillID, inPacket);

        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        if (skillID == 護堤) { // TODO 還有3個Mask要發未完成
            o1.nOption = 1;
            o1.rOption = 護堤;
            o1.tOption = 8;
            tsm.putCharacterStatValue(DreamDowon, o1);
            o2.nValue = 40;
            o2.nReason = chr.getJob();
            o2.tOption = 8;
            tsm.putCharacterStatValue(IndieDamReduceR, o2);
            tsm.sendSetStatPacket();
        }
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
        handleJobAdvance(JobConstants.JobEnum.ADELE_1.getJobId());
        chr.addSpToJobByCurrentJob(3);
        handleJobEnd();
    }

    @Override
    public void handleJobEnd() {
        super.handleJobEnd();
        //chr.forceUpdateSecondary(null, ItemData.getItemDeepCopy(1353600)); // Initial Path (2ndary)
        Item secondary = ItemData.getItemDeepCopy(1354000); // unk
        if (secondary != null) {
            chr.addItemToInventory(secondary);
        }

    }
}
