package net.swordie.ms.handlers;

import net.swordie.ms.Server;
import net.swordie.ms.ServerConfig;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.BroadcastMsg;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.items.Equip;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.jobs.JobManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.Login;
import net.swordie.ms.connection.packet.MapLoadable;
import net.swordie.ms.connection.packet.TestPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.discord.DiscordWebhook;
import net.swordie.ms.enums.CashItemType;
import net.swordie.ms.enums.CharNameResult;
import net.swordie.ms.enums.LoginType;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.util.FileoutputUtil;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.Channel;
import net.swordie.ms.world.World;
import net.swordie.ms.world.field.MapTaggedObject;
import org.apache.log4j.LogManager;
import org.mindrot.jbcrypt.BCrypt;
import org.python.antlr.ast.Str;
import org.python.jline.internal.Log;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static net.swordie.ms.enums.InvType.EQUIPPED;

/**
 * Created on 4/28/2017.
 */
public class LoginHandler {
    private static final org.apache.log4j.Logger log = LogManager.getRootLogger();

    @Handler(op = InHeader.PERMISSION_REQUEST)
    public static void handlePermissionRequest(Client c, InPacket inPacket) {
        byte locale = inPacket.decodeByte();
        short version = inPacket.decodeShort();
        if (locale != ServerConstants.LOCALE || version != ServerConstants.VERSION) {
            log.info(String.format("Client %s has an incorrect version.", c.getIP()));
            c.close();
        }
    }

    @Handler(op = InHeader.USE_AUTH_SERVER)
    public static void handleAuthServer(Client client, InPacket inPacket) {
        client.write(Login.sendAuthServer(false));
    }

    @Handler(op = InHeader.SET_GENDER)
    public static void handleSetGender(Client client, InPacket inPacket) {
        String name = inPacket.decodeString();
        String secpwd = inPacket.decodeString();
        byte gender = inPacket.decodeByte();
        User user = User.getFromDBByName(name);
        if (!user.getName().equals(name) || user.getPic() != null || gender < 0 || gender > 1) {
            client.write(Login.SetGender(false));
            return;
        }
        if (secpwd.length() >= 5) {
            user.setPic(secpwd);
            user.setGender(gender);
            DatabaseManager.saveToDB(user);
            client.write(Login.SetGender(true));
        } else {
            client.write(Login.SetGender(false));
        }
    }

