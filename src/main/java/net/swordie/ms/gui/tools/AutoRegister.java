package net.swordie.ms.gui.tools;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.User;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.enums.AccountCreateResult;

public class AutoRegister {
    public static void createAccount(Client c, String name, String pwd) {
        CreateAccountResult acr = CreateAccountResult.Success;
        if (User.getFromDBByName(name) != null) {
            acr = CreateAccountResult.NameInUse;
        } /*else if (Account.getFromDBByIp(c.getIP()) != null) {
            acr = CreateAccountResult.IpAlreadyHasAccount;
        }*/  else if (name.length() < 4 || pwd.length() < 4) {
            acr = CreateAccountResult.Unknown;
        }
        if (acr == CreateAccountResult.Success) {
            User user = new User(name);
            user.setHashedPassword(pwd);
            user.setRegisterIp(c.getIP());
            user.setCharacterSlots(ServerConstants.MAX_CHARACTERS / 2);
            DatabaseManager.saveToDB(user);
            c.write(WvsContext.broadcastMessage(1, "註冊賬號成功。\r\n請重新輸入賬號密碼進入遊戲。"));
        } else if (acr == CreateAccountResult.IpAlreadyHasAccount) {
            c.write(WvsContext.broadcastMessage(1, "每個IP只能註冊一個帳號"));
        } else if (acr == CreateAccountResult.NameInUse) {
            c.write(WvsContext.broadcastMessage(1, "帳號已經被註冊"));
        } else if (acr == CreateAccountResult.Unknown) {
            c.write(WvsContext.broadcastMessage(1, "帳號和密碼長度不足4"));
        }
    }
}

enum CreateAccountResult {
    Success,
    NameInUse,
    IpAlreadyHasAccount,
    Unknown
}
