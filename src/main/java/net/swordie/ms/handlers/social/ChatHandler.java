package net.swordie.ms.handlers.social;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.chatemotion.ChatEmoticon;
import net.swordie.ms.client.character.chatemotion.ChatEmoticonSaved;
import net.swordie.ms.client.character.commands.*;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.friend.Friend;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.Handler;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.loaders.ChatEmoticonData;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.swordie.ms.enums.ChatType.*;
import static net.swordie.ms.enums.InvType.EQUIP;
import static net.swordie.ms.enums.InvType.EQUIPPED;

public class ChatHandler {

    private static final Logger log = Logger.getLogger(ChatHandler.class);

    @Handler(op = InHeader.USER_CHAT)
    public static void handleUserChat(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        StringBuilder sb = new StringBuilder();
        c.verifyTick(inPacket);
        String msg = inPacket.decodeString();
        byte onlyBalloon = inPacket.decodeByte();
        if (msg.length() > 0) {
            if (msg.charAt(0) == AdminCommand.getPrefix()) {
                boolean executed = false;
                String command = msg.split(" ")[0].replace("!", "");
                for (Class clazz : AdminCommands.class.getClasses()) {
                    Command cmd = (Command) clazz.getAnnotation(Command.class);
                    boolean matchingCommand = false;
                    for (String name : cmd.names()) {
                        if (name.equalsIgnoreCase(command)
                                && chr.getUser().getAccountType().ordinal() >= cmd.requiredType().ordinal()) {
                            matchingCommand = true;
                            break;
                        }
                    }
                    if (matchingCommand) {
                        executed = true;
                        String[] split = null;
                        try {
                            AdminCommand adminCommand = (AdminCommand) clazz.getConstructor().newInstance();
                            Method method = clazz.getDeclaredMethod("execute", Char.class, String[].class);
                            split = msg.split(" ");
                            method.invoke(adminCommand, c.getChr(), split);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                            chr.chatMessage("Exception: " + e.getCause().toString());
                            e.printStackTrace();
                        }
                    }
                }
                if (!executed) {
                    chr.chatMessage(Expedition, "Unknown command \"" + command + "\"");
                }
            } else if (msg.charAt(0) == PlayerCommand.getPrefix()) {
                boolean executed = false;
                String command = msg.split(" ")[0].replace("@", "");
                for (Class clazz : PlayerCommands.class.getClasses()) {
                    Command cmd = (Command) clazz.getAnnotation(Command.class);
                    boolean matchingCommand = false;
                    for (String name : cmd.names()) {
                        if (name.equalsIgnoreCase(command)
                                && chr.getUser().getAccountType().ordinal() >= cmd.requiredType().ordinal()) {
                            matchingCommand = true;
                            break;
                        }
                    }
                    if (matchingCommand) {
                        executed = true;
                        String[] split = null;
                        try {
                            PlayerCommand playerCommand = (PlayerCommand) clazz.getConstructor().newInstance();
                            Method method = clazz.getDeclaredMethod("execute", Char.class, String[].class);
                            split = msg.split(" ");
                            method.invoke(playerCommand, c.getChr(), split);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                            chr.chatMessage("Exception: " + e.getCause().toString());
                            e.printStackTrace();
                        }
                    }
                }
                if (!executed) {
                    chr.chatMessage(Expedition, "Unknown command \"" + command + "\"");
                }
            } else {
                ChatType chatType = Normal;
                String tag = "";
                if (chr.getEquippedInventory().hasItem(1115012) && chr.getEquippedInventory().hasItem(1115100)) {
                    chatType = Expedition;
                    tag = "[Player] ";
                }
                if (chr.getEquippedInventory().hasItem(1115013) && chr.getEquippedInventory().hasItem(1115101)) {
                    chatType = Expedition;
                    tag = "[Donor] ";
                }
                if (chr.getEquippedInventory().hasItem(1115014) && chr.getEquippedInventory().hasItem(1115102)) {
                    chatType = Expedition;
                    tag = "[VIP] ";
                }

                if (chr.getUser().getAccountType().ordinal() >= AccountType.GameMaster.ordinal()) {
                    chr.getField().broadcastPacket(UserPacket.chat(chr, tag, ChatUserType.Admin, msg,
                            onlyBalloon, 0, c.getWorldId(), false));
                } else {
                    chr.getField().broadcastPacket(UserPacket.chat(chr, tag, ChatUserType.User, msg,
                            onlyBalloon, 0, c.getWorldId(), false));
                }
                //chr.getField().broadcastPacket(UserLocal.chatMsg(chatType, tag + chr.getName() + ": " + msg));

            }
        }
    }