    @Handler(op = InHeader.APPLY_HOTFIX)
    public static void handleApplyHotfix(Client c, InPacket inPacket) {
        File dataWz = new File(ServerConstants.RESOURCES_DIR + "/Data.wz");
        byte[] data = new byte[0];
/*        if (dataWz.exists()) {
            try {
                data = Files.readAllBytes(dataWz.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            c.write(Login.sendHotfix(data));
//        }
    }

    @Handler(op = InHeader.PONG)
    public static void handlePong(Client c, InPacket inPacket) {
    }

    @Handler(op = InHeader.CHECK_LOGIN_AUTH_INFO)
    public static void handleCheckLoginAuthInfo(Client c, InPacket inPacket) {
        byte[] machineID = inPacket.decodeArr(16); // AF 6F 58 97 8C 5F E5 D2 84 09 00 00 00 00 23 BE
        inPacket.decodeArr(6); // 00 00 00 00 02 00
        String username = inPacket.decodeString();
        String password = inPacket.decodeString();
        boolean success;
        LoginType result;
        User user = User.getFromDBByName(username);
        if (user != null) {
            String dbPassword = user.getPassword();
            String dbSecpwd = user.getPic();
            boolean hashed = Util.isStringBCrypt(dbPassword);
            boolean hashedsec = Util.isStringBCrypt(dbSecpwd);
            if (hashed) {
                try {
                    success = BCrypt.checkpw(password, dbPassword);
                } catch (IllegalArgumentException e) { // if password hashing went wrong
                    log.error(String.format("bcrypt check in login has failed! dbPassword: %s; stack trace: %s", dbPassword, e.getStackTrace().toString()));
                    success = false;
                }
            } else {
                success = password.equals(dbPassword);
            }
            result = success ? LoginType.Success : LoginType.IncorrectPassword;
            if (success) {
                if (Server.getInstance().isUserLoggedIn(user)) {
                    success = false;
                    result = LoginType.AlreadyConnected;
                } else if (user.getBanExpireDate() != null && !user.getBanExpireDate().isExpired()) {
                    success = false;
                    result = LoginType.Blocked;
                    String banMsg = String.format("You have been banned. \nReason: %s. \nExpire date: %s",
                            user.getBanReason(), user.getBanExpireDate().toLocalDateTime());
                    c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage(banMsg)));
                } else if (user.getPic() == null || user.getGender() == 10) {
                    success = false;
                    result = LoginType.WaitOTP;
                } else {
                    if (!hashed) {
                        user.setHashedPassword(user.getPassword());
                    }
                    if (!hashedsec) {
                        if (user.getPic() != null && user.getPic().length() >= 6 && !Util.isStringBCrypt(user.getPic())) {
                            user.setPic(BCrypt.hashpw(user.getPic(), BCrypt.gensalt(ServerConstants.BCRYPT_ITERATIONS)));
                        }
                    }
                    Server.getInstance().addUser(user);
                    c.setUser(user);
                    c.setMachineID(machineID);
                    DatabaseManager.saveToDB(user);
                }
            } else {
                result = LoginType.InactivateMember;
                success = false;
            }
        } else {
            result = LoginType.NotRegistered;
            success = false;
        }
        if (result == LoginType.WaitOTP) {
            c.write(Login.ChooseGender());
        } else {
            c.write(Login.checkPasswordResult(success, result, user));
        }
        if (result == LoginType.Success) {
            handleWorldListRequest(c, inPacket);
        }
    }


    @Handler(ops = {InHeader.WORLD_INFO_REQUEST, InHeader.WORLD_LIST_REQUEST})
    public static void handleWorldListRequest(Client c, InPacket inPacket) {
        if (c.getAccount() != null) {
            DatabaseManager.saveToDB(c.getAccount());
            c.setAccount(null);
        }

//        String[] bgs = new String[]{"Adventure", "adventurePathfinder"};
//        String[] bgs = new String[]{"2018halloween", "2018christMas", "SavageT", "adventurePathfinder", "2018MapleTree", "uFarm"};
//        String[] bgs = new String[]{"18thBF"};
        c.write(MapLoadable.setMapTaggedObjectVisisble(Collections.singleton(
                new MapTaggedObject(String.format("%s", Util.getRandomFromCollection(ServerConfig.WolrdSelect_bgs)), true)
        )));
        for (World world : Server.getInstance().getWorlds()) {
            c.write(Login.sendWorldInformation(world, null));
        }
        c.write(Login.sendWorldInformationEnd());
//        c.write(Login.sendRecommendWorldMessage(ServerConfig.WORLD_ID, ServerConfig.RECOMMEND_MSG));
    }

    @Handler(op = InHeader.REQUEST_RELOGIN_COOKIE)
    public static void handleReuestRelogin(Client c, InPacket inPacket) {
        String account = inPacket.decodeString();
        if (c.getChr() == null) { //
            return;
        }
        byte[] token = new byte[216];
        for (int i = 0; i < token.length; i++) {
            token[i] = (byte) Util.getRandom('0', '~');
        }
        Server.getInstance().addAuthToken(token, c.getUser().getId());

        System.out.println("token: " + token);
        OutPacket outPacket = new OutPacket(OutHeader.SET_ACCOUNT_INFO);
        outPacket.encodeShort(token.length);
        outPacket.encodeArr(token);
        c.write(outPacket);
    }

    @Handler(op = InHeader.WVS_CRASH_CALLBACK)
    public static void handleWvsCrashCallback(Client c, InPacket inPacket) {
        /*
        if (c.getChr() != null) {
            c.getChr().setChangingChannel(false);
        }
        */
    }

    @Handler(op = InHeader.SERVER_STATUS_REQUEST)
    public static void handleServerStatusRequest(Client c, InPacket inPacket) {
//        c.write(Login.sendWorldInformation(null));
        c.write(Login.sendWorldInformationEnd());
    }

