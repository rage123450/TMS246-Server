package net.swordie.ms.connection.packet;

import jnr.ffi.annotations.Out;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.LinkSkill;
import net.swordie.ms.client.alliance.AllianceResult;
import net.swordie.ms.client.character.*;
import net.swordie.ms.client.character.cards.CharacterCard;
import net.swordie.ms.client.character.info.ExpIncreaseInfo;
import net.swordie.ms.client.character.info.ZeroInfo;
import net.swordie.ms.client.character.items.Equip;
import net.swordie.ms.client.character.items.HotTimeReward;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.items.MemorialCubeInfo;
import net.swordie.ms.client.character.potential.CharacterPotential;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.skills.MatrixRecord;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.TownPortal;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.character.union.UnionBoard;
import net.swordie.ms.client.friend.result.FriendResult;
import net.swordie.ms.client.guild.Guild;
import net.swordie.ms.client.guild.bbs.GuildBBSPacket;
import net.swordie.ms.client.guild.result.GuildResult;
import net.swordie.ms.client.jobs.resistance.WildHunterInfo;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.client.party.PartyResult;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.loaders.ChatEmoticonData;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.util.AntiMacro;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.Position;
import net.swordie.ms.world.auction.AuctionItem;
import org.apache.log4j.LogManager;
import org.python.antlr.op.Eq;

import java.util.*;
import java.util.stream.Collectors;

import static net.swordie.ms.enums.InvType.*;
import static net.swordie.ms.enums.MessageType.*;

/**
 * Created on 12/22/2017.
 */
public class WvsContext {
    private static final org.apache.log4j.Logger log = LogManager.getRootLogger();


    public static OutPacket exclRequest() {
        return new OutPacket(OutHeader.EXCL_REQUEST);
    }

    public static OutPacket statChangedEmpty() {
        Map<Stat, Object> stats = new HashMap<>();
        return statChanged(stats, 1, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0, (short) 0);
    }

    public static OutPacket statChanged(Map<Stat, Object> stats) {
        return statChanged(stats, 0);
    }

    public static OutPacket statChanged(Map<Stat, Object> stats, byte exclRequest) {
        return statChanged(stats, exclRequest, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0, (short) 0);
    }

    public static OutPacket statChanged(Map<Stat, Object> stats, int subJob) {
        return statChanged(stats, 0, (byte) -1, (byte) 0, (byte) 0, (byte) 0, false, 0, 0, (short) subJob);
    }

    public static OutPacket statChanged(Map<Stat, Object> stats, int exclRequestSent, byte mixBaseHairColor,
                                        byte mixAddHairColor, byte mixHairBaseProb, byte charmOld, boolean updateCovery,
                                        int hpRecovery, int mpRecovery, short subJob) {
        OutPacket outPacket = new OutPacket(OutHeader.STAT_CHANGED);

        outPacket.encodeByte(exclRequestSent);
        outPacket.encodeByte(false); // isPet
        outPacket.encodeByte(false); // ?
        // GW_CharacterStat::DecodeChangeStat
        int mask = 0;
        for (Stat stat : stats.keySet()) {
            mask |= stat.getVal();
        }
        outPacket.encodeLong(mask);
        Comparator statComper = Comparator.comparingLong(o -> ((Stat) o).getVal());
        TreeMap<Stat, Object> sortedStats = new TreeMap<>(statComper);
        sortedStats.putAll(stats);
        for (Map.Entry<Stat, Object> entry : sortedStats.entrySet()) {
            Stat stat = entry.getKey();
            Object value = entry.getValue();
            switch (stat) {
                case skin://
                    outPacket.encodeByte((Byte) value);
                    break;
                case face://
                case hair://
                case hp://
                case mhp://
                case mp://
                case mmp://
                case pop://
                case charismaEXP://
                case insightEXP://
                case willEXP://
                case craftEXP://
                case senseEXP://
                case charmEXP://
                case eventPoints://
                case level://
                    outPacket.encodeInt((Integer) value);
                    break;
                case str://
                case dex://
                case inte://
                case luk://
                case ap://
                case fatigue:
                    outPacket.encodeShort((Short) value);
                    break;
                case sp://
                    if (value instanceof ExtendSP) {
                        ((ExtendSP) value).encode(outPacket);
                    } else {
                        outPacket.encodeShort((Short) value);
                    }
                    break;
                case exp://
                case money://
                    outPacket.encodeLong((Long) value);
                    break;
                case dayLimit://
                    ((NonCombatStatDayLimit) value).encode(outPacket);
                    break;
                case albaActivity://
                    //TODO
                    break;
                case characterCard://248 + 腳色卡改掉了
                    ((CharacterCard) value).encode(outPacket);
                    break;
                case pvp2://??
                    outPacket.encodeByte((Byte) value);
                    outPacket.encodeByte((Byte) value);
                    break;
                case job://
                    outPacket.encodeShort((Short) value);
                    outPacket.encodeShort(subJob);
            }
        }

        outPacket.encodeByte(mixBaseHairColor);
        outPacket.encodeByte(mixAddHairColor);
        outPacket.encodeByte(mixHairBaseProb);
        outPacket.encodeByte(charmOld > 0);
        if (charmOld > 0) {
            outPacket.encodeByte(charmOld);
        }
        outPacket.encodeByte(updateCovery);
        if (updateCovery) {
            outPacket.encodeInt(hpRecovery);
            outPacket.encodeInt(mpRecovery);
        }
        return outPacket;
    }


    public static OutPacket inventoryOperation(boolean exclRequest, boolean notRemoveAddInfo, InventoryOperation type, short oldPos, short newPos,
                                               int bagPos, Item item) {
        // ignoreExcl if a pet picks up an item
        List<ItemOperation> operations = new ArrayList<>();

        ItemOperation io = new ItemOperation();
        io.item = item;
        io.pos = oldPos;
        io.newPos = newPos;
        io.type = type;
        operations.add(io);
        return inventoryOperation(exclRequest ? 1 : 0, notRemoveAddInfo, operations);
    }

    public static OutPacket inventoryOperation(int exclResult, boolean notAddRemoveInfo, List<ItemOperation> itemOperations) {
        OutPacket outPacket = new OutPacket(OutHeader.INVENTORY_OPERATION);

        outPacket.encodeByte(exclResult);
        outPacket.encodeInt(itemOperations.size());
        outPacket.encodeByte(notAddRemoveInfo);

        byte addMovementInfo = 0;
        for (ItemOperation itemOperation : itemOperations) {
            Item item = itemOperation.item;
            // logic like this in packets :(
            InvType invType = item.getInvType();
            if ((itemOperation.pos > 0 && itemOperation.newPos < 0 && invType == EQUIPPED) || (invType == EQUIPPED && itemOperation.pos < 0)) {
                invType = item.isCash() ? CASH_EQUIP : InvType.EQUIP;
            }
            boolean isEquippedItem = (invType == EQUIPPED ||
                    ((invType == CASH_EQUIP || invType == EQUIP) && (itemOperation.pos < 0 || itemOperation.newPos < 0)));
            outPacket.encodeByte(itemOperation.type.getVal());
            outPacket.encodeByte(invType.getVal());
            outPacket.encodeShort(itemOperation.pos);

            switch (itemOperation.type) {
                case Add:
                    outPacket.encode(item);
                    break;
                case UpdateQuantity:
                    outPacket.encodeShort(item.getQuantity());
                    break;
                case Move:
                    outPacket.encodeShort(itemOperation.newPos);
                    if (isEquippedItem) {
                        addMovementInfo = 1;
                    }
                    break;
                case Remove:
                    if (isEquippedItem) {
                        addMovementInfo = 2;
                    }
                    break;
                case ItemExp:
                    outPacket.encodeLong(((Equip) item).getExp());
                    break;
                case UpdateBagPos:
                    outPacket.encodeInt(itemOperation.newPos);
                    break;
                case UpdateBagQuantity:
                    outPacket.encodeShort(item.getQuantity());
                    break;
                case BagRemove:
                    break;
                case BagToBag:
                    outPacket.encodeShort(item.getQuantity());
                    break;
                case BagNewItem:
                    outPacket.encode(item);
                    break;
                case BagRemoveSlot:
                    break;
            }
            boolean isSymbol = ItemConstants.isArcaneSymbol(item.getItemId());
            outPacket.encodeByte(isSymbol);
            if (isSymbol) {
                ((Equip) item).encodeSymbolData(outPacket);
            }
        }

        if (addMovementInfo != 0) {
            outPacket.encodeByte(addMovementInfo);
        }

        return outPacket;
    }
    @Deprecated//增加物品格子
    public static OutPacket getSlotUpdate(byte invType, byte newSlots) {
        OutPacket outPacket = new OutPacket(OutHeader.INVENTORY_GROW);
        outPacket.encodeByte(invType);
        outPacket.encodeByte(newSlots);
        return outPacket;
    }

