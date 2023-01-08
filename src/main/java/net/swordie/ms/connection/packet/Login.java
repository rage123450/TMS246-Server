package net.swordie.ms.connection.packet;

import net.swordie.ms.Server;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.ServerStatus;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.LoginType;
import net.swordie.ms.enums.WorldId;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.Channel;
import net.swordie.ms.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by Tim on 2/28/2017.
 */
public class Login {

    private static final Comparator<Char> LOCATION_COMPARATOR = (Char c1, Char c2) -> {
        int res;
        int c1id = c1.getId();
        int c1loc = c1.getLocation();
        int c2id = c2.getId();
        int c2loc = c2.getLocation();
        if (c1loc == c2loc) {
            res = Integer.compare(c1id, c2id);
        } else {
            res = Integer.compare(c1loc, c2loc);
        }
        return res;
    };

    public static OutPacket sendConnect(byte[] siv, byte[] riv) {
        OutPacket oPacket = new OutPacket();

        // version (short) + MapleString (short + char array size) + local IV (int) + remote IV (int) + locale (byte)
        // 0xE
        oPacket.encodeShort(15);
        oPacket.encodeShort(ServerConstants.VERSION);
        oPacket.encodeString(ServerConstants.MINOR_VERSION);
        oPacket.encodeArr(siv);
        oPacket.encodeArr(riv);
        oPacket.encodeByte(ServerConstants.LOCALE);
        oPacket.encodeByte(0);

        return oPacket;
    }

    public static OutPacket sendAliveReq() {
        return new OutPacket(OutHeader.ALIVE_REQ.getValue());
    }

    public static OutPacket sendChannelAliveReq() {
        OutPacket outPacket = new OutPacket(OutHeader.CHANNEL_ALIVE_REQ.getValue());

        outPacket.encodeArr("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 AF F9 08 00 00 00 00 00 00 00 00 00 00 00 00 00");//01 F2 C1 83
                           //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
                           //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 AF F9 08 00 00 00 00 00 00 00 00 00 00 00 00 00
                           //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 4B 5F 18 00 00 00 00 00 CB EA 02 00 00 00 00 00
        return outPacket;
    }

    public static OutPacket sendChannelAliveResponse(int code) {
        OutPacket outPacket = new OutPacket(OutHeader.CHANNEL_ALIVE_RESPONSE.getValue());

        outPacket.encodeByte(code);
        outPacket.encodeArr("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00");

        return outPacket;
    }

    public static OutPacket sendAuthServer(boolean useAuthServer) {
        OutPacket outPacket = new OutPacket(OutHeader.AUTH_SERVER.getValue());
        outPacket.encodeByte(useAuthServer);
        return outPacket;
    }

    public static OutPacket sendHotfix(byte[] data) {
        OutPacket outPacket = new OutPacket(OutHeader.HOTFIX.getValue());

        if (data.length == 0) {
            outPacket.encodeByte(0);
        } else {
            outPacket.encodeArr(Util.toPackedInt(data.length));
            outPacket.encodeInt(data.length); // version maybe?
            outPacket.encodeArr(data);
        }

        return outPacket;
    }