    @Handler(op = InHeader.USER_ITEM_LINKED_CHAT)
    public static void handleUserItemLinkedChat(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        String msg = inPacket.decodeString();
        byte type = inPacket.decodeByte();
        inPacket.decodeInt();
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeInt());
        int pos = inPacket.decodeInt();
        if (invType == EQUIP && pos < 0) {
            invType = EQUIPPED;
            pos = -pos;
        }
        Item item = chr.getInventoryByType(invType).getItemBySlot(pos);
        if (item == null) {
            chr.chatMessage("Could not find that item.");
            return;
        }
        chr.getField().broadcastPacket(UserPacket.itemLinkedChat(chr, ChatUserType.User, msg,
                3, 0, chr.getClient().getWorldId(), item, false));
    }

    @Handler(op = InHeader.USER_AD_BOARD_CLOSE)
    public static void handleUserAdBoardClose(Char chr, InPacket inPacket){
        chr.setADBoardRemoteMsg(null);
    }

    @Handler(op = InHeader.WHISPER)
    public static void handleWhisper(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        inPacket.decodeInt(); // tick
        String destName = inPacket.decodeString();
        Char dest = c.getWorld().getCharByName(destName);
        if (dest == null) {
            chr.chatMessage("Character not found.");
            return;
        }
        switch (type) {
            case 5: // /find command
                int fieldId = dest.getField().getId();
                int channel = dest.getClient().getChannel();
                if (channel != chr.getClient().getChannel()) {
                    chr.chatMessage("%s is in channel %s-%d.", dest.getName(), dest.getWorld().getName(), channel);
                } else {
                    String fieldString = StringData.getMapStringById(fieldId);
                    if (fieldString == null) {
                        fieldString = "Unknown field.";
                    }
                    chr.chatMessage("%s is at %s.", dest.getName(), fieldString);
                }
                break;
            case 68:
                break;
            case 6: // whisper
                String msg = inPacket.decodeString();
                dest.write(FieldPacket.whisper(chr, (byte) (c.getChannel() - 1), false, msg, false));
                chr.chatMessage(Whisper, String.format("%s<< %s", dest.getName(), msg));
                break;
        }

    }

    @Handler(op = InHeader.WHISPER_ITEM)
    public static void handleWhisper_Item(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        inPacket.decodeInt(); // tick
        String destName = inPacket.decodeString();
        Char dest = c.getWorld().getCharByName(destName);
        if (dest == null) {
            chr.chatMessage("Character not found.");
            return;
        }
        switch (type) {
            case 5: // /find command
                int fieldId = dest.getField().getId();
                int channel = dest.getClient().getChannel();
                if (channel != chr.getClient().getChannel()) {
                    chr.chatMessage("%s is in channel %s-%d.", dest.getName(), dest.getWorld().getName(), channel);
                } else {
                    String fieldString = StringData.getMapStringById(fieldId);
                    if (fieldString == null) {
                        fieldString = "Unknown field.";
                    }
                    chr.chatMessage("%s is at %s.", dest.getName(), fieldString);
                }
                break;
            case 68:
                break;
            case 6: // whisper
                String msg = inPacket.decodeString();
                dest.write(FieldPacket.whisper(chr, (byte) (c.getChannel() - 1), false, msg, false));
                chr.chatMessage(Whisper, String.format("%s<< %s", dest.getName(), msg));
                break;
        }

    }

    @Handler(op = InHeader.GROUP_MESSAGE)
    public static void handleGroupMessage(Char chr, InPacket inPacket) {
        byte type = inPacket.decodeByte(); // party = 1, alliance = 3
        byte idk2 = inPacket.decodeByte();
        int idk3 = inPacket.decodeInt(); // party id?
        String msg = inPacket.decodeString();
        if (msg.length() > 1000 || !Util.isValidString(msg)) {
            return;
        }
        switch (type) {
            case 0: // buddy
                for (Friend friend : chr.getOnlineFriends()) {
                    friend.write(FieldPacket.groupMessage(GroupMessageType.Buddy, chr, msg));
                }
                break;
            case 1: // party
                if (chr.getParty() != null) {
                    chr.getParty().broadcast(FieldPacket.groupMessage(GroupMessageType.Party, chr, msg), chr);
                }
                break;
            case 2: // guild
                if (chr.getGuild() != null) {
                    chr.getGuild().broadcast(FieldPacket.groupMessage(GroupMessageType.Guild, chr, msg), chr);
                }
                break;
            case 3: // alliance
                if (chr.getGuild() != null && chr.getGuild().getAlliance() != null) {
                    chr.getGuild().getAlliance().broadcast(FieldPacket.groupMessage(GroupMessageType.Alliance, chr, msg), chr);
                }
                break;
            default:
                log.error("Unhandled group message type " + type);
        }
    }

    @Handler(op = InHeader.ITEM_LINKED_GROUP_MESSAGE)
    public static void handleItemLinkedGroupMessage(Char chr, InPacket inPacket) {
        byte type = inPacket.decodeByte(); // party = 1, alliance = 3
        byte idk2 = inPacket.decodeByte();
        int accId = inPacket.decodeInt();
        int charId = inPacket.decodeInt();
        String msg = inPacket.decodeString();
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeInt());
        int pos = inPacket.decodeInt();
        if (invType == EQUIP && pos < 0) {
            invType = EQUIPPED;
            pos = -pos;
        }
        Item item = chr.getInventoryByType(invType).getItemBySlot(pos);
        if (item == null) {
            chr.chatMessage("Could not find that item.");
            return;
        }
        switch (type) {
            case 0: // buddy
                for (Friend friend : chr.getOnlineFriends()) {
                    friend.write(FieldPacket.itemLinkedGroupMessage(GroupMessageType.Buddy, chr, msg, item));
                }
                break;
            case 1: // party
                if (chr.getParty() != null) {
                    chr.getParty().broadcast(FieldPacket.itemLinkedGroupMessage(GroupMessageType.Party, chr, msg, item), chr);
                }
                break;
            case 2: // guild
                if (chr.getGuild() != null) {
                    chr.getGuild().broadcast(FieldPacket.itemLinkedGroupMessage(GroupMessageType.Guild, chr, msg, item), chr);
                }
                break;
            case 3: // alliance
                if (chr.getGuild() != null && chr.getGuild().getAlliance() != null) {
                    chr.getGuild().getAlliance().broadcast(FieldPacket.itemLinkedGroupMessage(GroupMessageType.Alliance, chr, msg, item), chr);
                }
                break;
            default:
                log.error("Unhandled group message type " + type);
        }
    }

    @Handler(op = InHeader.CHAT_EMOTICON)
    public static void handleChatEmoticon(Char chr, InPacket inPacket) {
        short slot1, s1;
        int emoticon;
        short slot, slot2, s2;
        String str;
        ChatEmoticonSaved em;
        byte type = inPacket.decodeByte();
        switch (type) {
            case 1:
            case 9:
                slot1 = inPacket.decodeShort();
                slot2 = inPacket.decodeShort();
                chr.write(WvsContext.getChatEmoticon(type, slot1, slot2, 0, ""));
                break;
            case 2:
                s1 = inPacket.decodeShort();
                chr.getEmoticonTabs().remove(s1 - 1);
                chr.write(WvsContext.getChatEmoticon(type, s1, (short) 0, 0, ""));
                break;
            case 5:
                emoticon = inPacket.decodeInt();
                s2 = chr.getEmoticonFreeSlot();
                chr.getEmoticonBookMarks().add(new Tuple<>(Integer.valueOf(emoticon), Short.valueOf(s2)));
                for (ChatEmoticon a : chr.getEmoticonTabs()) {
                    if (emoticon / 10000 == a.getEmoticonid()) {
                        a.getBookmarks().add(new Tuple<>(Integer.valueOf(emoticon), Short.valueOf(s2)));
                        break;
                    }
                }
                chr.write(WvsContext.getChatEmoticon(type, s2, (short) 0, emoticon, ""));
                break;
            case 6:
                emoticon = inPacket.decodeInt();
                for (Tuple<Integer, Short> a : chr.getEmoticonBookMarks()) {
                    if (((Integer) a.getLeft()).intValue() == emoticon) {
                        chr.getEmoticonBookMarks().remove(a);
                        break;
                    }
                }
                label44:
                for (ChatEmoticon a : chr.getEmoticonTabs()) {
                    for (Tuple<Integer, Short> b : a.getBookmarks()) {
                        if (((Integer) b.getLeft()).intValue() == emoticon) {
                            a.getBookmarks().remove(b);
                            break label44;
                        }
                    }
                }
                chr.write(WvsContext.getChatEmoticon(type, (short) 0, (short) 0, emoticon, ""));
                break;
            case 8:
                emoticon = inPacket.decodeInt();
                str = inPacket.decodeString();
                em = new ChatEmoticonSaved(chr.getId(), emoticon, str);
                chr.getSavedEmoticon().add(em);
                chr.write(WvsContext.getChatEmoticon(type, (short) chr.getSavedEmoticon().size(), (short) 0, emoticon, str));
                break;
            case 10:
                slot = inPacket.decodeShort();
                chr.getSavedEmoticon().remove(slot - 1);
                chr.write(WvsContext.getChatEmoticon(type, slot, (short) 0, 0, ""));
                break;
        }
        chr.getEmoticons().clear();
        ChatEmoticonData.LoadChatEmoticons(chr, chr.getEmoticonTabs());
    }

}