    public static OutPacket onMesoPickupResult(int meso) {
        OutPacket outPacket = new OutPacket(OutHeader.MOB_DROP_MESO_PICKUP);
        outPacket.encodeInt(meso);
        return outPacket;
    }

    public static OutPacket updateEventNameTag(int[] tags) {
        OutPacket outPacket = new OutPacket(OutHeader.EVENT_NAME_TAG);

        for (int i = 0; i < 5; i++) {
            outPacket.encodeString("");
            if (i >= tags.length) {
                outPacket.encodeByte(-1);
            } else {
                outPacket.encodeByte(tags[i]);
            }
        }

        return outPacket;
    }

    public static OutPacket changeSkillRecordResult(Skill skill) {
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);
        return changeSkillRecordResult(skills, true, false, false, false);
    }

    public static OutPacket changeSkillRecordResult(List<Skill> skills, boolean exclRequestSent, boolean showResult,
                                                    boolean removeLinkSkill, boolean sn) {
        OutPacket outPacket = new OutPacket(OutHeader.CHANGE_SKILL_RECORD_RESULT);

        outPacket.encodeByte(exclRequestSent);
        outPacket.encodeByte(showResult);
        outPacket.encodeByte(removeLinkSkill);
        outPacket.encodeShort(skills.size());
        for (Skill skill : skills) {
            outPacket.encodeInt(skill.getSkillId());
            outPacket.encodeInt(skill.getCurrentLevel());
            outPacket.encodeInt(skill.getMasterLevel());
            outPacket.encodeFT(FileTime.fromType(FileTime.Type.PLAIN_ZERO));
        }
        outPacket.encodeByte(sn);

        return outPacket;
    }

    public static OutPacket temporaryStatSet(TemporaryStatManager tsm) {
        OutPacket outPacket = new OutPacket(OutHeader.TEMPORARY_STAT_SET);

        boolean hasMovingAffectingStat = tsm.hasNewMovingEffectingStat(); // encoding flushes new stats
        tsm.encodeForLocal(outPacket);

        outPacket.encodeShort(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(hasMovingAffectingStat);

        if (hasMovingAffectingStat) {
            outPacket.encodeByte(0);
        }
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);

        outPacket.encodeArr(new byte[10]);


        return outPacket;
    }

    public static OutPacket temporaryStatReset(TemporaryStatManager temporaryStatManager, boolean demount) {
        OutPacket outPacket = new OutPacket(OutHeader.TEMPORARY_STAT_RESET);

        outPacket.encodeByte(0); // ?
        outPacket.encodeByte(0); // ?
        outPacket.encodeByte(demount);

        for (int i : temporaryStatManager.getRemovedMask()) {
            outPacket.encodeInt(i);
        }
//        temporaryStatManager.getRemovedStats().forEach((cts, option) -> outPacket.encodeInt(0));
        temporaryStatManager.encodeRemovedIndieTempStat(outPacket);

        if (temporaryStatManager.hasRemovedMovingEffectingStat()) {
            outPacket.encodeByte(0);
        }
        temporaryStatManager.getRemovedStats().clear();

        outPacket.encodeArr(new byte[10]);

        return outPacket;
    }

    public static OutPacket skillUseResult(boolean stillGoing) {
        OutPacket outPacket = new OutPacket(OutHeader.SKILL_USE_RESULT);
        // 2221011 - Frozen Breath
        outPacket.encodeByte(stillGoing);

        return outPacket;
    }

    public static OutPacket initMapleAchievement() {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(ACHIEVEMENT_INIT.getVal());
        // No clue about the packet structure or the values,
        // so it's just a paste of a sniff for now.
        int size = 1;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeLong(1255); // id?
            outPacket.encodeInt(1);
            outPacket.encodeByte(-1);
            outPacket.encodeByte(2);
            outPacket.encodeLong(-71937691);
            outPacket.encodeString("");
        }

        return outPacket;
    }

    public static OutPacket characterModified(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.CHARACTER_MODIFIED);

        chr.encode(outPacket, DBChar.CoupleRecord); // <<<<------------------------------------

        return outPacket;
    }

    public static OutPacket dropPickupMessage(int money, short smallChangeExtra) {
        return dropPickupMessage(money, 0, (byte) 1, smallChangeExtra, (short) 0);
    }

    public static OutPacket dropPickupMessage(Item item, short quantity) {
        return dropPickupMessage(item.getItemId(), 0, (byte) 0, (short) 0, quantity);
    }

    public static OutPacket dropPickupMessage(int i, int delay, byte type, short smallChangeExtra, short quantity) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(DROP_PICKUP_MESSAGE.getVal());
        outPacket.encodeInt(delay);
        outPacket.encodeByte(0); // new 200
        outPacket.encodeByte(type);
        // also error (?) codes -2, ,-3, -4, -5, <default>
        switch (type) {
            case 1: // Mesos
                outPacket.encodeByte(false); // boolean: portion was lost after falling to the ground
                outPacket.encodeInt(i); // Mesos
                outPacket.encodeShort(smallChangeExtra); // Spotting small change
                break;
            case 0: // item
                outPacket.encodeInt(i);
                outPacket.encodeInt(quantity); // ?
                outPacket.encodeByte(0);
                break;
            case 2: // ?
                outPacket.encodeInt(100);
                outPacket.encodeLong(0);
                break;
        }

        return outPacket;
    }

    public static OutPacket questRecordMessageAddValidCheck(int qrKey, byte state) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(QUEST_RECORD_MESSAGE_ADD_VALID_CHECK.getVal());
        outPacket.encodeInt(qrKey);
        outPacket.encodeByte(true);
        outPacket.encodeByte(state);
        // TODO probably missing something here

        return outPacket;
    }

    public static OutPacket questRecordMessage(Quest quest) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(QUEST_RECORD_MESSAGE.getVal());
        outPacket.encodeInt(quest.getQRKey());
        QuestStatus state = quest.getStatus();
        outPacket.encodeByte(state.getVal());

        switch (state) {
            case NotStarted:
                outPacket.encodeByte(0); // If quest is completed, but should never be true?
                break;
            case Started:
                outPacket.encodeString(quest.getQRValue());
                break;
            case Completed:
                outPacket.encodeFT(quest.getCompletedTime());
                break;
        }

        return outPacket;
    }

    public static OutPacket questRecordExMessage(Quest quest) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(QUEST_RECORD_EX_MESSAGE.getVal());
        outPacket.encodeInt(quest.getQRKey());
        outPacket.encodeString(quest.getQRValue());

        return outPacket;
    }

    public static OutPacket questWorldShareMessage(Quest quest) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(WORLD_SHARE_RECORD_MESSAGE.getVal());
        outPacket.encodeInt(quest.getQRKey());
        outPacket.encodeString(quest.getQRValue());

        return outPacket;
    }

    public static OutPacket incExpMessage(ExpIncreaseInfo eii) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(INC_EXP_MESSAGE.getVal());
        eii.encode(outPacket);

        return outPacket;
    }

    public static OutPacket incSpMessage(short job, byte amount) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(INC_SP_MESSAGE.getVal());
        outPacket.encodeShort(job);
        outPacket.encodeByte(amount);

        return outPacket;
    }

    public static OutPacket incMoneyMessage(int amount) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(INC_MONEY_MESSAGE.getVal());
        outPacket.encodeInt(amount);
        outPacket.encodeInt(amount > 0 ? 1 : -1);

        return outPacket;
    }

    /**
     * Returns a packetfor messages with the following {@link MessageType}:<br>
     * GENERAL_ITEM_EXPIRE_MESSAGE<br>
     * ITEM_PROTECT_EXPIRE_MESSAGE<br>
     * ITEM_ABILITY_TIME_LIMITED_EXPIRE_MESSAGE<br>
     * SKILL_EXPIRE_MESSAGE
     *
     * @param mt    The message type.
     * @param items The list of ints that should be encoded.
     * @return The message OutPacket.
     */
    public static OutPacket message(MessageType mt, List<Integer> items) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(mt.getVal());
        switch (mt) {
            case GENERAL_ITEM_EXPIRE_MESSAGE:
            case ITEM_PROTECT_EXPIRE_MESSAGE:
            case ITEM_ABILITY_TIME_LIMITED_EXPIRE_MESSAGE:
            case SKILL_EXPIRE_MESSAGE:
                outPacket.encodeByte(items.size());
                items.forEach(outPacket::encodeInt);
                break;
        }
        return outPacket;
    }

    public static OutPacket itemExpireReplaceMessage(List<String> strings) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(ITEM_EXPIRE_REPLACE_MESSAGE.getVal());
        outPacket.encodeByte(strings.size());
        strings.forEach(outPacket::encodeString);

        return outPacket;
    }

    public static OutPacket incNonCombatStatEXPMessage(Stat trait, int amount) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(INC_NON_COMBAT_STAT_EXP_MESSAGE.getVal());
        long mask = 0;
        mask |= trait.getVal();
        outPacket.encodeLong(mask);
        outPacket.encodeInt(amount);

        return outPacket;
    }

    /**
     * Returns a packetfor messages with the following {@link MessageType}:<br>
     * int: <br>
     * CASH_ITEM_EXPIRE_MESSAGE<br>
     * INC_POP_MESSAGE<br>
     * INC_GP_MESSAGE<br>
     * GIVE_BUFF_MESSAGE<br><br>
     * int + byte: <br>
     * INC_COMMITMENT_MESSAGE<br><br>
     * String: <br>
     * SYSTEM_MESSAGE<br><br>
     * int + String: <br>
     * QUEST_RECORD_EX_MESSAGE<br>
     * WORLD_SHARE_RECORD_MESSAGE<br>
     *
     * @param mt     The message type.
     * @param i      The integer to encode.
     * @param string The String to encode.
     * @param type   The type (byte) to encode.
     * @return The message OutPacket.
     */
    public static OutPacket message(MessageType mt, int i, String string, byte type) {
        OutPacket outPacket = new OutPacket(OutHeader.MESSAGE);

        outPacket.encodeByte(mt.getVal());
        switch (mt) {
            case CASH_ITEM_EXPIRE_MESSAGE:
            case INC_POP_MESSAGE:
            case INC_GP_MESSAGE:
            case GIVE_BUFF_MESSAGE:
                outPacket.encodeInt(i);
                break;
            case INC_COMMITMENT_MESSAGE:
                outPacket.encodeInt(i);
                outPacket.encodeInt(i);
                outPacket.encodeInt(i < 0 ? 1 : i == 0 ? 2 : 0); // gained = 0, lost = 1, cap = 2
                break;
            case SYSTEM_MESSAGE:
                outPacket.encodeString(string);
                break;
            case QUEST_RECORD_EX_MESSAGE:
            case WORLD_SHARE_RECORD_MESSAGE:
            case COLLECTION_RECORD_MESSAGE:
                outPacket.encodeInt(i);
                outPacket.encodeString(string);
                break;
            case INC_HARDCORE_EXP_MESSAGE:
                outPacket.encodeInt(i); //You have gained x EXP
                outPacket.encodeInt(i); //Field Bonus Exp
                break;
            case BARRIER_EFFECT_IGNORE_MESSAGE:
                outPacket.encodeByte(type); //protection/shield scroll pop-up Message
                break;
        }

        return outPacket;
    }

    public static OutPacket flipTheCoinEnabled(byte enabled) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_FLIP_THE_COIN_ENABLED);

        outPacket.encodeByte(enabled);

        return outPacket;
    }

    public static OutPacket modComboResponse(int combo, boolean show) {
        OutPacket outPacket = new OutPacket(OutHeader.MOD_COMBO_RESPONSE);

        outPacket.encodeInt(combo);
        outPacket.encodeByte(!show);

        return outPacket;
    }

    public static OutPacket wildHunterInfo(WildHunterInfo whi) {
        OutPacket outPacket = new OutPacket(OutHeader.WILD_HUNTER_INFO);

        whi.encode(outPacket);

        return outPacket;
    }

    public static OutPacket zeroTag(Char chr, byte Gender,int nowhp, int maxhp) {
        OutPacket outPacket = new OutPacket(OutHeader.ZERO_TAG);
        int a = (chr.getSkillLevel(80000406) > 0) ? chr.getSkillLevel(80000406) : 0;
        outPacket.encodeShort(199);
        outPacket.encodeByte(Gender);
        outPacket.encodeInt(nowhp);
        outPacket.encodeLong(chr.getSkillCustomValue0(101000201));
        outPacket.encodeInt(maxhp);
        outPacket.encodeInt(((Gender == 1) ? 100 : 100) + a * 10 + ((Gender == 1 && chr.getSkillLevel(101100203) > 0) ? 30 : 0));

        return outPacket;
    }

    public static OutPacket zeroInfo(ZeroInfo currentInfo, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.ZERO_INFO);

        currentInfo.encode(outPacket, chr);

        return outPacket;
    }

    public static OutPacket useSkillBook(Char chr, int skillid, int maxlevel, boolean canuse, boolean success) {
        OutPacket outPacket = new OutPacket(OutHeader.SKILL_LEARN_ITEM_RESULT);
        outPacket.encodeByte(0);
        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(1);
        outPacket.encodeInt(skillid);
        outPacket.encodeInt(maxlevel);
        outPacket.encodeBoolean(canuse);
        outPacket.encodeBoolean(success);
        return outPacket;
    }

    public static OutPacket useAPSPReset(boolean reset, int cid) {
        OutPacket outPacket = new OutPacket(reset ? OutHeader.SKILL_RESET_ITEM_RESULT : OutHeader.ABILITY_RESET_ITEM_RESULT);
        outPacket.encodeByte(1);
        outPacket.encodeInt(cid);
        outPacket.encodeByte(1);
        return outPacket;
    }

    public static OutPacket gatherItemResult(byte type) {
        OutPacket outPacket = new OutPacket(OutHeader.GATHER_ITEM_RESULT);

        outPacket.encodeByte(true); // doesn't get used
        outPacket.encodeByte(type);

        return outPacket;
    }

    public static OutPacket sortItemResult(byte type) {
        OutPacket outPacket = new OutPacket(OutHeader.SORT_ITEM_RESULT);

        outPacket.encodeByte(true); // doesn't get used
        outPacket.encodeByte(type);

        return outPacket;
    }

    public static OutPacket partyResult(PartyResult pri) {
        OutPacket outPacket = new OutPacket(OutHeader.PARTY_RESULT);

        outPacket.encode(pri);

        return outPacket;
    }

    public static OutPacket partyMemberCandidateResult(Set<Char> chars) {
        OutPacket outPacket = new OutPacket(OutHeader.PARTY_MEMBER_CANDIDATE_RESULT);

        outPacket.encodeByte(chars.size());
        for (Char chr : chars) {
            outPacket.encodeInt(chr.getId());
            outPacket.encodeString(chr.getName());
            outPacket.encodeShort(chr.getJob());
            outPacket.encodeShort(chr.getAvatarData().getCharacterStat().getSubJob());
            outPacket.encodeInt(chr.getLevel());
            outPacket.encodeByte(0); // new 200
        }

        return outPacket;
    }

    public static OutPacket partyCandidateResult(Set<Party> parties) {
        OutPacket outPacket = new OutPacket(OutHeader.PARTY_CANDIDATE_RESULT);

        outPacket.encodeByte(parties.size());
        for (Party party : parties) {
            Char leader = party.getPartyLeader().getChr();
            outPacket.encodeInt(party.getId());
            outPacket.encodeString(leader.getName());
            outPacket.encodeInt(party.getAvgLevel());
            outPacket.encodeByte(party.getMembers().size());
            outPacket.encodeString(party.getName());
            outPacket.encodeByte(party.getMembers().size());
            for (PartyMember pm : party.getMembers()) {
                outPacket.encodeInt(pm.getCharID());
                outPacket.encodeString(pm.getCharName());
                outPacket.encodeShort(pm.getJob());
                outPacket.encodeShort(pm.getSubSob());
                outPacket.encodeInt(pm.getLevel());
                outPacket.encodeByte(pm.equals(party.getPartyLeader()));
                outPacket.encodeByte(0); // new 200
            }
        }

        return outPacket;
    }

    public static OutPacket guildResult(GuildResult gri) {
        OutPacket outPacket = new OutPacket(OutHeader.GUILD_RESULT);

        gri.encode(outPacket);

        return outPacket;
    }

    public static OutPacket guildSearchResult(Collection<Guild> guilds) {
        OutPacket outPacket = new OutPacket(OutHeader.GUILD_SEARCH_RESULT);

        outPacket.encodeShort(0);// mode
        outPacket.encodeString("");// text
        outPacket.encodeByte(0);// option
        outPacket.encodeBoolean(false);
        outPacket.encodeByte(1);
        outPacket.encodeBoolean(false);
        outPacket.encodeInt(0); //
        outPacket.encodeInt(guilds.size());
        for (Guild g : guilds) {
            outPacket.encodeInt(g.getId());
            outPacket.encodeByte(g.getLevel());
            outPacket.encodeString(g.getName());
            outPacket.encodeString(g.getGuildLeader().getName());
            outPacket.encodeShort(g.getMembers().size());
            outPacket.encodeShort(g.getAverageMemberLevel());
            outPacket.encodeByte((g.getRequestors() != null) ? 0 : 1);
            outPacket.encodeLong(0L);
            outPacket.encodeByte(1);
            outPacket.encodeString(g.getNotice());
            outPacket.encodeInt(135);
            outPacket.encodeInt(55);
            outPacket.encodeInt(14);
            outPacket.encodeByte(0);
        }

        return outPacket;
    }


    public static OutPacket allianceResult(AllianceResult ar) {
        OutPacket outPacket = new OutPacket(OutHeader.ALLIANCE_RESULT);

        outPacket.encode(ar);

        return outPacket;
    }

    public static OutPacket guildBBSResult(GuildBBSPacket gbp) {
        OutPacket outPacket = new OutPacket(OutHeader.GUILD_BBS_RESULT);

        outPacket.encode(gbp);

        return outPacket;
    }

    public static OutPacket flameWizardFlameWalkEffect(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.FLAME_WIZARD_FLAME_WALK_EFFECT);

        outPacket.encodeInt(chr.getId());

        return outPacket;
    }

    public static OutPacket flameWizardFlareBlink(Char chr, Position newPosition, boolean used) {
        OutPacket outPacket = new OutPacket(OutHeader.FLAME_WIZARD_FLARE_BLINK);

        outPacket.encodeInt(chr.getId()); //chr
        outPacket.encodeByte(used); //used

        if (used) {

            //Blink - Clear + Teleport
            chr.write(FieldPacket.teleport(newPosition, chr));

        } else {

            //Blink - Set Position
            outPacket.encodeByte(used);
            outPacket.encodeShort(1);
            outPacket.encodePosition(newPosition); //2x encode Short (x/y)
            outPacket.encodePosition(new Position()); //2x encode Short (x/y)
        }

        return outPacket;
    }

    public static OutPacket friendResult(FriendResult friendResult) {
        OutPacket outPacket = new OutPacket(OutHeader.FRIEND_RESULT);

        friendResult.encode(outPacket);

        return outPacket;
    }


    // removed v188?
