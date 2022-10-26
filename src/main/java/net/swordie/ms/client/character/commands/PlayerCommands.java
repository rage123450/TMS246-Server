package net.swordie.ms.client.character.commands;


import net.swordie.ms.Server;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.MatrixRecord;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.ChatType;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.VCoreData;
import net.swordie.ms.loaders.containerclasses.VCoreInfo;
import net.swordie.ms.scripts.ScriptManagerImpl;
import net.swordie.ms.scripts.ScriptType;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.swordie.ms.enums.AccountType.Player;
import static net.swordie.ms.enums.ChatType.Expedition;

public class PlayerCommands {
    private static final Logger log = LogManager.getLogger(PlayerCommands.class);

    public static ChatType playerChatType = Expedition;
    public static String playerMsgPrefix = "[System] ";

    @Command(names = { "help" }, requiredType = Player)
    public static class Help extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            for (Class clazz : PlayerCommands.class.getClasses()) {
                Command cmd = (Command) clazz.getAnnotation(Command.class);
                if (chr.getUser().getAccountType().ordinal() >= cmd.requiredType().ordinal()) {
                    StringBuilder str = new StringBuilder(String.format("[%s] ", cmd.requiredType()));
                    String[] names = cmd.names();
                    for (int i = 0; i < names.length; i++) {
                        String cmdName = names[i];
                        str.append(cmdName);
                        if (i != names.length - 1) {
                            str.append(", ");
                        }
                    }
                    if (!cmd.description().isEmpty()) {
                        String description = cmd.description();
                        str.append(": " + description);
                    }
                    chr.chatMessage(Expedition, playerMsgPrefix + str.toString());
                }
            }
        }
    }

    @Command(names = { "sellitem" }, requiredType = Player)
    public static class SellItem extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            ScriptManagerImpl smi = chr.getScriptManager();
            smi.startScript(0, "inv-seller", ScriptType.Npc);
        }
    }

    @Command(names = {"nodes"}, requiredType = Player)
    public static class OpenNodestone extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            int NodeID = 2435719;
            if (!chr.hasItem(NodeID)) {
                chr.chatMessage("You do not currently possess any tradeable Nodestones");
                return;
            }
            Item item = chr.getInventoryByType(ItemData.getItemInfoByID(NodeID).getInvType()).getItemByItemID(NodeID);
            if (item == null) {
                chr.chatMessage("An error has occurred");
                return;
            }
        /*    ScriptManagerImpl sm = chr.getScriptManager();
            for (int z = 0; z < item.getQuantity(); z++) {
                sm.openNodestone(2435719);
            }*/
            for (int z = 0; z < item.getQuantity(); z++) {
                List<VCoreInfo> infos = new ArrayList<>(VCoreData.getPossibilitiesByJob(chr.getJob()));
                int rng = Util.getRandom(99);
                if (rng < GameConstants.NODE_ENFORCE_CHANCE) {
                    infos = infos.stream().filter(VCoreInfo::isEnforce).collect(Collectors.toList());
                } else if (rng - GameConstants.NODE_ENFORCE_CHANCE < GameConstants.NODE_SKILL_CHANCE) {
                    infos = infos.stream().filter(VCoreInfo::isSkill).collect(Collectors.toList());
                } else {
                    infos = infos.stream().filter(VCoreInfo::isSpecial).collect(Collectors.toList());
                }
                MatrixRecord mr = new MatrixRecord();
                for (int i = 0; i < 3; i++) {
                    VCoreInfo vci = Util.getRandomFromCollection(infos);
                    infos.remove(Util.findWithPred(infos, v -> v.getIconID() == vci.getIconID()));
                    switch (i) {
                        case 0:
                            mr.setIconID(vci.getIconID());
                            mr.setMaxLevel(vci.getMaxLevel());
                            mr.setSkillID1(vci.getSkillID());
                            mr.setSlv(1);
                            if (vci.isSoloNode()) {
                                // stop if the info is a solo node (i.e, 5th job skill/5th job special node)
                                i = 3;
                            } else {
                                // if it's not a solo node, reduce the possibilities to the other non-solo nodes
                                infos = infos.stream().filter(v -> !v.isSoloNode()).collect(Collectors.toList());
                            }
                            break;
                        case 1:
                            mr.setSkillID2(vci.getSkillID());
                            break;
                        case 2:
                            mr.setSkillID3(vci.getSkillID());
                            break;
                    }
                }
           /*     mr.create(chr.getId());
                chr.getMatrixRecords().add(mr);
                infos.clear();
                //chr.write(WvsContext.nodestoneOpenResult(mr));*/
                chr.getMatrixRecords().add(mr);
            }
            chr.write(WvsContext.matrixUpdate(chr.getSortedMatrixRecords(), false, 0, 0));
        }
    }

    @Command(names = { "check", "dispose" }, requiredType = Player)
    public static class Check extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            User user = chr.getUser();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            chr.chatMessage(playerChatType, playerMsgPrefix + "DP: " + user.getDonationPoints()
                    + "  VP: " + user.getVotePoints()
                    + "  PQ Points: " + chr.getPQPoints()
                    + "  Dojo Points: " + chr.getDojoPoints());
            chr.chatMessage(playerChatType, playerMsgPrefix + "Exp: " + chr.getTotalStat(BaseStat.expR) + "%"
                    + "  Meso: " + chr.getTotalStat(BaseStat.mesoR) + "%"
                    + "  Drop: " + chr.getTotalStat(BaseStat.dropR) + "%");
            chr.chatMessage(playerChatType, playerMsgPrefix
                    + "Server Time: %s", dateFormat.format(date) );
            ScriptManagerImpl smi = chr.getScriptManager();
            // All but field
            smi.stop(ScriptType.Portal);
            smi.stop(ScriptType.Npc);
            smi.stop(ScriptType.Reactor);
            smi.stop(ScriptType.Quest);
            smi.stop(ScriptType.Item);
            chr.dispose();
        }
    }

    @Command(names = { "stats" }, requiredType = Player)
    public static class Stats extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            chr.chatMessage(playerChatType, playerMsgPrefix + "Str: " + chr.getTotalStat(BaseStat.strR) + "%"
                    + "  Dex: " + chr.getTotalStat(BaseStat.dexR) + "%"
                    + "  Int: " + chr.getTotalStat(BaseStat.intR) + "%"
                    + "  Luk: " + chr.getTotalStat(BaseStat.lukR) + "%");
            chr.chatMessage(playerChatType, playerMsgPrefix + "Att: " + chr.getTotalStat(BaseStat.padR) + "%"
                    + "  Matt: " + chr.getTotalStat(BaseStat.madR) + "%"
                    + "  Att Speed: " + chr.getTotalStat(BaseStat.booster));
        }
    }

    @Command(names = { "mobinfo" }, requiredType = Player)
    public static class MobInfo extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            Rect rect = new Rect(
                    chr.getPosition().deepCopy().getX() - 200,
                    chr.getPosition().deepCopy().getY() - 200,
                    chr.getPosition().deepCopy().getX() + 200,
                    chr.getPosition().deepCopy().getY() + 200
            );
            Mob mob = chr.getField().getMobs().stream().filter(m -> rect.hasPositionInside(m.getPosition())).findFirst().orElse(null);
            Char controller = chr.getField().getLifeToControllers().getOrDefault(mob, null);
            if (mob != null) {
                chr.chatMessage(playerChatType, playerMsgPrefix + String.format("[Mob Info] Level: %d | HP: %s/%s " +
                                        "| MP: %s/%s | PDR: %s | MDR: %s " +
                                        "| Controller: %s | Exp : %s | NX: %s",
                                mob.getLevel(),
                                NumberFormat.getNumberInstance(Locale.US).format(mob.getHp()),
                                NumberFormat.getNumberInstance(Locale.US).format(mob.getMaxHp()),
                                NumberFormat.getNumberInstance(Locale.US).format(mob.getMp()),
                                NumberFormat.getNumberInstance(Locale.US).format(mob.getMaxMp()),
                                mob.getPdr(),
                                mob.getMdr(),
                                controller == null ? "null" : chr.getName(),
                                mob.getForcedMobStat().getExp(),
                                mob.getNxDropAmount()
                        )
                );
            } else {
                chr.chatMessage(playerChatType, playerMsgPrefix + "Could not find mob.");
            }
        }
    }

    @Command(names = { "fifthJob" }, requiredType = Player)
    public static class FifthJob extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            if (chr.getLevel() >= 200 && !chr.getQuestManager().hasQuestCompleted(1460)) {
                chr.getQuestManager().addQuest(1460);
            } else {
                chr.chatMessage(playerChatType, playerMsgPrefix + "You must be at least level 200, and not have 5th job already.");
            }
        }
    }

    @Command(names = { "uptime" }, requiredType = Player)
    public static class UpTime extends PlayerCommand {
        public static void execute(Char chr, String[] args) {
            chr.chatMessage(playerChatType, playerMsgPrefix + "Server is online for %s.", Server.getInstance().getUpTimeToString());
        }
    }

}
