package net.swordie.ms.handlers.item;

import jnr.ffi.annotations.In;
import net.swordie.ms.Server;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.BroadcastMsg;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.avatar.BeautyAlbum;
import net.swordie.ms.client.character.items.*;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.QuestConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.pet.Pet;
import net.swordie.ms.life.pet.PetSkill;
import net.swordie.ms.loaders.FieldData;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;
import net.swordie.ms.loaders.containerclasses.MakingSkillRecipe;
import net.swordie.ms.scripts.ScriptType;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Randomizer;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Triple;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.World;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.Portal;
import net.swordie.ms.world.gach.result.GachaponResult;
import org.apache.log4j.Logger;
import org.graalvm.nativeimage.c.struct.CField;

import java.text.NumberFormat;
import java.util.*;

import static net.swordie.ms.client.character.items.Item.Type.getTypeById;
import static net.swordie.ms.enums.ChatType.*;
import static net.swordie.ms.enums.EquipBaseStat.tuc;
import static net.swordie.ms.enums.InvType.*;
import static net.swordie.ms.enums.InventoryOperation.Move;

public class ItemHandler {

    private static final Logger log = Logger.getLogger(ItemHandler.class);

    @Handler(op = InHeader.USER_PORTAL_SCROLL_USE_REQUEST)
    public static void handleUserPortalScrollUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        if ((field.getFieldLimit() & FieldOption.PortalScrollLimit.getVal()) > 0 || !field.isChannelField()) {
            chr.chatMessage("You may not use a return scroll in this map.");
            chr.dispose();
            return;
        }
        c.verifyTick(inPacket);
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();

        System.out.println(itemID);

        Item item = chr.getConsumeInventory().getItemBySlot(slot);

        if (item == null || item.getItemId() != itemID || item.getQuantity() < 1 || !chr.isInValidState()) {
            chr.dispose("You cannot use this return scroll right now.");
            return;
        }

        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        Field toField;

