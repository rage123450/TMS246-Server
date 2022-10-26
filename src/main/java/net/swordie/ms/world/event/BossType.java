package net.swordie.ms.world.event;

import net.swordie.ms.constants.BossConstants;

public enum BossType {

    EasyBalrog(BossConstants.BALROG_EASY_BATTLE_MAP, BossConstants.BALROG_ENTRY_MAP, BossConstants.BALROG_ENTRY_MAP, BossConstants.BALROG_TIME_LIMIT / 2), // 10 min
    NormalBalrog(BossConstants.BALROG_HARD_BATTLE_MAP, BossConstants.BALROG_ENTRY_MAP, BossConstants.BALROG_ENTRY_MAP, BossConstants.BALROG_TIME_LIMIT), // 20 min

    EasyZakum(BossConstants.ZAKUM_EASY_ALTAR, BossConstants.ZAKUM_EASY_ENTRANCE, BossConstants.ZAKUM_DOOR_TO_ENTRANCE, BossConstants.ZAKUM_TIME_BASE), // 15 min
    NormalZakum(BossConstants.ZAKUM_HARD_ALTAR, BossConstants.ZAKUM_HARD_ENTRANCE, BossConstants.ZAKUM_DOOR_TO_ENTRANCE, BossConstants.ZAKUM_TIME_BASE * 2), // 30 min
    ChaosZakum(BossConstants.ZAKUM_CHAOS_ALTAR, BossConstants.ZAKUM_CHAOS_ENTRANCE, BossConstants.ZAKUM_DOOR_TO_ENTRANCE, BossConstants.ZAKUM_TIME_BASE * 3), // 45 min

    EasyHorntail(BossConstants.HORNTAIL_EASY_START, BossConstants.HORNTAIL_ENTRANCE, BossConstants.HORNTAIL_WARPOUT, BossConstants.HORNTAIL_EASY_TIME),
    NormalHorntail(BossConstants.HORNTAIL_NORMAL_START, BossConstants.HORNTAIL_ENTRANCE, BossConstants.HORNTAIL_WARPOUT, BossConstants.HORNTAIL_NORMAL_TIME),
    ChaosHorntail(BossConstants.HORNTAIL_CHAOS_START, BossConstants.HORNTAIL_ENTRANCE, BossConstants.HORNTAIL_WARPOUT, BossConstants.HORNTAIL_CHAOS_TIME),

    PinkBean(BossConstants.PINKBEAN_NORMAL_MAP, BossConstants.PINKBEAN_NORMAL_LOBBY, BossConstants.PINKBEAN_EXIT_MAP, BossConstants.PINKBEAN_NORMAL_TIME),
    ChaosPinkBean(BossConstants.PINKBEAN_CHAOS_MAP, BossConstants.PINKBEAN_CHAOS_LOBBY, BossConstants.PINKBEAN_EXIT_MAP, BossConstants.PINKBEAN_CHAOS_TIME),

    EasyVonLeon(BossConstants.VON_LEON_EASY_MAP, BossConstants.VON_LEON_LOBBY, BossConstants.VON_LEON_LOBBY, BossConstants.VON_LEON_TIME_BASE), // 20 min
    NormalVonLeon(BossConstants.VON_LEON_NORMAL_MAP, BossConstants.VON_LEON_LOBBY, BossConstants.VON_LEON_LOBBY, BossConstants.VON_LEON_TIME_BASE * 2), // 40 min

    NormalHilla(BossConstants.HILLA_NORMAL_WARPIN, BossConstants.HILLA_LOBBY, BossConstants.HILLA_WARPOUT, BossConstants.HILLA_NORMAL_TIME),
    HardHilla(BossConstants.HILLA_HARD_WARPIN, BossConstants.HILLA_LOBBY, BossConstants.HILLA_WARPOUT, BossConstants.HILLA_HARD_TIME),
    ;

    private int warpInMap, lobbyMap, warpOutMap, timeLimitSec;

    BossType(int inMap, int lobby, int outMap, int timeLimitSeconds) {
        timeLimitSec = timeLimitSeconds;
        warpOutMap = outMap;
        warpInMap = inMap;
        lobbyMap = lobby;
    }

    public int getWarpInMap() {
        return warpInMap;
    }

    public int getLobbyMap() {
        return lobbyMap;
    }

    public int getWarpOutMap() {
        return warpOutMap;
    }

    public int getTimeLimitSec() {
        return timeLimitSec;
    }

    public int getMaxDailyAttempts() {
        switch (this) {
            case EasyBalrog:
            case EasyHorntail:
            case EasyZakum:
                return 1;
            case NormalBalrog:
            case NormalHorntail:
            case NormalZakum:
            case PinkBean:
            case EasyVonLeon:
            case NormalVonLeon:
                return 2;
            case ChaosPinkBean:
            case ChaosHorntail:
            case ChaosZakum:
            case HardHilla:
                return 3;
        }
        return 1;
    }

    public int[] getAssociatedMaps() {
        switch (this) {
            case EasyBalrog:
                return new int[]{BossConstants.BALROG_EASY_BATTLE_MAP, BossConstants.BALROG_EASY_WIN_MAP};
            case NormalBalrog:
                return new int[]{BossConstants.BALROG_HARD_BATTLE_MAP, BossConstants.BALROG_HARD_WIN_MAP};
            case ChaosZakum:
                return new int[]{BossConstants.ZAKUM_CHAOS_ALTAR};
            case EasyZakum:
                return new int[]{BossConstants.ZAKUM_EASY_ALTAR};
            case NormalZakum:
                return new int[]{BossConstants.ZAKUM_HARD_ALTAR}; // hard == normal
            case EasyHorntail:
                return new int[]{240060000, 240060100, 240060201}; // todo make sure these are correct
            case NormalHorntail:
                return new int[]{240060002, 240060102, 240060202}; // todo verify
            case ChaosHorntail:
                return new int[]{240060001, 240060101, 240060201}; // cave of trial 1, 2, horntails cave
            case PinkBean:
                return new int[]{BossConstants.PINKBEAN_NORMAL_MAP}; // twilight of gods
            case ChaosPinkBean:
                return new int[]{BossConstants.PINKBEAN_CHAOS_MAP}; // twilight of gods (chaos map)
            case EasyVonLeon:
                return new int[]{BossConstants.VON_LEON_EASY_MAP, BossConstants.VON_LEON_EASY_BANISH_MAP};
            case NormalVonLeon:
                return new int[]{BossConstants.VON_LEON_NORMAL_MAP, BossConstants.VON_LEON_NORMAL_BANISH_MAP};
            case NormalHilla:
                return new int[]{262030100, 262030200, 262030300}; // todo verify
            case HardHilla:
                return new int[]{262031100, 262031200, 262031300}; // todo verify
            // hilla maps between 262030100 and 262031300
        }
        return null;
    }

    public String getName() {
        switch (this) {
            case ChaosHorntail:
                return "Chaos Horntail";
            case ChaosPinkBean:
                return "Chaos Pink Bean";
            case ChaosZakum:
                return "Chaos Zakum";
            case PinkBean:
                return "Pink Bean";
            case EasyHorntail:
            case NormalHorntail:
                return "Horntail";
            case EasyBalrog:
            case NormalBalrog:
                return "Balrog";
            case EasyZakum:
            case NormalZakum:
                return "Zakum";
            case NormalHilla:
            case HardHilla:
                return "Hilla";
            case EasyVonLeon:
            case NormalVonLeon:
                return "Von Leon";
        }
        return "Boss"; // "Are you sure you want to enter the <Boss> battle?"
    }
}