    public static OutPacket checkPasswordResult(boolean success, LoginType msg, User user) {
        OutPacket outPacket = new OutPacket(OutHeader.CHECK_PASSWORD_RESULT.getValue());

        outPacket.encodeByte(msg.getValue());
        outPacket.encodeString("");
        if (success) {
//            outPacket.encodeByte(LoginType.Success.getValue());
//            outPacket.encodeString("");
            outPacket.encodeString(user.getName());
            outPacket.encodeLong(user.getId());
            outPacket.encodeInt(user.getId());
            outPacket.encodeByte(user.getAccountType().getVal()); // GM?

            outPacket.encodeInt(user.getAccountType().getVal()/*0x2880130*/); // GM

            outPacket.encodeInt(0);
            outPacket.encodeInt(0);

            outPacket.encodeInt(0);

            outPacket.encodeByte(user.getpBlockReason());
            outPacket.encodeByte(false);
            outPacket.encodeLong(user.getChatUnblockDate());
            outPacket.encodeByte(0);
            outPacket.encodeLong(user.getChatUnblockDate());
            outPacket.encodeByte(!user.hasCensoredNxLoginID());
            if (user.hasCensoredNxLoginID()) {
                outPacket.encodeString(user.getName()/*user.getCensoredNxLoginID()*/);
            }
            outPacket.encodeString(user.getName());
            JobConstants.encode(outPacket);
            outPacket.encodeByte(user.getGradeCode());
            outPacket.encodeInt(-1);
            outPacket.encodeByte(0); // idk
        } else if (msg == LoginType.Blocked) {
//            outPacket.encodeByte(msg.getValue());
//            outPacket.encodeString("");
            outPacket.encodeInt(0);
            outPacket.encodeByte(0); // nReason
            outPacket.encodeFT(user.getBanExpireDate());
        } else if (msg == LoginType.WaitOTP){
            outPacket.encodeByte(1);
        }else {
//            outPacket.encodeByte(msg.getValue());
//            outPacket.encodeString("");
            outPacket.encodeInt(0);
        }

        return outPacket;
    }

    public static int onlineAmount() {
        int total = 0;
        for (World w : Server.getInstance().getWorlds()) {
            for (Channel c : w.getChannels()) {
                total += c.getChars().size();
            }
        }
        return total;
    }

    public static OutPacket sendWorldInformation(World world, Set<Tuple<Position, String>> stringInfos) {
        // CLogin::OnWorldInformation
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION.getValue());