    @Handler(op = InHeader.WORLD_STATUS_REQUEST)
    public static void handleWorldStatusRequest(Client c, InPacket inPacket) {
        byte worldId = inPacket.decodeByte();
        c.write(Login.sendServerStatus(worldId));
    }

    @Handler(op = InHeader.SELECT_WORLD)
    public static void handleSelectWorld(Client c, InPacket inPacket) {
        byte code = 0; // success code
        String authInfo = null;
        byte[] machineID = new byte[16];

        byte type = inPacket.decodeByte();
        byte worldId = inPacket.decodeByte();
        byte channel = (byte) (inPacket.decodeByte() + 1);
        boolean RE_LOGIN = inPacket.decodeBoolean();
        if (RE_LOGIN) {
            inPacket.decodeByte(); // 01
            authInfo = inPacket.decodeString();
            machineID = inPacket.decodeArr(16);
            inPacket.decodeInt(); // 00 00 00 00
            inPacket.decodeByte(); // 00
        }
        inPacket.decodeInt(); // 192.168.1.102
        inPacket.decodeString(); // CPU
        inPacket.decodeString(); // OS 作業系統

        User user = c.getUser();

        if (RE_LOGIN) {
            System.out.println("auth token info: " + authInfo);
            int userID = Server.getInstance().getUserIdFromAuthToken(authInfo);
            user = User.getFromDBById(userID);
        }

        if (user == null) {
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Cannot find user or your token is expired. restart the client and try again.")));
            return;
        }

        Account acc = user.getAccountByWorldId(worldId);

        if (!Server.getInstance().isOnline()) {
            return;
        }

        if (user.getBanExpireDate() != null && !user.getBanExpireDate().isExpired()) {
            boolean success;
            LoginType result;
            success = false;
            result = LoginType.Blocked;
            String banMsg = String.format("You have been banned. \r\nReason: %s. \r\nExpire date: %s",
                    user.getBanReason(), user.getBanExpireDate().toYYMMDD());
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage(banMsg)));
            return;
        }

        if (acc == null) {
            acc = new Account(user, worldId);
            DatabaseManager.saveToDB(acc); // assign id
            user.addAccount(acc);
            DatabaseManager.saveToDB(user); // add to user's list of accounts
        }

        c.setUser(user);
        c.setAccount(acc);
        acc.setUser(user);
        user.setCurrentAcc(acc);

        c.write(Login.SET_CLIENT_KEY(new SecureRandom().nextLong()));
        if (RE_LOGIN) {
            c.setMachineID(machineID);
            c.write(Login.sendAccountInfo(user));
        }

        c.setWorldId(worldId);
        c.setChannel(channel);

        c.write(Login.SET_PHYSICAL_WORLD_ID(worldId));
        c.write(Login.selectWorldResult(c.getUser(), c.getAccount(), code, Server.getInstance().getWorldById(worldId).isReboot() ? "reboot" : "normal", true));
        Server.getInstance().addUser(user);
    }

    @Handler(op = InHeader.LOGOUT_WORLD)
    public static void handleLogOutWorld(Client c, InPacket inPacket) {
        Server.getInstance().removeUser(c.getUser());
        c.setUser(c.getUser());
//        System.out.println(c.getUser());
    }

    @Handler(op = InHeader.CHECK_DUPLICATE_ID)
    public static void handleCheckDuplicatedID(Client c, InPacket inPacket) {
        String name = inPacket.decodeString();
        CharNameResult code;
        if (!GameConstants.isValidName(name)) {
            code = CharNameResult.Unavailable_Invalid;
        } else {
            code = Char.getFromDBByNameAndWorld(name, c.getAccount().getWorldId()) == null ? CharNameResult.Available : CharNameResult.Unavailable_InUse;
        }
        c.write(Login.checkDuplicatedIDResult(name, code.getVal()));
    }

    @Handler(op = InHeader.CREATE_NEW_CHARACTER)
    public static void handleCreateNewCharacter(Client c, InPacket inPacket) {
        Account acc = c.getAccount();
        String name = inPacket.decodeString();
        int keySettingType = inPacket.decodeInt();
        int eventNewCharSaleJob = inPacket.decodeInt();
        int curSelectedRace = inPacket.decodeInt();
        short curSelectedSubJob = inPacket.decodeShort();
        byte gender = inPacket.decodeByte();
        byte skin = inPacket.decodeByte();
        byte itemLength = inPacket.decodeByte();
        int[] items = new int[itemLength]; //face, hair, markings, skin, overall, top, bottom, cape, boots, weapon
        for (int i = 0; i < itemLength; i++) {
            items[i] = inPacket.decodeInt();
        }
        int face = items[0];
        int hair = items[1];
        CharNameResult code = null;

        if (!ItemData.isStartingItems(items) || skin > ItemConstants.MAX_SKIN || skin < 0
                || face < ItemConstants.MIN_FACE || face > ItemConstants.MAX_FACE
                || hair < ItemConstants.MIN_HAIR || hair > ItemConstants.MAX_HAIR) {
            c.getUser().getOffenseManager().addOffense("Tried to add items unavailable on character creation.");
            code = CharNameResult.Unavailable_CashItem;
        }

        if (!GameConstants.isValidName(name)) {
            code = CharNameResult.Unavailable_Invalid;
        } else if (Char.getFromDBByNameAndWorld(name, acc.getWorldId()) != null) {
            code = CharNameResult.Unavailable_InUse;
        } else if (acc.getCharacters().size() >= acc.getUser().getCharacterSlots()) {
            code = CharNameResult.Unavailable_Invalid;
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You are already at the maximum amount of characters on this world.")));
        }
        if (code != null) {
            c.write(Login.checkDuplicatedIDResult(name, code.getVal()));
            return;
        }

        JobConstants.JobEnum job = JobConstants.LoginJob.getLoginJobById(curSelectedRace).getBeginJob();
        Char chr = new Char(name, keySettingType, eventNewCharSaleJob, job.getJobId(),
                curSelectedSubJob, curSelectedRace, gender, skin, face, hair, items);
        chr.setUserId(acc.getUser().getId());
        JobManager.getJobById(job.getJobId(), chr).setCharCreationStats(chr);
        chr.initFuncKeyMaps(keySettingType, JobConstants.isBeastTamer(chr.getJob()));
        // chr.setFuncKeyMap(FuncKeyMap.getDefaultMapping());
        // default quick slot keys
        chr.setQuickslotKeys(Arrays.asList(42, 82, 71, 73, 29, 83, 79, 81, 2, 3, 4, 5, 16, 17, 18, 19, 6, 7, 8, 9, 20,
                30, 31, 32, 10, 11, 33, 34, 37, 38, 49, 50));
        DatabaseManager.saveToDB(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        if (curSelectedRace == JobConstants.LoginJob.影武者.getJobType()) {
            cs.setSubJob(1);
        }
        cs.setCharacterId(chr.getId());
        cs.setCharacterIdForLog(chr.getId());
        cs.setWorldIdForLog(acc.getWorldId());
        for (Map.Entry<Byte, Integer> entry : chr.getAvatarData().getAvatarLook().getHairEquips().entrySet()) {
            Equip equip = ItemData.getEquipDeepCopyFromID(entry.getValue(), false);
            if (equip != null && equip.getItemId() >= 1000000) {
                equip.setBagIndex(entry.getKey());
                chr.addItemToInventory(EQUIPPED, equip, true);
            }
        }

/*        Equip codex = ItemData.getEquipDeepCopyFromID(1172000, false);
        codex.setInvType(EQUIPPED);
        codex.setBagIndex(BodyPart.MonsterBook.getVal());
        chr.addItemToInventory(EQUIPPED, codex, true);*/

        if (curSelectedRace == 15) { // Zero hack for adding 2nd weapon (removing it in hair equips for zero look)
            Equip equip = ItemData.getEquipDeepCopyFromID(1562000, false);
            equip.setBagIndex(ItemConstants.getBodyPartFromItem(
                    equip.getItemId(), chr.getAvatarData().getAvatarLook().getGender()));
            chr.addItemToInventory(EQUIPPED, equip, true);
        }

        chr.setLocation(ServerConstants.MAX_CHARACTERS); // so new characters are appended to the end
        acc.addCharacter(chr);
        DatabaseManager.saveToDB(acc);
        c.write(Login.createNewCharacterResult(LoginType.Success, acc.getWorldId(), chr));
    }

    @Handler(op = InHeader.DELETE_CHARACTER)
    public static void handleDeleteCharacter(Client c, InPacket inPacket) {
        Account acc = c.getAccount();
        User user = c.getUser();
        if (acc != null) {
            String pic = inPacket.decodeString();
            int charId = inPacket.decodeInt();
            if (!Util.isStringBCrypt(user.getPic())) {
                user.setPic(BCrypt.hashpw(user.getPic(), BCrypt.gensalt(ServerConstants.BCRYPT_ITERATIONS)));
            }
            if (BCrypt.checkpw(pic, user.getPic())) {
                Char chr = acc.getCharById(charId);
                if (chr != null) {
                    acc.removeChar(chr);
                    DatabaseManager.saveToDB(acc);
                    c.write(Login.sendDeleteCharacterResult(charId, LoginType.Success));
                } else {
                    c.write(Login.sendDeleteCharacterResult(charId, LoginType.UnauthorizedUser));
                }
            } else {
                c.write(Login.selectCharacterResult(LoginType.IncorrectPassword, (byte) 0, 0, 0));
            }
        }
    }

    @Handler(op = InHeader.CLIENT_ERROR)
    public static void handleClientError(Client c, InPacket inPacket) {
        c.close();
        if (inPacket.getData().length < 8) {
            log.error(String.format("Error: %s", inPacket));
            return;
        }
        short type = inPacket.decodeShort();
        String type_str = "Unknown?!";
        if (type == 0x01) {
            type_str = "SendBackupPacket";
        } else if (type == 0x02) {
            type_str = "Crash Report";
        } else if (type == 0x03) {
            type_str = "Exception";
        }
        int errortype = inPacket.decodeInt();
        short data_length = inPacket.decodeShort();

        int idk = inPacket.decodeInt(); // D8 BC A9 BC

        short op = inPacket.decodeShort();

        OutHeader opcode = OutHeader.getOutHeaderByOp(op);
        log.error(String.format("[Error %s] (%s / %d) Data: %s", errortype, opcode, op, inPacket));
        if (opcode == OutHeader.REMOTE_SET_TEMPORARY_STAT) {
            inPacket.decodeInt();
        }
        if (opcode == OutHeader.TEMPORARY_STAT_SET || opcode == OutHeader.REMOTE_SET_TEMPORARY_STAT) {
            for (int i = 0; i < CharacterTemporaryStat.length; i++) {
                int mask = inPacket.decodeInt();
                for (CharacterTemporaryStat cts : CharacterTemporaryStat.values()) {
                    if (cts.getPos() == i && (cts.getVal() & mask) != 0) {
                        log.error(String.format("[Error %s] Contained stat %s", errortype, cts));
                    }
                }
            }
        } else if (opcode == OutHeader.CASH_SHOP_CASH_ITEM_RESULT) {
            byte cashType = inPacket.decodeByte();
            CashItemType cit = CashItemType.getResultTypeByVal(cashType);
            log.error(String.format("[Error %s] CashItemType %s", errortype, cit == null ? "Unknown" : cit.toString()));
        }
    }

    @Handler(op = InHeader.WVS_SET_UP_STEP)
    public static void handle_WVS_SET_UP_STEP_Packet(Client c, InPacket inPacket) {
        int load = inPacket.decodeInt();
    }

    @Handler(op = InHeader.PRIVATE_SERVER_PACKET)
    public static void handlePrivateServerPacket(Client c, InPacket inPacket) {
        if (inPacket.getUnreadAmount() == 4) { // hack to ignore another non-game op that throws you a bunch of random bytes
            c.write(Login.sendAuthResponse((OutHeader.PRIVATE_SERVER_PACKET.getValue()) ^ inPacket.decodeShort()));
        }
    }

    @Handler(op = InHeader.SECURITY_PACKET)
    public static void handleSecurityPacket(Client c, InPacket inPacket) {
        int code = inPacket.decodeByte();
/*        if (code == 1 || code == 2) {
            OutPacket outPacket = new OutPacket(OutHeader.SECURITY_PACKET_);
            outPacket.encodeByte(1);
            outPacket.encodeByte(false);
            c.write(outPacket);
        }*/
    }

    @Handler(ops = {InHeader.SELECT_CHARACTER, InHeader.CREATE_CHAR_SELECT})
    public static void handleCharSelect(Client c, InPacket inPacket) {
        if (!Server.getInstance().isOnline()) {
            return;
        }

        int characterId = inPacket.decodeInt();
        boolean 非公開連線 = inPacket.decodeByte() == 1;

        byte worldId = c.getWorldId();
        byte channelId = c.getChannel();

        if (c.getAccount() == null) {
            c.write(Login.checkPasswordResult(false, LoginType.AlreadyConnected, c.getUser()));
            return;
        }
        if (c.getAccount().hasCharacter(characterId)) {
            Channel channel = Server.getInstance().getWorldById(worldId).getChannelById(channelId);
            Server.getInstance().getWorldById(worldId).getChannelById(channelId).addClientInTransfer(channelId, characterId, c);
            c.write(Login.selectCharacterResult(LoginType.Success, (byte) 0, channel.getPort(), characterId));
        } else {
            c.write(Login.selectCharacterResult(LoginType.UnauthorizedUser, (byte) 0, 0, 0));
        }
    }

    @Handler(op = InHeader.CHAR_SELECT_NO_PIC)
    public static void handleCharSelectNoPic(Client c, InPacket inPacket) {
        if (!Server.getInstance().isOnline()) {
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Server is offline.")));
            return;
        }
        inPacket.decodeArr(2);
        int characterId = inPacket.decodeInt();
        String mac = inPacket.decodeString();
        String somethingElse = inPacket.decodeString();
        String pic = BCrypt.hashpw(inPacket.decodeString(), BCrypt.gensalt(ServerConstants.BCRYPT_ITERATIONS));
        c.getUser().setPic(pic);
        // Update in DB
        DatabaseManager.saveToDB(c.getUser());
        if (c.getUser().getCharById(characterId) == null) {
            c.write(Login.selectCharacterResult(LoginType.UnauthorizedUser, (byte) 0, 0, 0));
            return;
        }
        byte worldId = c.getWorldId();
        byte channelId = c.getChannel();
        if (Server.getInstance().isUserLoggedIn(c.getUser())) {
            c.write(Login.checkPasswordResult(false, LoginType.AlreadyConnected, c.getUser()));
            return;
        }
        if (c.getAccount().hasCharacter(characterId)) {
            Channel channel = Server.getInstance().getWorldById(worldId).getChannelById(channelId);
            Server.getInstance().getWorldById(worldId).getChannelById(channelId).addClientInTransfer(channelId, characterId, c);
            c.write(Login.selectCharacterResult(LoginType.Success, (byte) 0, channel.getPort(), characterId));
        } else {
            c.write(Login.selectCharacterResult(LoginType.UnauthorizedUser, (byte) 0, 0, 0));
        }
    }


    @Handler(op = InHeader.CHAR_SELECT)
    public static void handleCharSelect_(Client c, InPacket inPacket) {
        if (!Server.getInstance().isOnline()) {
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Server is offline.")));
            return;
        }
        int characterId = inPacket.decodeInt();
        String name = inPacket.decodeString();
        byte worldId = c.getWorldId();
        byte channelId = c.getChannel();
        if (c.getAccount() == null) {
            c.write(Login.checkPasswordResult(false, LoginType.AlreadyConnected, c.getUser()));
            return;
        }
        Channel channel = Server.getInstance().getWorldById(worldId).getChannelById(channelId);
        if (channel != null) {
            if (c.isAuthorized() && c.getAccount().hasCharacter(characterId)) {
                Server.getInstance().getWorldById(worldId).getChannelById(channelId).addClientInTransfer(channelId, characterId, c);
                c.write(Login.selectCharacterResult(LoginType.Success, (byte) 0, channel.getPort(), characterId));
            }
        } else {
            c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Could not connect to channel, please report this to a GM and try to relog.")));
        }
        // if anything is wrong, the 2nd pwd authorizer should return an error
    }

    @Handler(op = InHeader.CHECK_SPW_REQUEST)
    public static boolean handleCheckSpwRequest(Client c, InPacket inPacket) {
        boolean success = false;
        User user = c.getUser();
        Account account = c.getAccount();
        int picEncodingType = inPacket.decodeInt();
        /* hardcoded 0, could change the encoding of pic in further patches . In 202.2 I think it did some weird things
        per encoding type + length. Got reverted in 202.3 though.
           */
        String pic = inPacket.decodeString();
        int charId = inPacket.decodeInt();
        boolean invisible = inPacket.decodeByte() != 0;
        String mac = inPacket.decodeString();
        String hwid = inPacket.decodeString();
//        int userId = inPacket.decodeInt();
        // after this: 2 strings indicating pc info. Not interested in that rn
        if (!Util.isStringBCrypt(user.getPic())) {
            user.setPic(BCrypt.hashpw(user.getPic(), BCrypt.gensalt(ServerConstants.BCRYPT_ITERATIONS)));
        }
        if (BCrypt.checkpw(pic, user.getPic())) {
            success = true;
        } else {
            c.write(Login.selectCharacterResult(LoginType.IncorrectPassword, (byte) 0, 0, 0));
        }
        c.setAuthorized(success);
        return success;
    }


    @Handler(op = InHeader.CHANGE_PIC_REQUEST)
    public static void handleChangePicRequest(Client c, InPacket inPacket) {
        User user = c.getUser();
        String currentPic = inPacket.decodeString();
        String newPic = inPacket.decodeString();
        if (currentPic.equals(user.getPic())) {
            user.setPic(newPic);
        } else {
            c.write(Login.selectCharacterResult(LoginType.IncorrectPassword, (byte) 0, 0, 0));
        }
    }

    @Handler(op = InHeader.LOGIN_BASIC_INFO)
    public static void handleLoginBasicInfo(Client c, InPacket inPacket) {
        boolean idk = inPacket.decodeByte() != 0;
    }

    @Handler(op = InHeader.GAMESTART_UNK)
    public static void handleRequestHotfix(Client c, InPacket inPacket) {
    }

    @Handler(op = InHeader.AUTH_FAILURE)
    public static void handleAuthFailure(Client c, InPacket inPacket) {
        byte step = inPacket.decodeByte();
        int errorCode = inPacket.decodeInt();
        log.error(String.format("Auth failure! Login step %d, errorCode %d.", step, errorCode));
        byte worldID = 1;
        byte channel = 1;
        byte code = 0;
        c.setWorldId(worldID);
        c.setChannel(channel);
    }

    @Handler(op = InHeader.CHECK_SPW_EXIST_REQUEST)
    public static void handleCheckSndPasswordExistRequest(Client c, InPacket inPacket) {
        c.write(Login.RequestSpw());
        c.write(Login.secondPasswordWindows());
    }

//    @Handler(op = InHeader.EXCEPTION_LOG)
    public static void handleExceptionLog(Client c, InPacket inPacket) {

        inPacket.decodeString_Int();

        String str = inPacket.decodeString();
        String packet = str.split("[]]")[1].substring(12); // skip everything up until the opcode
        byte[] fullPacketArr = Util.getByteArrayByString(packet);
        short op = (short) ((fullPacketArr[0] & 0xFF) + ((fullPacketArr[1] & 0xFF) << 8));
        byte[] packetData = new byte[fullPacketArr.length - 2];
        System.arraycopy(fullPacketArr, 2, packetData, 0, packetData.length);
        OutHeader header = OutHeader.getOutHeaderByOp(op);

        String msg = String.format("Exception log: [%s], %d/0x%X\t| %s\r\n Full String: %s", header, op, op, Util.readableByteArray(packetData), str);
        StringBuilder sb = new StringBuilder();

        if (header == OutHeader.TEMPORARY_STAT_SET || header == OutHeader.REMOTE_SET_TEMPORARY_STAT || header == OutHeader.USER_ENTER_FIELD) {
            inPacket = new InPacket(packetData);
            if (header == OutHeader.REMOTE_SET_TEMPORARY_STAT) {
                inPacket.decodeInt(); // chr id
            } else if (header == OutHeader.USER_ENTER_FIELD) {
                inPacket.decodeInt();
                inPacket.decodeInt();
                inPacket.decodeString();
                inPacket.decodeString(); // parent name, deprecated
                // guild
                inPacket.decodeString();
                inPacket.decodeShort();
                inPacket.decodeByte();
                inPacket.decodeShort();
                inPacket.decodeByte();
                // end guild
                inPacket.decodeByte();
                inPacket.decodeInt();
                inPacket.decodeInt();
                inPacket.decodeInt();
                inPacket.decodeInt();
                inPacket.decodeByte();
            }
            for (int i = 0; i < CharacterTemporaryStat.length; i++) {
                int mask = inPacket.decodeInt();
                for (CharacterTemporaryStat cts : CharacterTemporaryStat.values()) {
                    if (cts.getPos() == i && (cts.getVal() & mask) != 0) {
                        String stat = String.format("Contained stat %s", cts);
                        sb.append(stat);
                        log.error(stat);
                    }
                }
            }
        }

        msg = msg + "\r\n" + sb;
        log.error(msg);
        FileoutputUtil.log("ErrorCodes.txt", msg);

        DiscordWebhook webhook = new DiscordWebhook("");
        webhook.setContent("Exception Encountered \r\nHeader: " + header);
        try {
            webhook.execute(); //Handle exception         
        } catch (IOException ex) {
            log.error(ex);
            //  Logger.getLogger(AdminCommands.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Handler(op = InHeader.VIEW_CHANNEL_REQUEST)
    public static void handleViewChannelRequest(Client c, InPacket inPacket) {
        byte idk = inPacket.decodeByte();
        int worldId = inPacket.decodeInt();
        World world = Server.getInstance().getWorldById(worldId);
        if (world != null) {
            c.write(Login.viewChannelResult(LoginType.Success, worldId));
        } else {
            c.write(Login.viewChannelResult(LoginType.Unknown, worldId));
        }

    }

    @Handler(op = InHeader.CHAR_POSITION_CHANGE)
    public static void handleCharPositionChange(Client c, InPacket inPacket) {
        User user = c.getUser();
        Account acc = c.getAccount();
        if (acc == null) {
            return;
        }
        inPacket.decodeInt(); // accID
        if (inPacket.decodeByte() != 0) {
            int size = inPacket.decodeInt();
            for (int i = 0; i < size; i++) {
                int charId = inPacket.decodeInt();
                Char chr = acc.getCharById(charId);
                if (chr == null) {
                    user.getOffenseManager().addOffense("Tried to change order of a Char that is not linked to their account.");
                } else {
                    chr.setLocation(i);
                }
            }
        }
    }

    @Handler(op = InHeader.ALIVE_ACK)
    public static void handleAliveAck(Client c, InPacket inPacket) {
        if (c.isWaitingForAliveAck()) {
            c.setPing(System.currentTimeMillis() - c.getLastPingTime());
        }
    }

    @Handler(op = InHeader.CHANNEL_ALIVE_ACK)
    public static void handleChannelAliveAck(Client c, InPacket inPacket) {
        if (c.isWaitingForAliveAck()) {
            c.setPing(System.currentTimeMillis() - c.getLastPingTime());
            //c.write(TestPacket.test517());
/*
            c.write(Login.sendChannelAliveResponse(0x11));
            c.write(Login.sendChannelAliveResponse(0x24));
            c.write(Login.sendChannelAliveResponse(0x15));
            c.write(Login.sendChannelAliveResponse(0x0C));
            c.write(Login.sendChannelAliveResponse(0x0D));
            c.write(Login.sendChannelAliveResponse(0x0F));
            c.write(Login.sendChannelAliveResponse(0x10));
            */

        }
    }

    @Handler(op = InHeader.UNK_REQ_2)
    public static void handleUNK_REQ_2(Client c, InPacket inPacket) {
        OutPacket outPacket = new OutPacket(OutHeader.UNK_ACK_2.getValue());
        outPacket.encodeInt(1);
        outPacket.encodeByte(1);

        c.write(outPacket);
    }

    @Handler(op = InHeader.UNK_REQ_1)
    public static void handleUNK_REQ_1(Client c, InPacket inPacket) {
        OutPacket outPacket = new OutPacket(OutHeader.UNK_ACK_1.getValue());
        outPacket.encodeLong(0);
        outPacket.encodeLong(0);

        c.write(outPacket);
    }
}
