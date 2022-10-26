package net.swordie.ms.enums;

/**
 * Created by Asura on 16-6-2018.
 */
public enum BroadcastMsgType {
    Notice(0),
    PopUpMessage(1),
    DarkBlueOnLightBlue(2),
    Megaphone(3),
    SlideNotice(4),
    PartyChat(5),
    BlueChat_ItemInfo(6),
    GM_ErrorMessage(7),
    ItemMegaphone(8), // Holds item info
    ItemMegaphoneNoItem(9),
    TripleMegaphone(10),
    YellowChatFiled_ItemInfo(11), //  item shown when clicked,  does hold Item info
    BlowWeather(12), // TODO  Contains PackedCharLook
    TryRegisterAutoStartQuest(13), // tries to auto start quests with the announcement (?) - probably the  "A quest has arrived! Please clock on the icon at the botfom of your screen."
    TryRegisterAutoStartQuest_NoAnnouncement(14), // tries to auto start quests (?)
    SwedishFlag(15), // Repeats the string 3x on the same line
    RedWithChannelInfo(16), // May be for  /find ?
    WhiteYellow_ItemInfo(17), // Holds item info
    BlueChat_ItemInfo_2(18),
    WhiteYellow(19),
    PopUpNotice(20),
    Yellow(21), // Holds item info
    Yellow_2(22),
    MegaphoneNoMessage(23),
    BalloonMessage(24); // TODO   Contains PackedCharLook

    private byte val;

    BroadcastMsgType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public boolean isSmega() {
        switch (this) {
            case Megaphone:
            case MegaphoneNoMessage:
//            case 34:
//            case 35:
//            case 36:
            case ItemMegaphone:
            case ItemMegaphoneNoItem:
            case TripleMegaphone:
            case DarkBlueOnLightBlue:
                return true;
        }
        return false;
    }
}