        outPacket.encodeByte(world.getWorldId().getVal());
        outPacket.encodeString(world.getName());
        outPacket.encodeInt(0);
        outPacket.encodeInt(1);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(world.getWorldState());
        outPacket.encodeString(world.getWorldEventDescription()/* + onlineAmount()*/);
//        outPacket.encodeByte(world.isCharCreateBlock());
        outPacket.encodeByte(world.getChannels().size());
        for (Channel c : world.getChannels()) {
            outPacket.encodeString(c.getName());
            outPacket.encodeInt(c.getGaugePx());
            outPacket.encodeByte(c.getWorldId().getVal());
            outPacket.encodeByte(c.getChannelId());
            outPacket.encodeByte(c.isAdultChannel());
        }
        if (stringInfos == null) {
            outPacket.encodeShort(0);
        } else {
            outPacket.encodeShort(stringInfos.size());
            for (Tuple<Position, String> stringInfo : stringInfos) {
                outPacket.encodePosition(stringInfo.getLeft());
                outPacket.encodeString(stringInfo.getRight());
            }
        }
        outPacket.encodeInt(0); // some offset
        outPacket.encodeByte(false); // connect with star planet stuff, not interested
        return outPacket;
    }

    public static OutPacket sendWorldInformationEnd() {
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION);

        outPacket.encodeByte(-1);
        outPacket.encodeByte(false);
        outPacket.encodeByte(false);
        outPacket.encodeInt(-1);
        outPacket.encodeInt(-1);

        return outPacket;
    }

    public static OutPacket viewChannelResult(LoginType type, int worldId) {
        OutPacket outPacket = new OutPacket(OutHeader.SELECT_WORLD_BUTTON);

        outPacket.encodeByte(type.getValue());
        outPacket.encodeString("");
        outPacket.encodeInt(worldId);
        outPacket.encodeInt(-1);

        return outPacket;
    }

    public static OutPacket sendAccountInfo(User user) {
        OutPacket outPacket = new OutPacket(OutHeader.ACCOUNT_INFO_RESULT);

        outPacket.encodeByte(0); // succeed
        outPacket.encodeString("");

        outPacket.encodeInt(user.getId());
        outPacket.encodeByte(user.getAccountType().getVal());
        outPacket.encodeInt(user.getAccountType().getVal());
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);

        outPacket.encodeByte(user.getpBlockReason());
        outPacket.encodeByte(false);
        outPacket.encodeLong(user.getChatUnblockDate());
        outPacket.encodeByte(0); // 0x40
        outPacket.encodeLong(user.getChatUnblockDate()); // 16 00 00 00 10 01 00 00
        outPacket.encodeString(user.getName()/*user.getCensoredNxLoginID()*/);
        outPacket.encodeString(user.getName()); // 加密過的
        outPacket.encodeString("");
        JobConstants.encode(outPacket);
        outPacket.encodeByte(0); // bIsBeginningUser
        outPacket.encodeInt(-1);
        outPacket.encodeByte(0);

        return outPacket;
    }

    public static OutPacket sendServerStatus(byte worldId) {
        OutPacket outPacket = new OutPacket(OutHeader.SERVER_STATUS.getValue());
        World world = Server.getInstance().getWorldById(worldId);
        if (world != null && !world.isFull()) {
            outPacket.encodeByte(world.getStatus().getValue());
        } else {
            outPacket.encodeByte(ServerStatus.BUSY.getValue());
        }
        outPacket.encodeByte(0); // ?

        return outPacket;
    }

    public static OutPacket SET_CLIENT_KEY(long key) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_CLIENT_KEY.getValue());
        outPacket.encodeLong(key);

        return outPacket;
    }

    public static OutPacket SET_PHYSICAL_WORLD_ID(int worldId) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_PHYSICAL_WORLD_ID.getValue());
        outPacket.encodeInt(worldId);

        return outPacket;
    }

    public static OutPacket selectWorldResult(User user, Account account, byte code, String specialServer,
                                              boolean burningEventBlock) {
        OutPacket outPacket = new OutPacket(OutHeader.SELECT_WORLD_RESULT);

        outPacket.encodeByte(code);
        outPacket.encodeString(specialServer);
        outPacket.encodeByte(0);
        outPacket.encodeInt(1);
        outPacket.encodeInt(1);
        outPacket.encodeInt(1);
        outPacket.encodeInt(account.getTrunk().getSlotCount());
        outPacket.encodeByte(burningEventBlock); // bBurningEventBlock
        outPacket.encodeByte(false); // true -> cant create character
        int reserved = 0;
        outPacket.encodeInt(reserved); // Reserved size
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.ZERO_TIME)); //Reserved timestamp
        for (int i = 0; i < reserved; i++) {
            // not really interested in this
            FileTime ft = FileTime.fromType(FileTime.Type.ZERO_TIME);
            outPacket.encodeInt(ft.getLowDateTime());
            ft.encode(outPacket);
        }
        boolean isEdited = false;
        outPacket.encodeByte(isEdited); // edited characters
        List<Char> chars = new ArrayList<>(account.getCharacters());
        chars.sort(Comparator.comparingInt(Char::getId));
        int orderSize = chars.size();
        outPacket.encodeInt(orderSize);
        for (Char chr : chars) { // 角色位置
            outPacket.encodeInt(chr.getId());
        }
        outPacket.encodeByte(chars.size());
        for(Char chr : chars) {
            chr.getAvatarData().encode(outPacket);
        }
        outPacket.encodeByte(0/*user.getPicStatus().getVal()*/); // bLoginOpt
        outPacket.encodeByte(false); // bQuerySSNOnCreateNewCharacter
        outPacket.encodeByte(0/*1*/);
        outPacket.encodeInt(user.getCharacterSlots());
        outPacket.encodeInt(0); // buying char slots
        outPacket.encodeInt(-1); // nEventNewCharJob
        outPacket.encodeByte(false);
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.ZERO_TIME));
        outPacket.encodeByte(0); // nRenameCount
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(true);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.ZERO_TIME));

        return outPacket;
    }

    public static OutPacket checkDuplicatedIDResult(String name, byte code) {
        OutPacket outPacket = new OutPacket(OutHeader.CHECK_DUPLICATED_ID_RESULT);

        outPacket.encodeString(name);
        outPacket.encodeByte(code);

        return outPacket;
    }

    public static OutPacket createNewCharacterResult(LoginType type, int WorldId, Char c) {
        OutPacket outPacket = new OutPacket(OutHeader.CREATE_NEW_CHARACTER_RESULT);

        outPacket.encodeByte(type.getValue());
        outPacket.encodeInt(WorldId);
        if (type == LoginType.Success) {
            c.getAvatarData().encode(outPacket);
        }
        outPacket.encodeByte(false); // new 199
        outPacket.encodeInt(0);

        return outPacket;
    }

    public static OutPacket sendAuthResponse(int response) {
        OutPacket outPacket = new OutPacket(OutHeader.PRIVATE_SERVER_PACKET);

        outPacket.encodeInt(response);

        return outPacket;
    }

    public static OutPacket sendSecurity(int code) {
        OutPacket outPacket = new OutPacket(OutHeader.SECURITY_PACKET);

        outPacket.encodeByte(code);
        if (code == 7) {
            int a = 49;
            outPacket.encodeInt(a);
            outPacket.encodeArr(new byte[a]);
        } else if (code == 1) {
            outPacket.encodeByte(0);
        }

        return outPacket;
    }

    public static OutPacket selectCharacterResult(LoginType loginType, byte errorCode, int port, int characterId) {
        OutPacket outPacket = new OutPacket(OutHeader.SELECT_CHARACTER_RESULT);

        outPacket.encodeByte(loginType.getValue());
        outPacket.encodeString("");
        outPacket.encodeByte(errorCode);

        if (loginType == LoginType.Success) {
            byte[] server = new byte[]{127, 0, 0, ((byte) 1)};
            outPacket.encodeArr(server);
            outPacket.encodeShort(port);
            outPacket.encodeInt(characterId);

            outPacket.encodeInt(1);
            outPacket.encodeInt(1);
            outPacket.encodeInt(1);

            outPacket.encodeInt(101495092);
            outPacket.encodeByte(0); // bAuthenCode
            outPacket.encodeInt(0); // ulArgument
            outPacket.encodeByte(20);
            outPacket.encodeInt(1000);

//            outPacket.encodeByte(false);
//            outPacket.encodeInt(0);
//            outPacket.encodeString("");
//            outPacket.encodeString("");
//            outPacket.encodeString("");
        }

        return outPacket;
    }

    public static OutPacket sendDeleteCharacterResult(int charId, LoginType loginType) {
        OutPacket outPacket = new OutPacket(OutHeader.DELETE_CHARACTER_RESULT);

        outPacket.encodeInt(charId);
        outPacket.encodeByte(loginType.getValue());
        if (loginType == LoginType.Success) {
            outPacket.encodeLong(0);
            outPacket.encodeLong(0);
        }

        return outPacket;
    }

    public static OutPacket sendAuthInfoRequest() {
        OutPacket outPacket = new OutPacket(OutHeader.AUTH_INFO_REQUEST);

        outPacket.encodeByte(0);
        outPacket.encodeByte(0);

        return outPacket;
    }

    public static OutPacket sendRecommendWorldMessage(WorldId worldId, String msg) {
        OutPacket oPacket = new OutPacket(OutHeader.RECOMMENDED_WORLD_MESSAGE);
        oPacket.encodeByte(1);
        oPacket.encodeInt(worldId.getVal());
        oPacket.encodeString(msg);
        return oPacket;
    }


    public static OutPacket secondPasswordWindows() {
        OutPacket outPacket = new OutPacket(OutHeader.CHECK_SPW_EXIST_RESPONSE);
        outPacket.encodeByte(3);
        outPacket.encodeByte(0);
        return outPacket;
    }

    public static OutPacket RequestSpw() {
        OutPacket outPacket = new OutPacket(OutHeader.REQUEST_SPW);
        outPacket.encodeByte(0);
        return outPacket;
    }

    public static OutPacket ChooseGender() {
        OutPacket outPacket = new OutPacket(OutHeader.CHOOSE_GENDER);
        outPacket.encodeByte(1);
        return outPacket;
    }

    public static OutPacket SetGender(boolean success) {
        OutPacket outPacket = new OutPacket(OutHeader.GENDER_SET);
        outPacket.encodeByte(success);
        return outPacket;
    }
}