//    public static OutPacket loadAccountIDOfCharacterFriendResult(Set<Friend> friends) {
//        OutPacket outPacket = new OutPacket(OutHeader.LOAD_ACCOUNT_ID_OF_CHARACTER_FRIEND_RESULT);
//
//        outPacket.encodeInt(friends.size());
//        for(Friend fr : friends) {
//            outPacket.encodeInt(fr.getFriendID());
//            outPacket.encodeInt(fr.getFriendAccountID());
//        }
//
//        return outPacket;
//    }

    public static OutPacket macroSysDataInit(List<Macro> macros) {
        OutPacket outPacket = new OutPacket(OutHeader.MACRO_SYS_DATA_INIT);

        outPacket.encodeByte(macros.size());
        for (Macro macro : macros) {
            macro.encode(outPacket);
        }
        return outPacket;
    }

    public static OutPacket monsterBookSetCard(int id) {
//        OutPacket outPacket = new OutPacket(OutHeader.MONSTER_LIFE_INVITE_ITEM_RESULT);
        OutPacket outPacket = new OutPacket(OutHeader.MONSTER_BOOK_SET_CARD);

        outPacket.encodeByte(id > 0); // false -> already added msg
        if (id > 0) {
            outPacket.encodeInt(id);
            outPacket.encodeInt(1); // card count, but we're just going to stuck with 1.
        }

        return outPacket;
    }

    public static OutPacket characterPotentialReset(PotentialResetType prt, int arg) {
        OutPacket outPacket = new OutPacket(OutHeader.CHARACTER_POTENTIAL_RESET);

        outPacket.encodeByte(prt.ordinal());
        switch (prt) {
            case Pos:
                outPacket.encodeShort(arg);
                break;
            case Skill:
                outPacket.encodeInt(arg);
                break;
            case All:
                break;
        }
        return outPacket;
    }

    public static OutPacket characterPotentialSet(CharacterPotential cp) {
        return characterPotentialSet(true, true, cp.getKey(), cp.getSkillID(), cp.getSlv(), cp.getGrade(), true);
    }

    public static OutPacket characterPotentialSet(boolean exclRequest, boolean changed, short pos, int skillID,
                                                  int skillLevel, short grade, boolean updatePassive) {
        OutPacket outPacket = new OutPacket(OutHeader.CHARACTER_POTENTIAL_SET);

        outPacket.encodeByte(exclRequest);
        outPacket.encodeByte(changed);
        if (changed) {
            outPacket.encodeShort(pos);
            outPacket.encodeInt(skillID);
            outPacket.encodeShort(skillLevel);
            outPacket.encodeShort(grade);
            outPacket.encodeByte(updatePassive);
        }

        return outPacket;
    }

    public static OutPacket characterHonorExp(int exp) {
        OutPacket outPacket = new OutPacket(OutHeader.CHARACTER_HONOR_EXP);

        outPacket.encodeInt(exp);

        return outPacket;
    }

    public static OutPacket cashPetPickUpOnOffResult(boolean changed, boolean on) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_PET_PICK_UP_ON_OFF_RESULT);

        outPacket.encodeByte(changed);
        outPacket.encodeByte(on);


        return outPacket;
    }

    public static OutPacket setSonOfLinkedSkillResult(LinkedSkillResultType lsrt, int sonID, String sonName,
                                                      int originalSkillID, String existingParentName) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_SON_OF_LINKED_SKILL_RESULT);

        outPacket.encodeInt(lsrt.getVal());
        outPacket.encodeInt(originalSkillID);
        switch (lsrt) {
            case SetSonOfLinkedSkillResult_Success:
                outPacket.encodeInt(sonID);
                outPacket.encodeString(sonName);
                break;
            case SetSonOfLinkedSkillResult_Fail_ParentAlreadyExist:
                outPacket.encodeString(existingParentName);
                outPacket.encodeString(sonName);
                break;
            case SetSonOfLinkedSkillResult_Fail_Unknown:
                break;
            case SetSonOfLinkedSkillResult_Fail_MaxCount:
                outPacket.encodeString(existingParentName);
                break;
            case SetSonOfLinkedSkillResult_Fail_DBRequestFail:
                break;
        }

        return outPacket;
    }

    public static OutPacket memorialCubeResult(Equip equip, MemorialCubeInfo mci) {
        OutPacket outPacket = new OutPacket(OutHeader.MEMORIAL_CUBE_RESULT);

        outPacket.encodeLong(equip.getSerialNumber());
        mci.encode(outPacket);
        outPacket.encodeInt(0);//248++

        return outPacket;
    }

    public static OutPacket blackCubeResult(Equip equip, MemorialCubeInfo mci) {
        OutPacket outPacket = new OutPacket(OutHeader.BLACK_CUBE_RESULT);

        outPacket.encodeLong(equip.getId());
        mci.encode(outPacket);
        outPacket.encodeInt(equip.getBagIndex());
        outPacket.encodeInt(0);//248++
        outPacket.encodeByte(0);//248++

        return outPacket;
    }

    public static OutPacket whiteCubeResult(Equip equip, MemorialCubeInfo mci) {
        OutPacket outPacket = new OutPacket(OutHeader.WHITE_ADDITIONAL_CUBE_RESULT);

        outPacket.encodeLong(equip.getSerialNumber());
        mci.encode(outPacket);
        outPacket.encodeInt(equip.getBagIndex());
        outPacket.encodeInt(0);//248++
        outPacket.encodeByte(0);//248++

        return outPacket;
    }

    public static OutPacket broadcastMsg(BroadcastMsg broadcastMsg) {
        OutPacket outPacket = new OutPacket(OutHeader.BROADCAST_MSG);

        broadcastMsg.encode(outPacket);

        return outPacket;
    }

    public static OutPacket broadcastMessage(int type, String message){
        return broadcastMessage(type, 0, message, false);
    }

    public static OutPacket broadcastMessage(int type, int channel, String message, boolean megaEar) {
        OutPacket outPacket = new OutPacket(OutHeader.BROADCAST_MSG);

        outPacket.encodeByte(type);
        if (type == 4) {
            outPacket.encodeByte(1); // v207
        }
        if ((type != 23) && (type != 24)) {
            outPacket.encodeString(message);
        }
        switch (type) {
            case 3:
            case 36:
            case 37:
            case 38: // crashes
                outPacket.encodeByte(channel - 1);
                outPacket.encodeByte(megaEar ? 1 : 0);
                break;
            case 8:
                outPacket.encodeByte(channel - 1);
                outPacket.encodeByte(megaEar ? 1 : 0);
                outPacket.encodeByte(megaEar);
                break;
            case 12:
                outPacket.encodeInt(channel);
                break;
            case 6:
            case 11:
            case 20:
                outPacket.encodeInt((channel >= 1000000) && (channel < 6000000) ? channel : 0);
                break;
            case 24:
                outPacket.encodeShort(0);
            case 4:
            case 5:
            case 7:
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
        }
        return outPacket;
    }

    public static OutPacket goldApple(Item item, Item apple) {
        OutPacket outPacket = new OutPacket(OutHeader.GOLD_APPLE);

        outPacket.encodeByte((item != null));
        if (item != null) {
            outPacket.encodeInt(item.getItemId());//抽到的道具
            outPacket.encodeShort(item.getQuantity());
            outPacket.encodeInt(apple.getItemId());
            outPacket.encodeInt(apple.getBagIndex());
            outPacket.encodeInt(2435458);//2630612
            outPacket.encodeInt(1);
            outPacket.encodeByte(item.getType().getVal() == 1);
            if (item.getType().getVal() ==1) {
                Equip equip = (Equip) item;
                equip.encode(outPacket);
            }
        } else {
            outPacket.encodeInt(0);
        }

        return outPacket;
    }


    public static OutPacket setAvatarMegaphone(Char chr, int megaItemId, List<String> lineList, boolean whisperIcon) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_AVATAR_MEGAPHONE);

        outPacket.encodeInt(megaItemId); // Avatar Megaphone Item ID
        outPacket.encodeString(chr.getName());

        for (String line : lineList) {
            outPacket.encodeString(line);
        }

        chr.encodeChat(outPacket, lineList.get(0)); // v207
        outPacket.encodeInt(chr.getClient().getChannel() - 1);
        outPacket.encodeByte(whisperIcon);

        chr.getAvatarData().getAvatarLook().encode(outPacket); // encode AvatarLook
        outPacket.encodeByte(false); // new v207

        return outPacket;
    }

    public static OutPacket receiveHyperStatSkillResetResult(int charID, boolean exclRequest, boolean success) {
        OutPacket outPacket = new OutPacket(OutHeader.RECEIVE_HYPER_STAT_SKILL_RESET_RESULT);

        outPacket.encodeByte(exclRequest);
        outPacket.encodeInt(charID);
        outPacket.encodeByte(success);

        return outPacket;
    }

    public static OutPacket mapTransferResult(MapTransferType mapTransferType, byte itemType, int[] hyperrockfields) {
        OutPacket outPacket = new OutPacket(OutHeader.MAP_TRANSFER_RESULT);

        outPacket.encodeByte(mapTransferType.getVal()); // Map Transfer Type
        outPacket.encodeByte(itemType); // Item Type (1 = Teleport Rock, 2 = VIP Teleport Rock, 3 = Premium VIP Teleport Rock, 5 = Hyper Teleport Rock)
        if (mapTransferType == MapTransferType.DeleteListSend || mapTransferType == MapTransferType.RegisterListSend) {
            for (int fieldid : hyperrockfields) {
                outPacket.encodeInt(fieldid); // Target Field ID
            }
        }

        return outPacket;
    }

    public static OutPacket monsterCollectionResult(MonsterCollectionResultType mcrt, InvType invType, int fullSlots) {
        OutPacket outPacket = new OutPacket(OutHeader.MONSTER_COLLECTION_RESULT);

        outPacket.encodeInt(mcrt.ordinal());
        if (invType != null) {
            outPacket.encodeInt(invType.getVal());
        } else {
            outPacket.encodeInt(0);
        }
        outPacket.encodeInt(fullSlots);

        return outPacket;
    }

    public static OutPacket weatherEffectNotice(WeatherEffNoticeType type, String text, int duration) {
        OutPacket outPacket = new OutPacket(OutHeader.WEATHER_EFFECT_NOTICE);

        outPacket.encodeString(text); // Text
        outPacket.encodeInt(type.getVal()); // Weather Notice Type
        outPacket.encodeInt(duration); // Duration in ms
        outPacket.encodeByte(1); // Forced Notice
        outPacket.encodeInt(0);

        return outPacket;
    }

    public static OutPacket penaltyMsg(String msg, int type, int time, int type2) {
        OutPacket outPacket = new OutPacket(OutHeader.PENALTY_MSG.getValue());

        outPacket.encodeString(msg);
        outPacket.encodeInt(type);
        outPacket.encodeInt(time);
        outPacket.encodeByte(1);
        outPacket.encodeInt(type2);

        return outPacket;
    }

    public static OutPacket resultInstanceTable(String name, int type, int subType, boolean rightResult, int value) {
        OutPacket outPacket = new OutPacket(OutHeader.RESULT_INSTANCE_TABLE.getValue());

        outPacket.encodeString(name);
        outPacket.encodeInt(type); // nCount
        outPacket.encodeInt(subType);
        outPacket.encodeByte(rightResult);
        outPacket.encodeInt(value);

        return outPacket;
    }

    public static OutPacket resultInstanceTable(InstanceTableType ritt, boolean rightResult, int value) {
        return resultInstanceTable(ritt.getTableName(), ritt.getType(), ritt.getSubType(), rightResult, value);
    }

    /**
     * Creates a packet to indicate the golden hammer is finished.
     *
     * @param returnResult See below
     * @param //msg        when returnResult is:
     *                     0 or 1:
     *                     Anything: Golden hammer refinement applied
     *                     2:
     *                     0: Increased available upgrade by 1
     *                     1: Refining using golden hammer failed
     *                     3:
     *                     1: Item is not upgradable
     *                     2: 2 upgrade increases have been used already
     *                     3: You can't vicious hammer non-horntail necklace
     * @param upgradesLeft amount of upgrades left. NOTE: ((v9 >> 8) & 0xFF) - v9 + 2) (where v9 = upgradesLeft)
     * @return the created packet
     */
    public static OutPacket goldHammerItemUpgradeResult(GoldHammerResult returnResult, int result, int upgradesLeft) {
        // result shit seems random based on ^ notes so no enum
        OutPacket outPacket = new OutPacket(OutHeader.GOLD_HAMMER_ITEM_UPGRADE_RESULT);

        outPacket.encodeByte(returnResult.getVal());
        outPacket.encodeInt(result);
        if (returnResult.equals(GoldHammerResult.Success)) {
            outPacket.encodeInt(upgradesLeft);
        }

        return outPacket;
    }

    public static OutPacket returnToCharacterSelect() {
        return new OutPacket(OutHeader.RETURN_TO_CHARACTER_SELECT);
    }

    public static OutPacket returnToTitle() {
        return new OutPacket(OutHeader.RETURN_TO_TITLE);
    }

    public static OutPacket townPortal(TownPortal townPortal) {
        OutPacket outPacket = new OutPacket(OutHeader.TOWN_PORTAL); // As a response to Enter_TP_Request, creates the Door in the TownField

        outPacket.encodeInt(townPortal.getTownFieldId()); // townFieldId
        outPacket.encodeInt(townPortal.getFieldFieldId()); // field FieldId
        outPacket.encodeInt(townPortal.getSkillid()); // Skill Id
        outPacket.encodePosition(new Position()); // fieldField TownPortal Position

        return outPacket;
    }

    public static OutPacket givePopularityResult(PopularityResultType prType, Char targetChr, int newFame, boolean inc) {
        OutPacket outPacket = new OutPacket(OutHeader.GIVE_POPULARITY_RESULT);

        outPacket.encodeByte(prType.getVal());

        switch (prType) {
            case Success:
                outPacket.encodeString(targetChr.getName());
                outPacket.encodeByte(inc); // true = fame  |  false = defame
                outPacket.encodeInt(newFame);
                break;

            case InvalidCharacterId:
            case LevelLow:
            case AlreadyDoneToday:
            case AlreadyDoneTarget:
                break;

            case Notify:
                outPacket.encodeString(targetChr.getName());
                outPacket.encodeByte(inc); // true = fame  |  false = defame
                break;
        }

        return outPacket;
    }

    public static OutPacket randomPortalNotice(RandomPortal randomPortal) {
        OutPacket outPacket = new OutPacket(OutHeader.RANDOM_PORTAL_NOTICE);

        outPacket.encodeByte(randomPortal.getAppearType().ordinal());
        outPacket.encodeInt(randomPortal.getField().getId());

        return outPacket;
    }

    public static OutPacket randomMissionResult(RandomMissionType type, int arg1, int arg2) {
        OutPacket outPacket = new OutPacket(OutHeader.RANDOM_MISSION_RESULT);

        outPacket.encodeInt(type.getVal());
        outPacket.encodeInt(arg1);
        outPacket.encodeInt(arg2);

        return outPacket;
    }

    public static OutPacket matrixUpdate(List<MatrixRecord> matrixRecords, boolean remove, int removeType, int removeArg) {
        OutPacket outPacket = new OutPacket(OutHeader.MATRIX_UPDATE);

        outPacket.encodeInt(matrixRecords.size());
        for (MatrixRecord mr : matrixRecords) {
            outPacket.encode(mr);
        }
        List<MatrixRecord> activeRecords = matrixRecords.stream().filter(MatrixRecord::isActive).collect(Collectors.toList());
        outPacket.encodeInt(activeRecords.size());
        for (MatrixRecord mr : activeRecords) {
            outPacket.encodeInt(matrixRecords.indexOf(mr));
            outPacket.encodeInt(mr.getPosition());
            outPacket.encodeInt(mr.getSlv()); // nLevel
            outPacket.encodeByte(1); // bHide
        }
        outPacket.encodeByte(remove);
        outPacket.encodeInt(removeType);
        if (removeType == 0) {
            outPacket.encodeInt(removeArg);
        }

        return outPacket;
    }

    public static OutPacket nodestoneOpenResult(MatrixRecord mr) {
        OutPacket outPacket = new OutPacket(OutHeader.NODE_STONE_OPEN_RESULT);

        outPacket.encodeInt(mr.getIconID());
        outPacket.encodeInt(mr.getSlv()); // core level 原本發1
        outPacket.encodeInt(mr.getSkillID1());
        outPacket.encodeInt(mr.getSkillID2());
        outPacket.encodeInt(mr.getSkillID3());
        outPacket.encodeInt(0); // ?

        return outPacket;
    }

    public static OutPacket nodeEnhanceResult(int pos, int expGained, int oldSlv, int newSlv) {
        OutPacket outPacket = new OutPacket(OutHeader.NODE_ENHANCE_RESULT);

        outPacket.encodeInt(pos);
        outPacket.encodeInt(expGained);
        outPacket.encodeInt(oldSlv);
        outPacket.encodeInt(newSlv);

        return outPacket;
    }

    public static OutPacket nodeDisassembleResult(int shardsGained) {
        OutPacket outPacket = new OutPacket(OutHeader.NODE_DISASSEMBLE_RESULT);

        outPacket.encodeInt(shardsGained);

        return outPacket;
    }

    public static OutPacket nodeCraftResult(MatrixRecord mr, int count) {
        OutPacket outPacket = new OutPacket(OutHeader.NODE_CRAFT_RESULT);

        outPacket.encodeInt(mr.getIconID());
        outPacket.encodeInt(mr.getSlv());
        outPacket.encodeInt(mr.getSkillID1());
        outPacket.encodeInt(mr.getSkillID2());
        outPacket.encodeInt(mr.getSkillID3());
        outPacket.encodeInt(count);
        return outPacket;
    }

    public static OutPacket openCore(boolean success) {
        OutPacket outPacket = new OutPacket(OutHeader.MATRIX_OPEN);

        outPacket.encodeInt(3);
        outPacket.encodeByte(success);

        return outPacket;
    }

    public static OutPacket ArcaneCatalyst(Equip equip, int slot) {
        OutPacket outPacket = new OutPacket(OutHeader.ARCANE_CATALYST);

        outPacket.encode(equip);
        outPacket.encodeInt(slot);

        return outPacket;
    }

    public static OutPacket ArcaneCatalyst2(Equip equip) {
        OutPacket outPacket = new OutPacket(OutHeader.ARCANE_CATALYST2);

        outPacket.encode(equip);

        return outPacket;
    }

    public static OutPacket createCore(int type) {
        OutPacket outPacket = new OutPacket(OutHeader.CREATE_CORE);

        outPacket.encodeByte(type);

        return outPacket;
    }

    public static OutPacket AlarmAuction(int type, AuctionItem auctionItem) {
        OutPacket outPacket = new OutPacket(OutHeader.ALARM_AUCTION);
        outPacket.encodeByte(type);
        if (auctionItem == null) {
            outPacket.encodeArr(new byte[10000]);
            return outPacket;
        }
        auctionItem.encodeHistory(outPacket);

        return outPacket;
    }

    public static OutPacket OnBomb(int skillid, int slv, Position position) {
        OutPacket outPacket = new OutPacket(OutHeader.ON_BOMB);

        outPacket.encodeInt(skillid);
        outPacket.encodeInt(slv);
        outPacket.encodePositionInt(position);
        outPacket.encodeByte(0);

        return outPacket;
    }

    public static OutPacket antiMacroResult(final byte[] image, byte notificationType, byte antiMacroType) {
        return antiMacroResult(image, notificationType, antiMacroType, (byte) 0, (byte) 1);
    }

    public static OutPacket antiMacroResult(final byte[] image, byte notificationType, byte antiMacroType, byte first, byte refreshAntiMacroCount) {
        OutPacket outPacket = new OutPacket(OutHeader.ANTI_MACRO_RESULT);

        outPacket.encodeByte(notificationType);
        outPacket.encodeByte(antiMacroType);

        if (notificationType == AntiMacro.AntiMacroResultType.AntiMacroRes.getVal()) {
            outPacket.encodeByte(first);
            outPacket.encodeByte(refreshAntiMacroCount);

            if (image == null) {
                outPacket.encodeInt(0);
            } else {
                outPacket.encodeInt(image.length);
                outPacket.encodeArr(image);
            }
        } else if (notificationType == AntiMacro.AntiMacroResultType.AntiMacroRes_Fail.getVal() ||
                notificationType == AntiMacro.AntiMacroResultType.AntiMacroRes_Success.getVal()) {
            outPacket.encodeString(""); // unused?
        }

        return outPacket;
    }

    public static OutPacket report(int mode) {
        OutPacket outPacket = new OutPacket(OutHeader.CLAIM_RESULT);
        outPacket.encodeByte(mode);
        switch (mode) {
            case 2:
                outPacket.encodeByte(0);
                outPacket.encodeInt(1);
                outPacket.encodeInt(0);
                break;
        }
        return outPacket;
    }

    public static OutPacket OnSetClaimSvrAvailableTime(int from, int to) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_CLAIM_SVR_AVAILABLE_TIME);
        outPacket.encodeByte(from);
        outPacket.encodeByte(to);
        return outPacket;
    }

    public static OutPacket onSessionValue(String key, String value) {
        OutPacket outPacket = new OutPacket(OutHeader.SESSION_VALUE);

        outPacket.encodeString(key);
        outPacket.encodeString(value);
        return outPacket;
    }

    public static OutPacket setPassenserRequest(int requestorChrId) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_PASSENGER_REQUEST);

        outPacket.encodeInt(requestorChrId);

        return outPacket;
    }

    public static OutPacket ClaimSvrStatusChanged(boolean enable) {
        OutPacket outPacket = new OutPacket(OutHeader.CLAIM_SVR_STATUS_CHANGED);
        outPacket.encodeByte(enable ? 1 : 0);
        return outPacket;
    }

    public static OutPacket platformarEnterResult(boolean wrap) {
        OutPacket outPacket = new OutPacket(OutHeader.PLATFORMAR_ENTER_RESULT);

        outPacket.encodeByte(wrap);

        return outPacket;
    }

    public static OutPacket platformarOxyzen(int oxyzen) {
        OutPacket outPacket = new OutPacket(OutHeader.PLATFORMAR_OXYZEN);

        outPacket.encodeInt(oxyzen); // casted to long in client side

        return outPacket;
    }

    public static OutPacket setMaplePoints(int maplePoints) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_MAPLE_POINT);

        outPacket.encodeInt(maplePoints);

        return outPacket;
    }

    public static OutPacket linkSkillResult(int skillID, LinkSkillResult lsr) {
        OutPacket outPacket = new OutPacket(OutHeader.LINK_SKILL_RESULT);

        outPacket.encodeInt(skillID);
        outPacket.encodeInt(lsr.ordinal());

        return outPacket;
    }

    public static OutPacket unlinkedSkillInfo(LinkSkill linkSkill) {
        return unlinkedSkillInfo(Collections.singleton(linkSkill));
    }

    public static OutPacket unlinkedSkillInfo(Set<LinkSkill> linkSkills) {
        OutPacket outPacket = new OutPacket(OutHeader.UNLINKED_SKILL_INFO);

        outPacket.encodeInt(linkSkills.size());
        for (LinkSkill ls : linkSkills) {
            outPacket.encodeInt(ls.getLinkSkillID());
            outPacket.encodeInt(ls.getOriginID());
        }

        return outPacket;
    }

    public static OutPacket linkedSkillInfo(LinkSkill linkSkill, int stackedLevel) {
        OutPacket outPacket = new OutPacket(OutHeader.LINKED_SKILL_INFO);

        outPacket.encodeInt(linkSkill.getOriginID());
        outPacket.encodeInt(linkSkill.getUsingID());
        outPacket.encodeInt(linkSkill.getLinkSkillID());
        outPacket.encodeShort(linkSkill.getLevel());
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.ZERO_TIME)); // ftLastAssigned

        outPacket.encodeInt(linkSkill.getLinkSkillID());
        outPacket.encodeInt(linkSkill.getLinkSkillID());
        if (linkSkill.getLinkSkillID() > 0) {
            outPacket.encodeInt(stackedLevel);
        }

        return outPacket;
    }

    public static OutPacket towerChairSettingResult() {
        OutPacket outPacket = new OutPacket(OutHeader.TOWER_CHAIR_SETTING_RESULT);

        return outPacket;
    }

    public static OutPacket unionPresetInfoResult(int preset, boolean unlocked, UnionBoard ub) {
        OutPacket outPacket = new OutPacket(OutHeader.UNION_PRESET_INFO_RESULT);

        outPacket.encodeInt(preset);
        outPacket.encodeByte(unlocked);
        if (unlocked) {
            outPacket.encode(ub);
        }

        return outPacket;
    }

    public static OutPacket setBossReward(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.BOSS_REWARD);

        Iterator<Item> ite = chr.getEtcInventory().getItems().iterator();
        List<Item> items = new ArrayList<>();
        while (ite.hasNext()) {
            Item item = ite.next();
            if (item.getBossReward() != null) {
                items.add(item);
            }
        }

        outPacket.encodeInt(items.size());
        for (int i = 0; i < items.size(); i++) {
            outPacket.encodeLong(((Item) items.get(i)).getBossReward().getObjectId());
            outPacket.encodeInt(((Item) items.get(i)).getBossReward().getMobId());
            outPacket.encodeInt(((Item) items.get(i)).getBossReward().getPartyId());
            outPacket.encodeInt(((Item) items.get(i)).getBossReward().getPrice());
            outPacket.encodeInt(0);
            outPacket.encodeLong(0);
            //long time =items.get(i).getDateExpire() - FileTime.fromLong(7 * 24 * 60 * 60 * 1000);
            outPacket.encodeLong(FileTime.currentTime().toLong());
        }

        return outPacket;
    }

    public static OutPacket WonderBerry(byte effect, Item item, int useitemid) {
        OutPacket outPacket = new OutPacket(OutHeader.WONDER_BERRY);

        outPacket.encodeByte(effect);
        outPacket.encodeInt(useitemid);
        if (item.getType() == Item.Type.EQUIP) {
            Equip equip = (Equip) item;
            equip.encode(outPacket);
        } else {
            item.encode(outPacket);
        }
        return outPacket;
    }

    public static OutPacket setNpcNameInvisible(int npcid, boolean show) {
        OutPacket outPacket = new OutPacket(OutHeader.NPC_NAME_INVISIBLE);

        outPacket.encodeInt(npcid);
        outPacket.encodeByte(show);

        return outPacket;
    }

    public static OutPacket mixLense(int itemId, int baseFace, int newFace, boolean isDreeUp, boolean isBeta, boolean isAlphaBeta, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.MIX_LENSE);

        outPacket.encodeInt(itemId);
        outPacket.encodeByte(true);
        outPacket.encodeByte(isDreeUp || isBeta);
        outPacket.encodeByte(isAlphaBeta);
        outPacket.encodeByte(false);
        outPacket.encodeInt(1);
        outPacket.encodeInt(1);
        outPacket.encodeByte(2);
        outPacket.encodeInt(newFace);
        outPacket.encodeInt(baseFace);
        outPacket.encodeShort(0);
        outPacket.encodeShort(-1);
        outPacket.encodeArr(new byte[5]);

        return outPacket;
    }

    public static OutPacket sendHotTimeReward(HotTimeRewardSendType sendType, Stack<HotTimeReward> rewards) {
        OutPacket outPacket = new OutPacket(OutHeader.SOME_REWARD);
        // 9 - give reward 11 - maple point, 12 - game item, 13 - cash, 14 - meso, 15 - exp
        outPacket.encodeByte(sendType.getValue());
        int numOfItems = rewards.size();
        HotTimeReward reward;

        outPacket.encodeInt(numOfItems);  // numOfItems

        while (!rewards.isEmpty()) {
            reward = rewards.pop();
            outPacket.encodeInt(reward.getId());            // ??

            outPacket.encodeFT(reward.getStartTime());    // start time
            outPacket.encodeFT(reward.getEndTime());      // end time
            outPacket.encodeFT(reward.getStartTime());    // start time
            outPacket.encodeFT(reward.getEndTime());      // end time

            outPacket.encodeInt(reward.getRewardType().getValue()); // reward type: [game item, cash item, maplepoint, meso, exp]
            outPacket.encodeInt(reward.getItemId()); // itemId
            outPacket.encodeInt(reward.getQuantity()); // quantity

            outPacket.encodeInt(0);             // maplepoint?
            outPacket.encodeLong(0);            // meso?
            outPacket.encodeInt(0);             // exp?

            outPacket.encodeInt(reward.getMaplePointAmount());      // ?? maplepoint
            outPacket.encodeLong(reward.getMesoAmount());           // ?? meso
            outPacket.encodeInt(reward.getExpAmount());             // ?? exp

            outPacket.encodeInt(0);             // ??
            outPacket.encodeInt(0);             // ??

            outPacket.encodeString("");         // ??
            outPacket.encodeString("");         // ??
            outPacket.encodeString("");         // ??

            outPacket.encodeString(reward.getDescription()); // reward description
        }

        return outPacket;
    }

    public static OutPacket sendHotTimeRewardResult(int sendType, HotTimeReward reward) {
        OutPacket outPacket = new OutPacket(OutHeader.SOME_REWARD);
        // 9 - give reward 11 - maple point, 12 - game item, 13 - cash, 14 - meso, 15 - exp
        outPacket.encodeByte(sendType);

        switch (sendType) {
            case 11: // success: maple points
                outPacket.encodeInt(reward.getId());
                outPacket.encodeInt(reward.getMaplePointAmount());
                break;
            case 12: // success: game item
            case 13: // success: cash item
                outPacket.encodeInt(reward.getId());
                break;
            case 14: // success: meso
                outPacket.encodeInt(reward.getId());
                outPacket.encodeLong(reward.getMesoAmount());
                break;
            case 15: // success: exp
                outPacket.encodeInt(reward.getId());
                outPacket.encodeInt(reward.getExpAmount());
                break;
            case 21: // ?? 102 - inv full, 103 - already have item
                outPacket.encodeByte(0);
            case 22: // ??, 34 - inv full, -1 - cash item failed, 36 - already have item
                outPacket.encodeByte(0);
            case 20: // failed: maplePoints
            case 23: // failed: meso
            case 24: // failed: exp
                break;
        }

        return outPacket;
    }

    /*
    credit: kinneykin
    case 9:
                outPacket.encodeInt(rewards.size());
                if (rewards.size() > 0) {
                    for (int i = 0; i < rewards.size(); i++) {
                        RewardItems reward = rewards.get(i);
                        boolean empty = reward.getId() < 1;
                        // outPacket.encodeArr("27 C4 01 00");
                        outPacket.encodeInt(empty ? 0 : reward.getId()); // 0 = blank 1+ = gift
                        if (!empty) {
                            if ((option & 1) != 0) {
                                outPacket.encodeLong(reward.getReceiveDate()); //start time
                                outPacket.encodeLong(reward.getExpireDate()); //end time
                                outPacket.encodeLong(reward.getReceiveDate()); //start time
                                outPacket.encodeLong(reward.getExpireDate()); //end time
                            }
                            outPacket.encodeInt(reward.getType()); //type 1 = item 3 = maple point 4 = mesos 5 = exp
                            outPacket.encodeInt(reward.getItem()); // item id
                            outPacket.encodeInt(1); // item quantity (?)
                            outPacket.encodeInt(1440);
                            outPacket.encodeLong(0);
                            outPacket.encodeInt(0);
                            outPacket.encodeInt(reward.getMaplePoints()); // maple point amount
                            outPacket.encodeLong(reward.getMeso()); // mesos amount
                            outPacket.encodeInt(reward.getExp()); // exp amount
                            outPacket.encodeInt(-99);
                            outPacket.encodeInt(-99);
                            outPacket.encodeString("");
                            outPacket.encodeString("");
                            outPacket.encodeString("");
                            outPacket.encodeString(reward.getDesc());
                        }
                    }
                }
                break;
     */

    public static OutPacket inheritanceInfoResult(boolean canOpen, int rank, int levelReq, int newLazuli, int newLapis, int newRank, int essenceID, int essenceReq) {
        OutPacket outPacket = new OutPacket(OutHeader.INHERITANCE_INFO);

        outPacket.encodeByte(canOpen); //Can Open
        outPacket.encodeByte(1);

        outPacket.encodeInt(rank); //Current Rank
        outPacket.encodeInt(levelReq);

        outPacket.encodeInt(newLazuli);
        outPacket.encodeInt(newLapis);

        outPacket.encodeInt(newRank); //New Rank

        outPacket.encodeInt(essenceID); //EssenceID
        outPacket.encodeInt(essenceReq); //Essence count

        outPacket.encodeByte(0); //Unk
        return outPacket;
    }

    public static OutPacket UseMakeUpCoupon(Char chr, int itemid, int... make) {
        OutPacket outPacket = new OutPacket(OutHeader.USE_MAKEUP_COUPON);

        outPacket.encodeByte(1);
        outPacket.encodeInt(1);
        outPacket.encodeInt(4);
        outPacket.encodeInt(itemid);
        outPacket.encodeByte(1);//chr.getdressup 1 or 0

        return outPacket;
    }

    public static OutPacket unkZero() {
        OutPacket outPacket = new OutPacket(OutHeader.EGOEQUIP_CREATE_UPGRADE_ITEM_COST_INFO);

        outPacket.encodeArr("01 00 00 00 A0 86 01 00 58 02 00 00 00 00 00"); //Can Open

        //outPacket.encodeInt(1); //Openable
        //outPacket.encodeInt(50000);
        //outPacket.encodeInt(500);
        //outPacket.encodeShort(0);
        //outPacket.encodeByte(0);


        return outPacket;
    }

    public static OutPacket unkZero2() {
        OutPacket outPacket = new OutPacket(OutHeader.EGOEQUIP_CREATE_UPGRADE_ITEM_COST_INFO);

        outPacket.encodeArr("00 00 00 00 50 C3 00 00 F4 01 00 00 00 00 00");
        return outPacket;
    }

    public static OutPacket EquipGaugeComplete() {
        OutPacket outPacket = new OutPacket(OutHeader.EGOEQUIP_GAUGE_COMPLETE);

        return outPacket;
    }

    public static OutPacket mannequinRes(byte type, byte result, int type2) {
        OutPacket outPacket = new OutPacket(OutHeader.MANNEQUIN_RESULT.getValue());

        outPacket.encodeByte(type);
        outPacket.encodeByte(result);
        outPacket.encodeByte(type2);

        return outPacket;
    }

    public static OutPacket mannequin(byte type, byte result, int type2, byte slot, Mannequin mannequin) {
        OutPacket outPacket = new OutPacket(OutHeader.MANNEQUIN.getValue());

        outPacket.encodeByte(type);
        outPacket.encodeByte(result);
        outPacket.encodeByte(type2);
        switch(type2) {
            case 0:
                break;
            case 1:
                outPacket.encodeByte(9);
                outPacket.encodeByte(9);
                break;
            case 2:
                outPacket.encodeByte(slot);
                outPacket.encodeByte(0);
                outPacket.encodeByte(0);
                outPacket.encodeInt(mannequin.getValue());
                outPacket.encodeByte(mannequin.getBaseColor());
                outPacket.encodeByte(mannequin.getAddColor());
                outPacket.encodeByte(mannequin.getBaseProb());
                break;
            case 3:
                outPacket.encodeByte(8);
                outPacket.encodeByte(slot);
                outPacket.encodeByte(slot -1);
                outPacket.encodeByte(0);
                outPacket.encodeByte(0);
                outPacket.encodeInt(0);
                outPacket.encodeByte(-1);
                outPacket.encodeByte((type == 1 || type == 2) ? -1 : 0);
                outPacket.encodeByte(0);
                break;
            case 5:
                outPacket.encodeInt(0);
                break;
        }

        return outPacket;
    }

    public static OutPacket getChatEmoticon(byte type, short slot, short slot2, int emoticon, String a) {
        OutPacket outPacket = new OutPacket(OutHeader.CHAT_EMOTICON);
        Iterator<Integer> iterator;
        int size;
        outPacket.encodeByte(type);
        switch (type) {
            case 0:
                size = ((List) ChatEmoticonData.getEmoticons().get(Integer.valueOf(emoticon))).size();
                outPacket.encodeShort(slot);
                outPacket.encodeInt(emoticon);
                outPacket.encodeInt(size);
                for (iterator = ((List) ChatEmoticonData.getEmoticons().get(Integer.valueOf(emoticon))).iterator(); iterator.hasNext();) {
                    int em = ((Integer) iterator.next()).intValue();
                    outPacket.encodeInt(em);
                    outPacket.encodeFT(FileTime.fromType(FileTime.Type.ZERO_TIME));
                    outPacket.encodeShort(0);
                }
                break;
            case 1:
            case 7:
            case 9:
                outPacket.encodeShort(slot);
                outPacket.encodeShort(slot2);
                break;
            case 2:
            case 3:
                outPacket.encodeShort(slot);
                break;
            case 4:
                outPacket.encodeShort(slot);
                outPacket.encodeArr(new byte[14]);
                break;
            case 5:
                outPacket.encodeInt(emoticon);
                outPacket.encodeShort(slot);
                break;
            case 6:
                outPacket.encodeInt(emoticon);
                break;
            case 8:
                outPacket.encodeShort(slot);
                outPacket.encodeInt(emoticon);
                outPacket.encodeString(a, 25);
                break;
            case 10:
                outPacket.encodeShort(slot);
                break;
            case 11:
                outPacket.encodeInt(emoticon);
                outPacket.encodeString(a);
                break;
        }
        return outPacket;
    }
}