        if (itemID != 2030000 && itemID != 2030059) {
            toField = chr.getOrCreateFieldByCurrentInstanceType(ii.getMoveTo());
        } else {
            toField = chr.getOrCreateFieldByCurrentInstanceType(field.getReturnMap());
        }
        Portal portal = toField.getDefaultPortal();
        chr.warp(toField, portal);
        chr.consumeItem(itemID, 1);
    }

    @Handler(op = InHeader.SALON_QUERY)
    public static void handleSalonQuery(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt();
        if (inPacket.getUnreadAmount() == 0) {
            chr.write(UserLocal.beautyAlbumActions(0, chr, 0, 0));
            return;
        }
        int opType = inPacket.decodeByte();
        switch (opType) {
            case 7:
                int slotId = inPacket.decodeInt();
                int styleId = inPacket.decodeInt();
                inPacket.decodeShort();
                int styleToSave = (slotId / 10000 == 3 ? chr.getAvatarData().getAvatarLook().getHair() : chr.getAvatarData().getAvatarLook().getFace());
                BeautyAlbum styleToAdd = new BeautyAlbum(styleToSave, slotId);
                chr.addStyleToBeautyAlbum(styleToAdd);
                chr.write(UserLocal.beautyAlbumActions(7, chr, styleToSave, slotId));
                break;
            case 3:
                int slotId2 = inPacket.decodeInt();
                inPacket.decodeInt();
                chr.getScriptManager().changeCharacterLook(chr.getStyleBySlotId(slotId2).getStyleID());
                chr.write(UserLocal.beautyAlbumActions(1, chr, 0, 0));
                break;
            case 1:
                int slotId1 = inPacket.decodeInt();
                BeautyAlbum styleToAdd1 = chr.getStyleBySlotId(slotId1);
                chr.removeStyleToBeautyAlbum(styleToAdd1);
                chr.write(UserLocal.beautyAlbumActions(7, chr, 0, slotId1));
                break;
            default:
                break;
        }
        return;
    }

    @Handler(op = InHeader.REWARD_REQUEST)
    public static void handleRewardRequest(Char chr, InPacket inPacket) {
        int id = inPacket.decodeInt(); // id of item to remove in list
        int itemType = inPacket.decodeInt();
        int itemId = inPacket.decodeInt(); // item id
        int quantity = inPacket.decodeInt(); // quantity

        int int1 = inPacket.decodeInt(); // maplepoint?
        long long1 = inPacket.decodeLong(); // meso?
        int int2 = inPacket.decodeInt(); // exp?

        int maplePointAmount = inPacket.decodeInt();
        long mesoAmount = inPacket.decodeLong();
        int expAmount = inPacket.decodeInt();

        int int3 = inPacket.decodeInt(); // ??
        int int4 = inPacket.decodeInt(); // ??

        String str1 = inPacket.decodeString(); // ??
        String str2 = inPacket.decodeString(); // ??
        String str3 = inPacket.decodeString(); // ??

        byte rewardAccepted = inPacket.decodeByte();
        HotTimeReward reward = chr.getHotTimeRewards().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (reward == null) {
            chr.chatMessage("Can't find giftID!");
            chr.dispose();
            return;
        }
        if (rewardAccepted == 1) {
            if (reward.getItemId() != itemId || reward.getQuantity() != quantity) {
                chr.chatMessage("Nice try, guy.");
                log.error(String.format("[REWARD_REQUEST] User %s data mismatch on id %d", chr.getUser().getName(), id));
                log.error(String.format("[REWARD_REQUEST] itemId: expected %d - actual %d", reward.getItemId(), itemId));
                log.error(String.format("[REWARD_REQUEST] quantity: expected %d - actual %d", reward.getQuantity(), quantity));
                log.error(String.format("[REWARD_REQUEST] itemId: expected %d - actual %d", reward.getItemId(), itemId));
                log.error(String.format("[REWARD_REQUEST] maplePoint: expected %d - actual %d", reward.getMaplePointAmount(), maplePointAmount));
                log.error(String.format("[REWARD_REQUEST] meso: expected %d - actual %d", reward.getMesoAmount(), mesoAmount));
                log.error(String.format("[REWARD_REQUEST] exp: expected %d - actual %d", reward.getExpAmount(), expAmount));
            } else {
                int hotTimeResultId = 0;
                // TODO: dog shit code fix this
                Item item;
                Inventory inventory = new Inventory();
                if (itemId > 0) {
                    item = ItemData.getItemDeepCopy(itemId);
                    inventory = chr.getInventoryByType(item.getInvType());
                }
                // send dialog
                switch (reward.getRewardType()) {
                    case GAME_ITEM:         // 12 - id
                        if (!inventory.isFull() && !reward.getEndTime().isExpired()) {
                            hotTimeResultId = HotTimeRewardResult.SUCCESS_GAME_ITEM.getValue();
                            chr.addItemToInventory(reward.getItemId(), reward.getQuantity());
                        } else {
                            hotTimeResultId = HotTimeRewardResult.FAILED_GAME_ITEM.getValue();
                            chr.chatMessage("The time limit for this gift has expired.");
                            chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
                            return;
                        }
                        break;
                    case CASH_ITEM:         // 13 - id
                        if (!inventory.isFull()) {
                            hotTimeResultId = HotTimeRewardResult.SUCCESS_CASH_ITEM.getValue();
                            chr.addItemToInventory(reward.getItemId(), reward.getQuantity());
                        } else {
                            hotTimeResultId = HotTimeRewardResult.FAILED_CASH_ITEM.getValue();
                            chr.chatMessage("The time limit for this gift has expired.");
                            chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
                            return;
                        }
                        break;
                    case MAPLE_POINT:       // 11 - id, amount
                        if (!reward.getEndTime().isExpired()) {
                            hotTimeResultId = HotTimeRewardResult.SUCCESS_MAPLE_POINTS.getValue();
                            chr.addNx(reward.getMaplePointAmount());
                        } else {
                            hotTimeResultId = HotTimeRewardResult.FAILED_MAPLE_POINTS.getValue();
                            chr.chatMessage("The time limit for this gift has expired.");
                            chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
                            return;
                        }
                        break;
                    case MESO:              // 14 - id, amount
                        if (!reward.getEndTime().isExpired()) {
                            hotTimeResultId = HotTimeRewardResult.SUCCESS_MESO.getValue();
                            chr.addMoney(reward.getMesoAmount());
                        } else {
                            hotTimeResultId = HotTimeRewardResult.FAILED_MESO.getValue();
                            chr.chatMessage("The time limit for this gift has expired.");
                            chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
                            return;
                        }
                        break;
                    case EXPERIENCE:        // 15 - id, amount
                        if (!reward.getEndTime().isExpired()) {
                            hotTimeResultId = HotTimeRewardResult.SUCCESS_EXPERIENCE.getValue();
                            chr.addExp(reward.getExpAmount());
                        } else {
                            hotTimeResultId = HotTimeRewardResult.FAILED_EXPERIENCE.getValue();
                            chr.chatMessage("The time limit for this gift has expired.");
                            chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
                            return;
                        }
                        break;
                }

                chr.write(WvsContext.sendHotTimeRewardResult(hotTimeResultId, reward));
            }
        }

        chr.removeHotTimeReward(reward);
        chr.getHotTimeRewards().remove(reward);
    }


    @Handler(op = InHeader.USER_STAT_CHANGE_ITEM_CANCEL_REQUEST)
    public static void handleUserStatChangeItemCancelRequest(Char chr, InPacket inPacket) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int itemID = inPacket.decodeInt();
        tsm.removeStatsBySkill(itemID);
        tsm.sendResetStatPacket();
    }


    @Handler(op = InHeader.USER_CONSUME_CASH_ITEM_USE_REQUEST)
    public static void handleUserConsumeCashItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Inventory cashInv = chr.getInventoryByType(InvType.CASH);
        inPacket.decodeInt(); // tick
        short pos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Item item = cashInv.getItemBySlot(pos);
        ItemInfo itemInfo = ItemData.getItemInfoByID(itemID);
        if (item == null || item.getItemId() != itemID) {
            return;
        }
        if (itemID == 5064000 || itemID == 5064100 || itemID == 5064300 || itemID == 5064400) {
            inPacket.decodeShort();
        }

        if (itemID / 10000 == 553) {
            // Reward items
            Item reward = itemInfo.getRandomReward();
            chr.addItemToInventory(reward);

        } else if (itemID / 10000 == 555) {//墜飾
            //Beauty Salon Slots
            int salonType = itemID % 5550000;
            switch (salonType) {
                case 2000:
                    chr.write(UserLocal.beautyAlbumActions(5, chr, 20000, chr.getInventoryByType(InvType.getInvTypeByString("Face")).getSlots() + 1));
                    chr.getInventoryByType(InvType.getInvTypeByString("Face")).addSlots((byte) 1);
                    return;
                case 3000:
                    chr.write(UserLocal.beautyAlbumActions(5, chr, 30000, chr.getInventoryByType(InvType.getInvTypeByString("Hair")).getSlots() + 1));
                    chr.getInventoryByType(InvType.getInvTypeByString("Hair")).addSlots((byte) 1);
                    return;
            }

        } else if (itemID / 10000 == 512) { // Weather Effects 天氣效果
            String text = inPacket.decodeString();
            Field field = c.getChr().getField();
            field.broadcastPacket(FieldPacket.blowWeather(itemID, chr.getName() + " : " + text, 10, null));
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            field.broadcastPacket(FieldPacket.removeBlowWeather());
                        }
                    },
                    10000
            );

        } else if (itemID / 10000 == 539) {//喇叭系列
            // Avatar Megaphones
            List<String> lineList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String line = inPacket.decodeString();
                lineList.add(line);
            }
            boolean whisperIcon = inPacket.decodeByte() != 0;
            World world = c.getWorld();
            world.broadcastPacket(WvsContext.setAvatarMegaphone(chr, itemID, lineList, whisperIcon));

        } else if (itemID / 10000 == 519) {
            // Pet Skill Items
            long sn = inPacket.decodeLong();
            PetSkill ps = ItemConstants.getPetSkillFromID(itemID);
            if (ps == null) {
                chr.chatMessage(String.format("Unhandled pet skill item %d", itemID));
                return;
            }
            Item pi = chr.getCashInventory().getItemBySN(sn);
            if (!(pi instanceof PetItem)) {
                chr.chatMessage("Could not find that pet.");
                return;
            }
            boolean add = itemID < 5191000; // add property doesn't include the "Slimming Medicine"
            PetItem petItem = (PetItem) pi;
            if (add) {
                petItem.addPetSkill(ps);
            } else {
                petItem.removePetSkill(ps);
            }
            petItem.updateToChar(chr);
            chr.consumeItem(item);

        } else if (ItemConstants.isTravelingMerchantCSItem(itemID)) {
            chr.getScriptManager().openShop(1011100);
            return;

        } else if (ItemConstants.isPortableStorageCSItem(itemID)) {
            chr.getScriptManager().openTrunk(1012009);
            return;

        } else {
            Equip medal = (Equip) chr.getEquippedInventory().getFirstItemByBodyPart(BodyPart.Medal);
            int medalInt = 0;
            if (medal != null) {
                medalInt = (medal.getAnvilId() == 0 ? medal.getItemId() : medal.getAnvilId()); // Check for Anvilled medal
            }
            String medalString = (medalInt == 0 ? "" : String.format("<%s> ", StringData.getItemStringById(medalInt)));

            switch (itemID) {
                case 5150132://美髮券
                case 5152020:
                case 5153015: {
                    break;
                }//美髮券
                case 5068300:{
                    if (chr.getInventoryByType(CASH).getEmptySlots() >= 1
                            && chr.getInventoryByType(EQUIP).getEmptySlots() >= 1
                            && chr.getInventoryByType(CONSUME).getEmptySlots() >= 1) {
                        Item item13;
                        List<Tuple<Integer, Integer>> list = new ArrayList<>();

                        int[][] NormalItem = {{5068300,1}};
                        int[] HighuerItem = {5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228};
                        int[] UniqueItem = {5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228};

                        boolean bool = (Calendar.getInstance().get(7) == 2);

                        list.add(new Tuple(Integer.valueOf(1), Integer.valueOf(3004)));
                        list.add(new Tuple(Integer.valueOf(2), Integer.valueOf(6000 + (bool ? -500 : 0))));
                        list.add(new Tuple(Integer.valueOf(3), Integer.valueOf(996 + (bool ? 500 : 0))));
                        int itemid = 0;
                        int count = 1;

                        int i8 = GameConstants.getTupleRandom(list);
                        if (i8 == 1) {
                            int rand = Randomizer.rand(0, NormalItem.length -1);
                            itemid = NormalItem[rand][0];
                            count = NormalItem[rand][1];
                        } else if (i8 == 2) {
                            int rand = Randomizer.rand(0, HighuerItem.length - 1);
                            itemid = HighuerItem[rand];
                        } else if (i8 == 3) {
                            int rand = Randomizer.rand(0, UniqueItem.length - 1);
                            itemid = UniqueItem[rand];
                        }
                        Equip equip = ItemData.getEquipDeepCopyFromID(itemid, true);
                        Item getitem = ItemData.getItemDeepCopy(itemid, false);

                        if (equip == null) {
                            getitem.setQuantity(count);
                            chr.addItemToInventory(getitem);
                            c.write(WvsContext.WonderBerry((byte) 1,getitem, item.getItemId()));
                        } else {
                            chr.addItemToInventory(InvType.EQUIP, equip, false);
                            c.write(WvsContext.WonderBerry((byte) 1, equip, item.getItemId()));
                        }

                    }
                    break;
                }//小精靈的奇幻果實
                case ItemConstants.GOLD_APPLE: {// 金蘋果
                    if (chr.getInventoryByType(EQUIP).getEmptySlots() > 0
                            && chr.getInventoryByType(CONSUME).getEmptySlots() >= 2
                            && chr.getInventoryByType(INSTALL).getEmptySlots() > 0
                            && chr.getInventoryByType(ETC).getEmptySlots() > 0) {
                        boolean fromSpecial = Randomizer.isSuccess(ServerConstants.SgoldappleSuc);

                        List<Triple<Integer, Integer, Integer>> list = ServerConstants.goldapple;
                        if (fromSpecial) {
                            list = ServerConstants.Sgoldapple;
                        }

                        int itemid = 0, count = 0;
                        double bestValue = Double.MAX_VALUE;

                        for (Triple<Integer, Integer, Integer> element : list) {
                            double a = (Integer) element.getRight().intValue();
                            double r = a / 10000.0D;
                            double value = -Math.log(Randomizer.nextDouble()) / r;
                            if (value < bestValue) {
                                bestValue = value;
                                itemid = ((Integer) element.getLeft()).intValue();
                                count = ((Integer) element.getMiddle()).intValue();
                            }
                        }

                        if (itemid > 0 && count > 0) {
                            Equip equip = ItemData.getEquipDeepCopyFromID(itemid, true);
                            Item getitem = ItemData.getItemDeepCopy(itemid, false);
                            if (equip == null) {
                                getitem.setQuantity(count);
                                chr.addItemToInventory(getitem);
                                c.write(WvsContext.goldApple(getitem, item));
                                //c.write(UserPacket.effect(Effect.gainQuestItem(getitem.getItemId(), getitem.getQuantity())));
                            } else {
                                chr.addItemToInventory(InvType.EQUIP, equip, false);
                                c.write(WvsContext.goldApple(equip, item));
                                //c.write(UserPacket.effect(Effect.gainQuestItem(equip.getItemId(), equip.getQuantity())));
                            }
                            chr.addItemToInventory(2435458, 1);//蘋果碎片
                            if (fromSpecial) {
                                World world = chr.getClient().getWorld();
                                BroadcastMsg msg = BroadcastMsg.yellowFilled("恭喜玩家" + c.getChr().getName() + "獲得金蘋果大獎:", equip == null ? getitem : equip, true);
                                world.broadcastPacket(WvsContext.broadcastMsg(msg));
                                chr.write(WvsContext.broadcastMsg(msg));
                            }
                        }
                    }
                    break;
                }// 金蘋果
                case ItemConstants.HYPER_TELEPORT_ROCK: {// Hyper Teleport Rock
                    short type = inPacket.decodeShort();
                    if (type == 1) {
                        int fieldId = inPacket.decodeInt();
                        Field field = chr.getOrCreateFieldByCurrentInstanceType(fieldId);
                        if (field == null || (chr.getField().getFieldLimit() & FieldOption.TeleportItemLimit.getVal()) > 0 ||
                                !FieldData.getWorldMapFields().contains(fieldId)) {
                            chr.chatMessage("You may not warp to that map, as you cannot teleport from your current map.");
                            chr.dispose();
                            return;
                        }
                        chr.setInstance(null);
                        chr.warp(field);
                    } else {
                        String targetName = inPacket.decodeString();
                        int worldID = chr.getClient().getChannelInstance().getWorldId().getVal();
                        World world = Server.getInstance().getWorldById(worldID);
                        Char targetChr = world.getCharByName(targetName);

                        // Target doesn't exist
                        if (targetChr == null) {
                            chr.chatMessage(String.format("%s is not online.", targetName));
                            chr.dispose();
                            return;
                        }

                        Position targetPosition = targetChr.getPosition();

                        Field targetField = targetChr.getField();
                        if (targetField == null || (targetField.getFieldLimit() & FieldOption.TeleportItemLimit.getVal()) > 0) {
                            chr.chatMessage("You may not warp to that map, as the targeted map cannot be teleported to.");
                            chr.dispose();
                            return;
                        }
                        // Target is in an instanced Map
                        if (targetChr.getInstance() != null) {
                            chr.chatMessage(String.format("cannot find %s", targetName));
                            // Change channels & warp & teleport
                        } else if (targetChr.getClient().getChannel() != c.getChannel()) {
                            int fieldId = targetChr.getFieldID();
                            chr.setInstance(null);
                            chr.changeChannelAndWarp(targetChr.getClient().getChannel(), fieldId);
                            // warp & teleport
                        } else if (targetChr.getFieldID() != chr.getFieldID()) {
                            chr.setInstance(null);
                            chr.warp(targetField);
                            chr.write(FieldPacket.teleport(targetPosition, chr));
                            // teleport
                        } else {
                            chr.write(FieldPacket.teleport(targetPosition, chr));
                        }
                    }
                    break;
                }// Hyper Teleport Rock
                case ItemConstants.RED_CUBE: // 紅色方塊
                case ItemConstants.BLACK_CUBE: { // 黑色方塊
                    short ePos = (short) inPacket.decodeInt();
                    InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
                    Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
                    if (equip == null) {
                        chr.chatMessage(SystemNotice, "Could not find equip.");
                        chr.dispose();
                        return;
                    } else if (equip.getBaseGrade() < ItemGrade.Rare.getVal()) {
                        String msg = String.format("Character %d tried to use cube (id %d) an equip without a potential (id %d)", chr.getId(), itemID, equip.getItemId());
                        chr.getOffenseManager().addOffense(msg);
                        chr.dispose();
                        return;
                    }
                    Equip oldEquip = equip.deepCopy();
                    int tierUpChance = ItemConstants.getTierUpChance(itemID);
                    short hiddenValue = ItemGrade.getHiddenGradeByVal(equip.getBaseGrade()).getVal();
                    boolean tierUp = !(hiddenValue >= ItemGrade.HiddenLegendary.getVal()) && Util.succeedProp(tierUpChance);
                    if (tierUp) {
                        hiddenValue++;
                    }
                    equip.setHiddenOptionBase(hiddenValue, ItemConstants.THIRD_LINE_CHANCE);
                    equip.releaseOptions(false);
                    if (itemID == ItemConstants.RED_CUBE) {
                        c.write(FieldPacket.redCubeResult(chr.getId(), tierUp, itemID, ePos, item.getQuantity() - 1, equip));
                        c.write(FieldPacket.showItemReleaseEffect(chr.getId(), ePos, false));
                    } else {
                        if (chr.getMemorialCubeInfo() == null) {
                            chr.setMemorialCubeInfo(new MemorialCubeInfo(equip, oldEquip, itemID));
                        }
                        chr.getField().broadcastPacket(UserPacket.showItemMemorialEffect(chr.getId(), true, itemID, ePos, pos));
                        c.write(WvsContext.blackCubeResult(equip, chr.getMemorialCubeInfo()));
                    }
                    equip.updateToChar(chr);
                    break;
                }// 黑色方塊
                case 5062020:{ // 閃炫方塊
                    short ePos = (short) inPacket.decodeInt();
                    InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
                    Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
                    if (equip == null) {
                        chr.chatMessage(SystemNotice, "Could not find equip.");
                        chr.dispose();
                        return;
                    } else if (equip.getBaseGrade() < ItemGrade.Rare.getVal()) {
                        String msg = String.format("Character %d tried to use cube (id %d) an equip without a potential (id %d)", chr.getId(), itemID, equip.getItemId());
                        chr.getOffenseManager().addOffense(msg);
                        chr.dispose();
                        return;
                    }
                    Equip oldEquip = equip.deepCopy();

                    OutPacket outPacket = new OutPacket(OutHeader.UNK_789);
                    outPacket.encodeInt(chr.getId());
                    outPacket.encodeByte(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);
                    outPacket.encodeInt(1);

                    chr.write(outPacket);
                    break;
                }// 閃炫方塊
                case ItemConstants.BONUS_POT_CUBE: // 附加方塊
                case ItemConstants.SPECIAL_BONUS_POT_CUBE: // [MS特價] 大師附加奇幻方塊
                case ItemConstants.WHITE_BONUS_POT_CUBE: {// 白色附加方塊
                    if (c.getWorld().isReboot()) {
                        chr.getOffenseManager().addOffense(String.format("Character %d attempted to use a bonus potential cube in reboot world.", chr.getId()));
                        chr.dispose();
                        return;
                    }
                    short ePos = (short) inPacket.decodeInt();
                    InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
                    Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
                    if (equip == null) {
                        chr.chatMessage(SystemNotice, "Could not find equip.");
                        return;
                    } else if (equip.getBonusGrade() < ItemGrade.Rare.getVal()) {
                        chr.getOffenseManager().addOffense(String.format("Character %d tried to use cube (id %d) an equip without a potential (id %d)", chr.getId(), itemID, equip.getItemId()));
                        chr.dispose();
                        return;
                    }
                    Equip oldEquip = equip.deepCopy();
                    int tierUpChance = ItemConstants.getTierUpChance(itemID);
                    short hiddenValue = ItemGrade.getHiddenGradeByVal(equip.getBonusGrade()).getVal();
                    boolean tierUp = !(hiddenValue >= ItemGrade.HiddenLegendary.getVal()) && Util.succeedProp(tierUpChance);
                    if (tierUp) {
                        hiddenValue++;
                    }
                    equip.setHiddenOptionBonus(hiddenValue, ItemConstants.THIRD_LINE_CHANCE);
                    equip.releaseOptions(true);
                    if (itemID != ItemConstants.WHITE_BONUS_POT_CUBE) {
                        c.write(FieldPacket.bonusCubeResult(chr.getId(), tierUp, itemID, ePos, item.getQuantity() - 1, equip));
                        c.write(FieldPacket.showItemReleaseEffect(chr.getId(), ePos, true));
                    } else {
                        if (chr.getMemorialCubeInfo() == null) {
                            chr.setMemorialCubeInfo(new MemorialCubeInfo(equip, oldEquip, itemID));
                        }
                        chr.getField().broadcastPacket(UserPacket.showItemMemorialEffect(chr.getId(), true, itemID, ePos, pos));
                        c.write(WvsContext.whiteCubeResult(equip, chr.getMemorialCubeInfo()));
                    }
                    equip.updateToChar(chr);
                    break;
                }// 白色附加方塊
                case 5750001: {// Nebulite Diffuser
                    short ePos = inPacket.decodeShort();
                    Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
                    if (equip == null || equip.getSocket(0) == 0 || equip.getSocket(0) == ItemConstants.EMPTY_SOCKET_ID) {
                        chr.chatMessage("That item currently does not have an active socket.");
                        chr.dispose();
                        return;
                    }
                    equip.setSocket(0, ItemConstants.EMPTY_SOCKET_ID);
                    equip.updateToChar(chr);
                    break;
                }//Nebulite Diffuser
                case 5072000: {// 高效能喇叭
                    String text = inPacket.decodeString();
                    boolean whisperIcon = inPacket.decodeByte() != 0;
                    World world = chr.getClient().getWorld();
                    BroadcastMsg smega = BroadcastMsg.megaphone(
                            String.format("%s%s : %s", medalString, chr.getName(), text),
                            (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon, chr);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                }// 高效能喇叭
                case 5076000: {// 道具喇叭
                    String text = inPacket.decodeString();
                    boolean whisperIcon = inPacket.decodeByte() != 0;
                    boolean eqpSelected = inPacket.decodeByte() != 0;
                    InvType invType = EQUIP;
                    int itemPosition = 0;
                    if (eqpSelected) {
                        invType = InvType.getInvTypeByVal(inPacket.decodeInt());
                        itemPosition = inPacket.decodeInt();
                        if (invType == EQUIP && itemPosition < 0) {
                            invType = EQUIPPED;
                        }
                    }
                    Item broadcastedItem = chr.getInventoryByType(invType).getItemBySlot(itemPosition);

                    World world = chr.getClient().getWorld();
                    BroadcastMsg smega = BroadcastMsg.itemMegaphone(String.format("%s%s : %s", medalString, chr.getName(), text),
                            (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon, eqpSelected,
                            broadcastedItem, chr);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                }// 道具喇叭
                case 5077000: { // 三行喇叭
                    byte stringListSize = inPacket.decodeByte();
                    List<String> stringList = new ArrayList<>();
                    for (int i = 0; i < stringListSize; i++) {
                        stringList.add(String.format("%s%s : %s", medalString, chr.getName(), inPacket.decodeString()));
                    }
                    boolean whisperIcon = inPacket.decodeByte() != 0;

                    World world = chr.getClient().getWorld();
                    BroadcastMsg smega = BroadcastMsg.tripleMegaphone(stringList,
                            (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon, chr);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                }// 三行喇叭
                case 5170000: { // 取寵物名
                    int petID = inPacket.decodeInt();
                    inPacket.decodeInt(); //??
                    String petName = inPacket.decodeString();

                    Pet pet = chr.getPetById(petID);

                    if (petName.length() > 13) {
                        chr.chatMessage("no");
                        return;
                    }

                    if (pet != null) {
                        pet.setName(petName);
                        pet.getItem().setName(petName);
                        int idx = pet.getIdx();
                        chr.write(UserLocal.petNameChange(chr, idx, petName));
                    }
                    break;
                }// 取寵物名
                case 5062405: {// 神秘鐵砧
                    int appearancePos = inPacket.decodeInt();
                    int functionPos = inPacket.decodeInt();
                    Inventory inv = chr.getEquipInventory();
                    Equip appearance = (Equip) inv.getItemBySlot(appearancePos);
                    Equip function = (Equip) inv.getItemBySlot(functionPos);
                    if (appearance != null && function != null && appearance.getItemId() / 10000 == function.getItemId() / 10000) {
                        function.getOptions().set(6, appearance.getItemId() % 10000);
                    }
                    function.updateToChar(chr);
                    break;
                }// 神秘鐵砧
                case 5370000:
                case 5370001:
                case 5370002: {//黑板
                    chr.setADBoardRemoteMsg(inPacket.decodeString());
                    break;
                }//黑板
                default: {
                    chr.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("錯誤使用道具ID:" + itemID + "待修復.")));
                    break;
                }//待修復道具
            }
        }
        if (itemID != ItemConstants.HYPER_TELEPORT_ROCK) {
            chr.consumeItem(item);
        }
        chr.dispose();
    }


    @Handler(op = InHeader.USER_ARCANE_SYMBOL_MERGE_REQUEST)//合併ARC
    public static void handleUserArcaneSymbolMergeRequest(Char chr, InPacket inPacket) {
        int type = inPacket.decodeInt();
        int fromPos = inPacket.decodeInt();
        switch (type) {
            case 0:
                // merge
                Equip fromEq = (Equip) chr.getEquipInventory().getItemBySlot(fromPos);
                Equip toEq = (Equip) chr.getEquippedInventory().getItemByItemID(fromEq == null ? 0 : fromEq.getItemId());
                if (fromEq == null || toEq == null || fromEq.getItemId() != toEq.getItemId() || !ItemConstants.isArcaneSymbol(toEq.getItemId())) {
                    chr.chatMessage("Could not find one of the symbols.");
                    return;
                }
                if (toEq.getSymbolExp() >= ItemConstants.getRequiredSymbolExp(toEq.getSymbolLevel())) {
                    chr.chatMessage("Your symbol already is at max exp.");
                    return;
                }
                chr.consumeItem(fromEq);
                toEq.setSymbolExp(Math.min(toEq.getSymbolExp() + fromEq.getSymbolExp(), ItemConstants.getRequiredSymbolExp(toEq.getSymbolLevel())));
                toEq.updateToChar(chr);
                break;
            case 1:
                // enhance
                Equip symbol = (Equip) chr.getEquippedInventory().getItemBySlot(fromPos);
                int reqSymbolExp = ItemConstants.getRequiredSymbolExp(symbol.getLevel());
                if (symbol == null || !ItemConstants.isArcaneSymbol(symbol.getItemId())
                        || symbol.getSymbolLevel() >= ItemConstants.MAX_ARCANE_SYMBOL_LEVEL
                        || symbol.getSymbolExp() < reqSymbolExp) {
                    chr.chatMessage("Could not find symbol.");
                    return;
                }
                long cost = ItemConstants.getSymbolMoneyReqByLevel(symbol.getSymbolLevel());
                if (cost > chr.getMoney()) {
                    chr.chatMessage("You do not have enough mesos to level up your symbol.");
                    return;
                }
                chr.deductMoney(cost);
                symbol.setSymbolLevel((short) (symbol.getSymbolLevel() + 1));
                symbol.addSymbolExp(-reqSymbolExp);
                symbol.initSymbolStats(symbol.getSymbolLevel(), chr.getJob());
                symbol.updateToChar(chr);
                break;
            case 2:
                // mass merge
                int itemId = fromPos;
                toEq = (Equip) chr.getEquippedInventory().getItemByItemID(itemId);
                if (!(toEq instanceof Equip) || !ItemConstants.isArcaneSymbol(itemId)) {
                    chr.chatMessage("Could not find an arcane symbol to transfer to.");
                    return;
                }
                if (toEq.hasSymbolExpForLevelUp()) {
                    chr.chatMessage("First level your symbol before trying to add more symbols onto it.");
                    return;
                }
                Set<Equip> matchingSymbols = new HashSet<>();
                for (Item item : chr.getEquipInventory().getItems()) {
                    if (item.getItemId() == toEq.getItemId()) {
                        matchingSymbols.add((Equip) item);
                    }
                }
                for (Equip eqSymbol : matchingSymbols) {
                    chr.consumeItem(eqSymbol);
                    toEq.addSymbolExp(eqSymbol.getTotalSymbolExp());
                    if (toEq.hasSymbolExpForLevelUp()) {
                        break;
                    }
                }
                toEq.updateToChar(chr);
                break;
        }
    }


    @Handler(op = InHeader.USER_STAT_CHANGE_ITEM_USE_REQUEST)
    public static void handleUserStatChangeItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        if ((field.getFieldLimit() & FieldOption.StatChangeItemConsumeLimit.getVal()) > 0) {
            chr.dispose();
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item == null || item.getItemId() != itemID) {
            return;
        }
        chr.useStatChangeItem(item, true);
    }


    @Handler(op = InHeader.USER_SCRIPT_ITEM_USE_REQUEST)
    public static void handleUserScriptItemUseRequest(Client c, InPacket inPacket) {
        c.verifyTick(inPacket);
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        int quant = inPacket.decodeInt();
        Char chr = c.getChr();
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item == null || item.getItemId() != itemID) {
            item = chr.getCashInventory().getItemBySlot(slot);
        }
        if (item == null || item.getItemId() != itemID || quant < 0 || !chr.isInValidState()) {
            chr.dispose();
            return;
        }
        String script = String.valueOf(itemID);
        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        if (ii.getScript() != null && !"".equals(ii.getScript())) {
            script = ii.getScript();
        }
        if (item.getItemId() == ItemConstants.GAGAGUCCI) {
            int qid = QuestConstants.PVAC_DATA;
            if (chr.getRecordFromQuestEx(qid, "vac") == 0) {
                chr.setQuestRecordEx(qid, "vac", 1);
                chr.chatMessage("Pvac has been enabled");
            } else {
                chr.setQuestRecordEx(qid, "vac", 0);
                chr.chatMessage("Pvac has been disabled");
            }
            chr.dispose();
            return;
        }

        chr.getScriptManager().startScript(itemID, script, ScriptType.Item);
        chr.dispose();
    }



    @Handler(op = InHeader.USER_EQUIPMENT_ENCHANT_WITH_SINGLE_UI_REQUEST)
    public static void handleUserEquipmentEnchantWithSingleUIRequest(Client c, InPacket inPacket) {
        byte equipmentEnchantType = inPacket.decodeByte();

        Char chr = c.getChr();
        EquipmentEnchantType eeType = EquipmentEnchantType.getByVal(equipmentEnchantType);

        if (eeType == null) {
            log.error(String.format("Unknown enchant UI request %d", equipmentEnchantType));
            chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
            return;
        }

        switch (eeType) {
            case ScrollUpgradeRequest:
                inPacket.decodeInt();// tick
                short pos = inPacket.decodeShort();
                int scrollID = inPacket.decodeInt();
                Inventory inv = pos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                pos = (short) Math.abs(pos);
                Equip equip = (Equip) inv.getItemBySlot(pos);
                Equip prevEquip = equip.deepCopy();
                if (equip == null || equip.getBaseStat(tuc) <= 0 || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    chr.getOffenseManager().addOffense(String.format("Character %d tried to enchant a non-scrollable equip (pos %d, itemid %d).",
                            chr.getId(), pos, equip == null ? 0 : equip.getItemId()));
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                List<ScrollUpgradeInfo> suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                if (scrollID < 0 || scrollID >= suis.size()) {
                    chr.getOffenseManager().addOffense(String.format("Characer %d tried to spell trace scroll with an invalid scoll ID (%d, "
                            + "itemID %d).", chr.getId(), scrollID, equip.getItemId()));
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                ScrollUpgradeInfo sui = suis.get(scrollID);
                chr.consumeItem(ItemConstants.SPELL_TRACE_ID, sui.getCost());
                boolean success = sui.applyTo(equip);
                equip.recalcEnchantmentStats();
                String desc = success ? "Your item has been upgraded." : "Your upgrade has failed.";
                chr.write(FieldPacket.showScrollUpgradeResult(false, success ? 1 : 0, desc, prevEquip, equip));
                equip.updateToChar(chr);
                if (equip.getBaseStat(tuc) > 0) {
                    suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                    c.write(FieldPacket.scrollUpgradeDisplay(false, suis));
                }
                break;
            case HyperUpgradeResult:
                inPacket.decodeInt(); //tick
                int eqpPos = inPacket.decodeShort();
                boolean extraChanceFromMiniGame = inPacket.decodeByte() != 0;
                equip = (Equip) chr.getEquipInventory().getItemBySlot(eqpPos);
                if (extraChanceFromMiniGame) {
                    inPacket.decodeInt();
                }
                inPacket.decodeInt();
                inPacket.decodeInt();
                boolean safeGuard = inPacket.decodeByte() != 0;
                boolean equippedInv = eqpPos < 0;
                inv = equippedInv ? chr.getEquippedInventory() : chr.getEquipInventory();
                equip = (Equip) inv.getItemBySlot(Math.abs(eqpPos));
                if (equip == null) {
                    chr.chatMessage("Could not find the given equip.");
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                if (!ItemConstants.isUpgradable(equip.getItemId())
                        || (equip.getBaseStat(tuc) != 0 && !c.getWorld().isReboot())
                        || chr.getEquipInventory().getEmptySlots() == 0
                        || equip.getChuc() >= GameConstants.getMaxStars(equip)
                        || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    chr.chatMessage("Equipment cannot be enhanced.");
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                long cost = GameConstants.getEnchantmentMesoCost(equip.getrLevel() + equip.getiIncReq(), equip.getChuc(), equip.isSuperiorEqp());
                if (chr.getMoney() < cost) {
                    chr.chatMessage("Mesos required: " + NumberFormat.getNumberInstance(Locale.US).format(cost));
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                Equip oldEquip = equip.deepCopy();
                int successProp = GameConstants.getEnchantmentSuccessRate(equip);
                if (extraChanceFromMiniGame) {
                    successProp *= 1.045;
                }
                int destroyProp = safeGuard && equip.canSafeguardHyperUpgrade() ? 0 : GameConstants.getEnchantmentDestroyRate(equip);
                if (equippedInv && destroyProp > 0 && chr.getEquipInventory().getEmptySlots() == 0) {
                    c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You do not have enough space in your "
                            + "equip inventory in case your item gets destroyed.")));
                    return;
                }
                success = Util.succeedProp(successProp, 1000);
                boolean boom = false;
                boolean canDegrade = equip.isSuperiorEqp() ? equip.getChuc() > 0 : equip.getChuc() > 5 && equip.getChuc() % 5 != 0;
                if (success) {
                    equip.setChuc((short) (equip.getChuc() + 1));
                    equip.setDropStreak(0);
                } else if (Util.succeedProp(destroyProp, 1000)) {
                    equip.setChuc((short) 0);
                    equip.makeVestige();
                    boom = true;
                    if (equippedInv) {
                        chr.unequip(equip);
                        equip.setBagIndex(chr.getEquipInventory().getFirstOpenSlot());
                        equip.updateToChar(chr);
                        c.write(WvsContext.inventoryOperation(true, false, Move, (short) eqpPos, (short) equip.getBagIndex(), 0, equip));
                    }
                    if (!equip.isSuperiorEqp()) {
                        equip.setChuc((short) Math.min(12, equip.getChuc()));
                    } else {
                        equip.setChuc((short) 0);
                    }
                } else if (canDegrade) {
                    equip.setChuc((short) (equip.getChuc() - 1));
                    equip.setDropStreak(equip.getDropStreak() + 1);
                }
                chr.deductMoney(cost);
                equip.recalcEnchantmentStats();
                oldEquip.recalcEnchantmentStats();
                equip.updateToChar(chr);
                c.write(FieldPacket.showUpgradeResult(oldEquip, equip, success, boom, canDegrade));
                chr.dispose();
                break;
            case TransmissionResult:
                inPacket.decodeInt(); // tick
                short toPos = inPacket.decodeShort();
                short fromPos = inPacket.decodeShort();
                Equip fromEq = (Equip) chr.getEquipInventory().getItemBySlot(fromPos);
                Equip toEq = (Equip) chr.getEquipInventory().getItemBySlot(toPos);
                if (fromEq == null || toEq == null || fromEq.getItemId() != toEq.getItemId()
                        || !fromEq.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    log.error(String.format("Equip transmission failed: from = %s, to = %s", fromEq, toEq));
                    c.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                fromEq.removeVestige();
                fromEq.setChuc((short) 0);
                chr.consumeItem(toEq);
                fromEq.updateToChar(chr);
                c.write(FieldPacket.showTranmissionResult(fromEq, toEq));
                break;
            case ScrollUpgradeDisplay:
                int ePos = inPacket.decodeInt();
                inv = ePos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                ePos = Math.abs(ePos);
                equip = (Equip) inv.getItemBySlot(ePos);
                if (c.getWorld().isReboot()) {
                    chr.getOffenseManager().addOffense(String.format("Character %d attempted to scroll in reboot world (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.dispose();
                    return;
                }
                if (equip == null || equip.getBaseStat(tuc) <= 0 || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige) || !ItemConstants.isUpgradable(equip.getItemId())) {
                    chr.getOffenseManager().addOffense(String.format("Character %d tried to scroll a non-scrollable equip (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.dispose();
                    return;
                }
                suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                c.write(FieldPacket.scrollUpgradeDisplay(false, suis));
                break;
            /*case ScrollTimerEffective:
             break;*/
            case HyperUpgradeDisplay:
                ePos = inPacket.decodeInt();
                safeGuard = inPacket.decodeByte() != 0;
                inv = ePos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                ePos = Math.abs(ePos);
                equip = (Equip) inv.getItemBySlot(ePos);
                if (equip == null || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige) || !ItemConstants.isUpgradable(equip.getItemId())) {
                    chr.getOffenseManager().addOffense(String.format("Character %d tried to enchant a non-enchantable equip (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                cost = GameConstants.getEnchantmentMesoCost(equip.getrLevel() + equip.getiIncReq(), equip.getChuc(), equip.isSuperiorEqp());
                destroyProp = GameConstants.getEnchantmentDestroyRate(equip);
                if (safeGuard && equip.canSafeguardHyperUpgrade()) {
                    cost *= 2;
                }
                c.write(FieldPacket.hyperUpgradeDisplay(equip, equip.isSuperiorEqp() ? equip.getChuc() > 0 : equip.getChuc() > 5 && equip.getChuc() % 5 != 0,
                        cost, 0, 0, GameConstants.getEnchantmentSuccessRate(equip), 0,
                        destroyProp, 0, equip.getDropStreak() >= 2));
                break;
            case MiniGameDisplay:
                c.write(FieldPacket.miniGameDisplay(eeType));
                break;
            //case ShowScrollUpgradeResult:
            case ScrollTimerEffective:
            case ShowHyperUpgradeResult:
                break;
            /*
             case ShowScrollVestigeCompensationResult:
             case ShowTransmissionResult:
             case ShowUnknownFailResult:
             break;*/
            default:
                log.debug("Unhandled Equipment Enchant Type: " + eeType);
                chr.write(FieldPacket.showUnknownEnchantFailResult((byte) 0));
                break;
        }
    }

    @Handler(op = InHeader.USER_SKILL_LEARN_ITEM_USE_REQUEST)
    public static void handleUserLearnItemUseRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        short pos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Char chr = c.getChr();

        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        if (ii == null || !chr.hasItem(itemID)) {
            chr.chatMessage("Could not find that item.");
            return;
        }
        int masterLevel = ii.getMasterLv();
        int reqSkillLv = ii.getReqSkillLv();
        int skillid = 0;
        Map<ScrollStat, Integer> vals = ii.getScrollStats();
        int chance = vals.getOrDefault(ScrollStat.success, 100);

        for (int skill : ii.getSkills()) {
            if (chr.hasSkill(skill)) {
                skillid = skill;
                break;
            }
        }
        Skill skill = chr.getSkill(skillid);
        if (skill == null) {
            chr.chatMessage(Notice2, "An error has occurred. Mastery Book ID: " + itemID + ",  Skill ID: " + skillid + ".");
            chr.dispose();
            return;
        }
        if (skillid == 0 || (skill.getMasterLevel() >= masterLevel) || skill.getCurrentLevel() < reqSkillLv) {
            chr.chatMessage(SystemNotice, "You cannot use this Mastery Book.");
            chr.dispose();
            return;
        }

        if (skill.getCurrentLevel() > reqSkillLv && skill.getMasterLevel() < masterLevel) {
            chr.chatMessage(Mob, "Success Chance: " + chance + "%.");
            chr.consumeItem(itemID, 1);
            if (Util.succeedProp(chance)) {
                skill.setMasterLevel(masterLevel);
                chr.addSkill(skill);
                chr.write(WvsContext.changeSkillRecordResult(skill));
                chr.chatMessage(Notice2, "[Mastery Book] Item ID: " + itemID + "  set Skill ID: " + skillid + "'s Master Level to: " + masterLevel + ".");
            } else {
                chr.chatMessage(Notice2, "[Mastery Book] Item ID: " + itemID + " was used, however it was unsuccessful.");
            }
        }
        chr.dispose();
    }


    @Handler(op = InHeader.SOCKET_CREATE_REQUEST)
    public static void handleSocketCreateRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (equip == null || item == null || item.getItemId() != itemID) {
            log.error("Unknown equip or mismatching use items.");
            return;
        }
        boolean success = true;
        if (equip.getSocket(0) == ItemConstants.INACTIVE_SOCKET && ItemConstants.canEquipHavePotential(equip)) {
            chr.consumeItem(item);
            equip.setSocket(0, ItemConstants.EMPTY_SOCKET_ID);
        } else {
            success = false;
        }
        c.write(FieldPacket.socketCreateResult(success));
        equip.updateToChar(chr);
    }

    @Handler(op = InHeader.NEBULITE_INSERT_REQUEST)
    public static void handleNebuliteInsertRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short nebPos = inPacket.decodeShort();
        int nebID = inPacket.decodeInt();
        Item item = chr.getInstallInventory().getItemBySlot(nebPos);
        short ePos = inPacket.decodeShort();
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || item.getItemId() != nebID || !ItemConstants.isNebulite(item.getItemId())) {
            log.error("Nebulite or equip was not found when inserting.");
            chr.dispose();
            return;
        }
        if (equip.getSocket(0) != ItemConstants.EMPTY_SOCKET_ID) {
            log.error("Tried to Nebulite an item without an empty socket.");
            chr.chatMessage("You can only insert a Nebulite into empty socket slots.");
            chr.dispose();
            return;
        }
        if (!ItemConstants.nebuliteFitsEquip(equip, item)) {
            chr.getOffenseManager().addOffense(String.format("Character %d attempted to use a nebulite (%d) that doesn't fit an equip (%d).", chr.getId(), item.getItemId(), equip.getItemId()));
            chr.chatMessage("The nebulite cannot be mounted on this equip.");
            chr.dispose();
            return;
        }
        chr.consumeItem(item);
        equip.setSocket(0, nebID % ItemConstants.NEBILITE_BASE_ID);
        equip.updateToChar(chr);
    }

    @Handler(op = InHeader.USER_ITEM_SKILL_SOCKET_UPGRADE_ITEM_USE_REQUEST)
    public static void handleUserItemSkillSocketUpdateItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || !ItemConstants.isWeapon(equip.getItemId())
                || !ItemConstants.isSoulEnchanter(item.getItemId()) || equip.getrLevel() + equip.getiIncReq() < ItemConstants.MIN_LEVEL_FOR_SOUL_SOCKET) {
            chr.dispose();
            return;
        }
        int successProp = ItemData.getItemInfoByID(item.getItemId()).getScrollStats().get(ScrollStat.success);
        boolean success = Util.succeedProp(successProp);
        if (success) {
            equip.setSoulSocketId((short) (item.getItemId() % ItemConstants.SOUL_ENCHANTER_BASE_ID));
            equip.updateToChar(chr);
        }
        chr.getField().broadcastPacket(UserPacket.showItemSkillSocketUpgradeEffect(chr.getId(), success));
        chr.consumeItem(item);
    }

    @Handler(op = InHeader.USER_ITEM_SKILL_OPTION_UPGRADE_ITEM_USE_REQUEST)
    public static void handleUserItemSkillOptionUpdateItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || !ItemConstants.isWeapon(equip.getItemId())
                || !ItemConstants.isSoul(item.getItemId()) || equip.getSoulSocketId() == 0) {
            chr.dispose();
            return;
        }
        equip.setSoulOptionId((short) (1 + item.getItemId() % ItemConstants.SOUL_ITEM_BASE_ID));
        short option = ItemConstants.getSoulOptionFromSoul(item.getItemId());
        if (option == 0) {
            option = (short) ItemConstants.getRandomSoulOption();
        }
        equip.setSoulOption(option);
        equip.updateToChar(chr);
        chr.consumeItem(item);
        chr.getField().broadcastPacket(UserPacket.showItemSkillOptionUpgradeEffect(chr.getId(), true, false, ePos, uPos));
    }

    @Handler(op = InHeader.USER_WEAPON_TEMP_ITEM_OPTION_REQUEST)
    public static void handleUserWeaponTempItemOptionRequest(Char chr, InPacket inPacket) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(CharacterTemporaryStat.SoulMP)
                && tsm.getOption(CharacterTemporaryStat.SoulMP).nOption >= ItemConstants.MAX_SOUL_CAPACITY) {
            Option o = new Option();
            o.xOption = tsm.getOption(CharacterTemporaryStat.SoulMP).xOption;
            o.yOption = tsm.getOption(CharacterTemporaryStat.SoulMP).yOption;
           // o.zOption = ItemConstants.getSoulSkillFromSoulID(
              //      ((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId()

          //  );
            tsm.putCharacterStatValue(CharacterTemporaryStat.FullSoulMP, o);
            tsm.sendSetStatPacket();
            chr.dispose();
        }
        chr.dispose();
    }

    @Handler(op = InHeader.USER_PROTECT_BUFF_DIE_ITEM_REQUEST)
    public static void handleUserProtectBuffDieItemRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        boolean used = inPacket.decodeByte() != 0;
        if (used) {
            // grabs the first one from the list of buffItems
            Item buffProtector = chr.getBuffProtectorItem();
            if (buffProtector != null) {
                chr.setBuffProtector(true);
                chr.consumeItem(buffProtector);
                chr.write(UserLocal.setBuffProtector(buffProtector.getItemId(), true));
            } else {
                chr.getOffenseManager().addOffense(String.format("Character id %d tried to use a buff without having the appropriate item.", chr.getId()));
            }
        }
    }

    @Handler(op = InHeader.USER_DEFAULT_WING_ITEM)
    public static void handleUserDefaultWingItem(Char chr, InPacket inPacket) {
        int wingItem = inPacket.decodeInt();
        if (wingItem == 5010093) { // AB
            chr.getAvatarData().getCharacterStat().setWingItem(wingItem);
            chr.getField().broadcastPacket(UserRemote.setDefaultWingItem(chr));
        }
    }

    @Handler(op = InHeader.USER_RECIPE_OPEN_ITEM_USE_REQUEST)
    public static void handleUserRecipeOpenItemUseRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt();// tick
        short pos = inPacket.decodeShort();// // nPOS
        int itemID = inPacket.decodeInt();// nItemID

        Item item = chr.getInventoryByType(CONSUME).getItemBySlot(pos);
        if (item.getItemId() != itemID) {
            chr.dispose();
            return;
        }
        if (chr != null && chr.getHP() > 0 && ItemConstants.isRecipeOpenItem(itemID)) {
            ItemInfo recipe = ItemData.getItemInfoByID(itemID);
            if (recipe != null) {
                int recipeID = recipe.getSpecStats().getOrDefault(SpecStat.recipe, 0);
                int reqSkillLevel = recipe.getSpecStats().getOrDefault(SpecStat.reqSkillLevel, 0);
                MakingSkillRecipe msr = SkillData.getRecipeById(recipeID);
                if (msr != null && msr.isNeedOpenItem()) {
                    if (chr.getSkillLevel(msr.getReqSkillID()) < reqSkillLevel || chr.getSkillLevel(recipeID) > 0) {
                        return;
                    }
                    chr.addSkill(recipeID, 1, 1);
                }
            }
        }
    }

    @Handler(op = InHeader.USER_ACTIVATE_NICK_ITEM)
    public static void handleUserActivateNickItem(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int nickItem = inPacket.decodeInt();
        chr.setNickItem(nickItem);
        chr.getField().broadcastPacket(UserRemote.setActiveNickItem(chr, null), chr);
    }

//    @Handler(op = InHeader.PREMIUM_BOX_REQUEST)
//    public static void handlePremiumBoxRequest(Client c, InPacket inPacket) {
//        Char chr = c.getChr();
//        short id = inPacket.decodeShort(); // ??
//        int boxId = inPacket.decodeInt(); // box opened to display lol
//
//        OutPacket outPacket = new OutPacket(OutHeader.PREMIUM_SILVER_BOX_RESULT);
//        outPacket.encodeInt(boxId);
//        chr.write(outPacket);
//    }
}
