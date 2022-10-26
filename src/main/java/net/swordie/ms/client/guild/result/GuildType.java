package net.swordie.ms.client.guild.result;

import net.swordie.ms.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created on 3/21/2018.
 */
public enum GuildType {
    Req_LoadGuild(0),
    Req_FindGuildByCid(1),
    Req_FindGuildByGID(2),
    Req_InputGuildName(3),
    Req_CheckGuildName(4),
    Req_CreateGuildAgree(5),
    Req_CreateNewGuild(6),

    Req_InviteGuild(7),
    Req_JoinGuild(8),
    Req_JoinGuildDirect(9),
    Req_UpdateJoinState(10),
    Req_WithdrawGuild(11),
    Req_KickGuild(12),
    Req_RemoveGuild(13),
    Req_IncMaxMemberNum(14),

    Req_ChangeLevel(15),
    Req_ChangeJob(16),
    Req_SetGuildName(17),
    Req_SetGradeName(18),
    Req_SetMemberGrade(19),
    Req_SetMark(20),
    Req_SetNotice(21),
    Req_InputMark(22),

    Req_CheckQuestWaiting(23),
    Req_CheckQuestWaiting2(24),
    Req_InsertQuestWaiting(25),
    Req_CancelQuestWaiting(26),
    Req_RemoveQuestCompleteGuild(27),

    Req_IncPoint(28),
    Req_IncCommitment(29),
    Req_DecGGP(30),
    Req_DecIGP(31),

    Req_SetQuestTime(32),
    Req_ShowGuildRanking(33),

    Req_SetSkill(34),
    Req_SkillLevelSetUp(35),
    Req_ResetGuildBattleSkill(36),
    Req_UseActiveSkill(37),
    Req_UseADGuildSkill(38),
    Req_ExtendSkill(39),
    Req_ChangeGuildMaster(40),
    Req_FromGuildMember_GuildSkillUse(41),

    Req_SetGGP(42),
    Req_SetIGP(43),

    Req_BattleSkillOpen(44),
    Req_Search(45),

    Req_CreateNewGuild_Block(46),
    Req_CreateNewAlliance_Block(47),

    // Results ------------------------------
    Res_LoadGuild_Done(48),
    Res_FindGuild_Done(49),

    Res_CheckGuildName_Available(50),
    Res_CheckGuildName_AlreadyUsed(51),
    Res_CheckGuildName_Unknown(52),

    Res_CreateGuildAgree_Reply(53),
    Res_CreateGuildAgree_Unknown(54),
    Res_CreateNewGuild_Done(55),
    Res_CreateNewGuild_AlreadyJoined(56),
    Res_CreateNewGuild_GuildNameAlreayExist(57),
    Res_CreateNewGuild_Beginner(58),
    Res_CreateNewGuild_Disagree(59),
    Res_CreateNewGuild_NotFullParty(60),
    Res_CreateNewGuild_Unknown(61),

    Res_JoinGuild_Done(62),
    Res_JoinGuild_AlreadyJoined(63),
    Res_JoinGuild_AlreadyJoinedToUser(64),
    Res_JoinGuild_AlreadyFull(65),
    Res_JoinGuild_LimitRequest(66),
    Res_JoinGuild_UnknownUser(67),
    Res_JoinGuild_NonRequestFindUser(68),
    Res_JoinGuild_Unknown(69),

    Res_JoinRequest_Done(70),
    Res_JoinRequest_DoneToUser(71),
    Res_JoinRequest_AlreadyFull(72),
    Res_JoinRequest_LimitTime(73),
    Res_JoinRequest_Unknown(74),
    Res_JoinCancelRequest_Done(75),

    Res_WithdrawGuild_Done(76),
    Res_WithdrawGuild_NotJoined(77),
    Res_WithdrawGuild_Unknown(78),

    Res_KickGuild_Done(79),
    Res_KickGuild_NotJoined(80),
    Res_KickGuild_Unknown(81),

    Res_RemoveGuild_Done(82),
    Res_RemoveGuild_NotExist(83),
    Res_RemoveGuild_Unknown(84),
    Res_RemoveRequestGuild_Done(85),

    Res_InviteGuild_BlockedUser(86),
    Res_InviteGuild_BlockedRequests(87),
    Res_InviteGuild_AlreadyInvited(88),
    Res_InviteGuild_Rejected(89),

    // Bunch of Create/Join messages, copies of above?

    Res_AdminCannotCreate(97),
    Res_AdminCannotInvite(98),

    Res_IncMaxMemberNum_Done(99),
    Res_IncMaxMemberNum_Unknown(100),

    Res_ChangeMemberName(101),
    Res_ChangeRequestUserName(102),
    Res_ChangeLevelOrJob(103),
    Res_NotifyLoginOrLogout(104),
    Res_SetGradeName_Done(105),
    Res_SetGradeName_Unknown(106),
    Res_SetMemberGrade_Done(107),
    Res_SetMemberGrade_Unknown(108),
    Res_SetMemberCommitment_Done(109),
    Res_SetMark_Done(110),
    Res_SetMark_Unknown(111),
    Res_SetNotice_Done(112),

    Res_InsertQuest(113),
    Res_NoticeQuestWaitingOrder(114),
    Res_SetGuildCanEnterQuest(115),

    Res_IncPoint_Done(116),
    Res_ShowGuildRanking(117),
    Res_SetGGP_Done(118),
    Res_SetIGP_Done(119),

    Res_GuildQuest_NotEnoughUser(120),
    Res_GuildQuest_RegisterDisconnected(121),
    Res_GuildQuest_NoticeOrder(122),

    Res_Authkey_Update(123),
    Res_SetSkill_Done(124),
    Res_SetSkill_Extend_Unknown(125),
    Res_SetSkill_LevelSet_Unknown(126),
    Res_SetSkill_ResetBattleSkill(127),

    Res_UseSkill_Success(128),
    Res_UseSkill_Err(129),

    Res_ChangeName_Done(130),
    Res_ChangeName_Unknown(131),
    Res_ChangeMaster_Done(132),
    Res_ChangeMaster_Unknown(133),

    Res_BlockedBehaviorCreate(134),
    Res_BlockedBehaviorJoin(135),
    Res_BattleSkillOpen(136),
    Res_GetData(137),
    Res_Rank_Reflash(138),
    Res_FindGuild_Error(139),
    Res_ChangeMaster_Pinkbean(140),

    No(-1),
    ;

    private byte val;

    GuildType(int val) {
        this.val = (byte) val;
    }

    public static GuildType getTypeByVal(byte val) {
        return Arrays.stream(values()).filter(grt -> grt.getVal() == val).findAny().orElse(null);
    }

    public byte getVal() {
        return val;
    }

    public static void main(String[] args) {
        File file = new File("D:\\SwordieMS\\SwordieUTD\\src\\main\\java\\net\\swordie\\ms\\client\\guild\\result\\GuildType.java");
        int change = -1;
        try(Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains(",") && line.contains("(")) {
                    String[] split = line.split("[()]");
                    String name = split[0];
                    if (!Util.isNumber(split[1])) {
                        System.out.println(line);
                        continue;
                    }
                    int val = Integer.parseInt(split[1]);
                    GuildType oh = Arrays.stream(GuildType.values()).filter(o -> o.toString().equals(name.trim())).findFirst().orElse(null);
                    if (oh != null) {
                        if (oh.ordinal() >= Res_AdminCannotCreate.ordinal() && oh.ordinal() < No.ordinal()) {
                            val += change;
                            System.out.println(String.format("%s(%d),", name, val));
                        } else {
                            System.out.println(line);
                        }
                    }
                } else {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}