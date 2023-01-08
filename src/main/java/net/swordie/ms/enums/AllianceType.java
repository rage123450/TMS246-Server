package net.swordie.ms.enums;

/**
 * @author Sjonnie
 * Created on 9/1/2018.
 */
public enum AllianceType {
    Req_Create(0),
    Req_Load(1),
    Req_Withdraw(2),
    Req_Invite(3),
    Req_Join(4),
    Req_UpdateMemberCountMax(5),
    Req_Kick(6),
    Req_ChangeMaster(7),
    Req_SetGradeName(8),
    Req_ChangeGrade(9),
    Req_SetNotice(10),
    Req_Destroy(11),
    Req_ChangeName(11), //?

    Res_LoadDone(12), // v248
    Res_LoadGuildDone(13), // v248
    Res_NotifyLoginOrLogout(14), // v248
    Res_CreateDone(15), // v248
    Res_Withdraw_Done(16), // v248
    Res_Withdraw_Failed(17), // v248
    Res_Invite_Done(18), // v248
    Res_Invite_Failed(19), // v248
    Res_InviteGuild_BlockedByOpt(20),
    Res_InviteGuild_AlreadyInvited(21),
    Res_InviteGuild_Rejected(22),
    Res_UpdateAllianceInfo(23),
    Res_ChangeLevelOrJob(24),
    Res_ChangeMaster_Done(25),
    Res_SetGradeName_Done(26),
    Res_ChangeGrade_Done(27),
    Res_ChangeGrade_Fail(28),
    Res_SetNotice_Done(29),
    Res_Destroy_Done(30),
    Res_UpdateGuildInfo(31),
    Res_ChangeName_Done(32),
    Res_ChangeName_Failed(33),
    ;

    private final int val;

    AllianceType(int val) {
        this.val = val;
    }

    public static AllianceType getByVal(int val) {
        if (val >= 0 && val < values().length) {
            return values()[val];
        }
        return null;
    }

    public int getVal() {
        return val;
    }
}
