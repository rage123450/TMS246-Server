package net.swordie.ms.connection.packet;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.handlers.header.OutHeader;

public class TestPacket {

    public static OutPacket test() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_);
        outPacket.encodeArr("");
        return outPacket;
    }

    public static OutPacket test1139() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1139);
        outPacket.encodeArr("A4 8D 00 00 CF 71 11 00");
        return outPacket;
    }

    public static OutPacket test650(int unk) {
        OutPacket outPacket = new OutPacket(OutHeader.UNK_650);
        outPacket.encodeInt(unk);
        outPacket.encodeByte(0);
        return outPacket;
    }

    public static OutPacket test649(byte unk1, int unk2) {
        OutPacket outPacket = new OutPacket(OutHeader.UNK_649);
        outPacket.encodeByte(unk1);
        outPacket.encodeInt(unk2);
        return outPacket;
    }

    public static OutPacket test648() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK_648);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test646() {
        OutPacket outPacket = new OutPacket(OutHeader.UI_STATUS_BAR_PACKET);
        outPacket.encodeArr("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F0 3F");
        return outPacket;
    }

    public static OutPacket test576() {
        OutPacket outPacket = new OutPacket(OutHeader.ADMIN_PACKET);
        outPacket.encodeArr("37 01 00 00 00 00 00 00 00 00 00 00 00");
        return outPacket;
    }

    public static OutPacket test131() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_131);
        outPacket.encodeArr("01");
        return outPacket;
    }

    public static OutPacket test584() {
        OutPacket outPacket = new OutPacket(OutHeader.SET_QUEST_CLEAR);
        return outPacket;
    }

    public static OutPacket test99() {
        OutPacket outPacket = new OutPacket(OutHeader.TEMPORARY_STAT_SET);
        outPacket.encodeArr("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 01 00 00 00 00 01");
        return outPacket;
    }

    public static OutPacket test98() {
        OutPacket outPacket = new OutPacket(OutHeader.STAT_CHANGED);
        outPacket.encodeArr("00 00 01 00 00 00 00 00 00 00 00 FF 00 00 01 00 00");
        return outPacket;
    }

    public static OutPacket test527() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_527);
        outPacket.encodeByte(0);

        return outPacket;
    }

    public static OutPacket temporaryStats_Reset() {
        OutPacket outPacket = new OutPacket(OutHeader.FORCED_STAT_RESET);
        return outPacket;
    }

    public static OutPacket test229() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_229);
        outPacket.encodeArr("0A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 40 4B 4C 00 80 8D 5B 00 C0 CF 6A 00 00 12 7A 00 40 54 89 00 80 96 98 00");
        return outPacket;
    }

    public static OutPacket test181() {
        OutPacket outPacket = new OutPacket(OutHeader.HOUR_CHANGED);
        outPacket.encodeArr("02 00 11 00");
        return outPacket;
    }

    public static OutPacket test1598() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1598);
        outPacket.encodeArr("01 00 00 00 06 00 56 45 76 65 6E 74 00 00 00 00");
        return outPacket;
    }

    public static OutPacket test1248() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1248);
        outPacket.encodeInt(0);
        return outPacket;
    }

    public static OutPacket test1018() {
        OutPacket outPacket = new OutPacket(OutHeader.USER_RENAME_RESULT);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test1191() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1191);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test1220() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1220);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test1199() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1199);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test1099() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1099);
        outPacket.encodeArr("00");
        return outPacket;
    }

    public static OutPacket test1081() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1081);
        return outPacket;
    }

    public static OutPacket test1938() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_1938);
        outPacket.encodeArr("01 01 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 40 E0 FD 3B 37 4F 01 00 80 05 BB 46 E6 17 02 39 00 00 00 90 88 8C 00 58 C4 01 00 00 00 00 00 16 47 86 00 91 88 8C 00 F0 69 05 00 00 00 00 00 02 47 86 00 92 88 8C 00 26 B9 6B 00 00 00 00 00 66 47 86 00 93 88 8C 00 FC 61 06 00 00 00 00 00 8A 7F 87 00 94 88 8C 00 04 EA 16 00 00 00 00 00 82 7F 87 00 95 88 8C 00 3F 91 86 00 00 00 00 00 80 7F 87 00 96 88 8C 00 8C 12 07 00 00 00 00 00 70 58 87 00 97 88 8C 00 C4 E4 65 00 00 00 00 00 D4 58 87 00 98 88 8C 00 E8 0C 0B 00 00 00 00 00 48 80 87 00 99 88 8C 00 A0 8E 08 00 00 00 00 00 04 CE 87 00 9A 88 8C 00 9A 97 6F 00 00 00 00 00 A0 CD 87 00 9B 88 8C 00 A0 8E 08 00 00 00 00 00 14 F5 87 00 9C 88 8C 00 D5 65 75 00 00 00 00 00 B0 F4 87 00 9D 88 8C 00 A0 8E 08 00 00 00 00 00 24 1C 88 00 9E 88 8C 00 F3 37 75 00 00 00 00 00 C0 1B 88 00 9F 88 8C 00 A0 8E 08 00 00 00 00 00 34 43 88 00 A0 88 8C 00 B3 65 8A 00 00 00 00 00 D0 42 88 00 A1 88 8C 00 24 5A 09 00 00 00 00 00 47 E3 86 00 A2 88 8C 00 9C E3 0C 00 00 00 00 00 40 E3 86 00 A3 88 8C 00 EC A8 15 00 00 00 00 00 4E E3 86 00 A4 88 8C 00 18 CC 07 00 00 00 00 00 E6 6E 86 00 A5 88 8C 00 68 F3 08 00 00 00 00 00 22 6E 86 00 A6 88 8C 00 C4 F3 0B 00 00 00 00 00 8A 6E 86 00 A7 88 8C 00 08 2F 0A 00 00 00 00 00 65 31 87 00 A8 88 8C 00 4C 48 16 00 00 00 00 00 60 31 87 00 A9 88 8C 00 84 6A 0C 00 00 00 00 00 21 95 86 00 AA 88 8C 00 6C 6F 74 00 00 00 00 00 F4 95 86 00 AB 88 8C 00 08 FC 50 00 00 00 00 00 BF 0A 87 00 AC 88 8C 00 18 E5 84 00 00 00 00 00 5B 0A 87 00 AD 88 8C 00 80 25 9F 01 00 00 00 00 55 91 88 00 AE 88 8C 00 A5 33 37 05 00 00 00 00 F1 90 88 00 AF 88 8C 00 BC 1D B8 01 00 00 00 00 EE 7F 87 00 B0 88 8C 00 C4 0C CE 04 00 00 00 00 E4 7F 87 00 B1 88 8C 00 00 FE 7B 03 00 00 00 00 0C 80 87 00 B2 88 8C 00 22 CA CE 05 00 00 00 00 0D 80 87 00 B3 88 8C 00 D4 0C 06 00 00 00 00 00 22 B3 81 00 B4 88 8C 00 14 8E 17 00 00 00 00 00 2C B3 81 00 B5 88 8C 00 56 85 32 01 00 00 00 00 36 B3 81 00 B6 88 8C 00 10 C4 04 04 00 00 00 00 D4 80 87 00 B7 88 8C 00 83 80 8B 06 00 00 00 00 AC 80 87 00 B8 88 8C 00 26 A3 3C 08 00 00 00 00 1A 81 87 00 B9 88 8C 00 3C 0C 0F 3B 00 00 00 00 76 81 87 00 BA 88 8C 00 44 1D 4F 04 00 00 00 00 2A E8 83 00 BB 88 8C 00 4C C9 A4 04 00 00 00 00 91 E9 83 00 BE 88 8C 00 16 F1 F8 06 00 00 00 00 2F E8 83 00 BF 88 8C 00 40 5C 29 07 00 00 00 00 CA E9 83 00 C0 88 8C 00 E0 9D E8 02 00 00 00 00 0E 80 87 00 C2 88 8C 00 58 E6 AC 0F 00 00 00 00 D8 81 87 00 C3 88 8C 00 CC EC 2D 07 00 00 00 00 47 82 87 00 C4 88 8C 00 FC D6 54 02 00 00 00 00 3C 82 87 00 C5 88 8C 00 F8 93 1B 03 00 00 00 00 E8 80 87 00 C6 88 8C 00 B5 11 A6 08 00 00 00 00 F6 81 87 00 C7 88 8C 00 2C 03 10 07 00 00 00 00 42 81 87 00 C8 88 8C 00 00 A3 E1 11 00 00 00 00 A2 82 87 00 A4 80 92 00 CC BA 05 00 00 00 00 00 0D C3 8F 00 A5 80 92 00 14 8E 17 00 00 00 00 00 15 C3 8F 00 A6 80 92 00 70 9D 88 00 00 00 00 00 26 32 90 00 00 40 E0 FD 3B 37 4F 01 00 80 05 BB 46 E6 17 02 39 00 00 00 90 88 8C 00 58 C4 01 00 00 00 00 00 16 47 86 00 91 88 8C 00 F0 69 05 00 00 00 00 00 02 47 86 00 92 88 8C 00 26 B9 6B 00 00 00 00 00 66 47 86 00 93 88 8C 00 FC 61 06 00 00 00 00 00 8A 7F 87 00 94 88 8C 00 04 EA 16 00 00 00 00 00 82 7F 87 00 95 88 8C 00 3F 91 86 00 00 00 00 00 80 7F 87 00 96 88 8C 00 8C 12 07 00 00 00 00 00 70 58 87 00 97 88 8C 00 C4 E4 65 00 00 00 00 00 D4 58 87 00 98 88 8C 00 E8 0C 0B 00 00 00 00 00 48 80 87 00 99 88 8C 00 A0 8E 08 00 00 00 00 00 04 CE 87 00 9A 88 8C 00 9A 97 6F 00 00 00 00 00 A0 CD 87 00 9B 88 8C 00 A0 8E 08 00 00 00 00 00 14 F5 87 00 9C 88 8C 00 D5 65 75 00 00 00 00 00 B0 F4 87 00 9D 88 8C 00 A0 8E 08 00 00 00 00 00 24 1C 88 00 9E 88 8C 00 F3 37 75 00 00 00 00 00 C0 1B 88 00 9F 88 8C 00 A0 8E 08 00 00 00 00 00 34 43 88 00 A0 88 8C 00 B3 65 8A 00 00 00 00 00 D0 42 88 00 A1 88 8C 00 24 5A 09 00 00 00 00 00 47 E3 86 00 A2 88 8C 00 9C E3 0C 00 00 00 00 00 40 E3 86 00 A3 88 8C 00 EC A8 15 00 00 00 00 00 4E E3 86 00 A4 88 8C 00 18 CC 07 00 00 00 00 00 E6 6E 86 00 A5 88 8C 00 68 F3 08 00 00 00 00 00 22 6E 86 00 A6 88 8C 00 C4 F3 0B 00 00 00 00 00 8A 6E 86 00 A7 88 8C 00 08 2F 0A 00 00 00 00 00 65 31 87 00 A8 88 8C 00 4C 48 16 00 00 00 00 00 60 31 87 00 A9 88 8C 00 84 6A 0C 00 00 00 00 00 21 95 86 00 AA 88 8C 00 6C 6F 74 00 00 00 00 00 F4 95 86 00 AB 88 8C 00 08 FC 50 00 00 00 00 00 BF 0A 87 00 AC 88 8C 00 18 E5 84 00 00 00 00 00 5B 0A 87 00 AD 88 8C 00 80 25 9F 01 00 00 00 00 55 91 88 00 AE 88 8C 00 A5 33 37 05 00 00 00 00 F1 90 88 00 AF 88 8C 00 BC 1D B8 01 00 00 00 00 EE 7F 87 00 B0 88 8C 00 C4 0C CE 04 00 00 00 00 E4 7F 87 00 B1 88 8C 00 00 FE 7B 03 00 00 00 00 0C 80 87 00 B2 88 8C 00 22 CA CE 05 00 00 00 00 0D 80 87 00 B3 88 8C 00 D4 0C 06 00 00 00 00 00 22 B3 81 00 B4 88 8C 00 14 8E 17 00 00 00 00 00 2C B3 81 00 B5 88 8C 00 56 85 32 01 00 00 00 00 36 B3 81 00 B6 88 8C 00 10 C4 04 04 00 00 00 00 D4 80 87 00 B7 88 8C 00 83 80 8B 06 00 00 00 00 AC 80 87 00 B8 88 8C 00 26 A3 3C 08 00 00 00 00 1A 81 87 00 B9 88 8C 00 3C 0C 0F 3B 00 00 00 00 76 81 87 00 BA 88 8C 00 44 1D 4F 04 00 00 00 00 2A E8 83 00 BB 88 8C 00 4C C9 A4 04 00 00 00 00 91 E9 83 00 BE 88 8C 00 16 F1 F8 06 00 00 00 00 2F E8 83 00 BF 88 8C 00 40 5C 29 07 00 00 00 00 CA E9 83 00 C0 88 8C 00 E0 9D E8 02 00 00 00 00 0E 80 87 00 C2 88 8C 00 58 E6 AC 0F 00 00 00 00 D8 81 87 00 C3 88 8C 00 CC EC 2D 07 00 00 00 00 47 82 87 00 C4 88 8C 00 FC D6 54 02 00 00 00 00 3C 82 87 00 C5 88 8C 00 F8 93 1B 03 00 00 00 00 E8 80 87 00 C6 88 8C 00 B5 11 A6 08 00 00 00 00 F6 81 87 00 C7 88 8C 00 2C 03 10 07 00 00 00 00 42 81 87 00 C8 88 8C 00 00 A3 E1 11 00 00 00 00 A2 82 87 00 A4 80 92 00 CC BA 05 00 00 00 00 00 0D C3 8F 00 A5 80 92 00 14 8E 17 00 00 00 00 00 15 C3 8F 00 A6 80 92 00 70 9D 88 00 00 00 00 00 26 32 90 00");
        return outPacket;
    }

    public static OutPacket test2131() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_2131);
        outPacket.encodeArr("20 00 80 05 BB 46 E6 17 02 F0 69 3D 60 AD 1B D9 01");
        return outPacket;
    }

    public static OutPacket test1307() {
        OutPacket outPacket = new OutPacket(OutHeader.MOB_CRC_KEY_CHANGED);
        outPacket.encodeArr("72 2D 00 00");
        return outPacket;
    }

    public static OutPacket test2147() {
        OutPacket outPacket = new OutPacket(OutHeader.UNK248_2147);
        outPacket.encodeArr("01 17 00 00 00 00 00 00 00 05 00 00 00 75 4B 0F 00 FE 72 0F 00 17 99 0F 00 BC 42 1F 00 D8 5D 29 00 01 00 00 00 05 00 00 00 75 4B 0F 00 FE 72 0F 00 17 99 0F 00 BC 42 1F 00 D8 5D 29 00 03 00 00 00 04 00 00 00 AA 94 11 00 87 09 12 00 BC 42 1F 00 D8 5D 29 00 04 00 00 00 03 00 00 00 19 BB 11 00 BD 42 1F 00 D8 5D 29 00 05 00 00 00 04 00 00 00 31 C0 0F 00 3D FC 10 00 BD 42 1F 00 D8 5D 29 00 0A 00 00 00 04 00 00 00 31 C0 0F 00 3D FC 10 00 BD 42 1F 00 D8 5D 29 00 0C 00 00 00 02 00 00 00 CE 1F 11 00 D8 5D 29 00 0E 00 00 00 04 00 00 00 31 C0 0F 00 3D FC 10 00 BD 42 1F 00 D8 5D 29 00 0F 00 00 00 05 00 00 00 18 99 0F 00 F0 46 11 00 29 BB 11 00 BD 42 1F 00 D8 5D 29 00 11 00 00 00 01 00 00 00 D8 5D 29 00 12 00 00 00 03 00 00 00 66 1F 11 00 CE 1F 11 00 D8 5D 29 00 13 00 00 00 04 00 00 00 AA 94 11 00 87 09 12 00 BC 42 1F 00 D8 5D 29 00 15 00 00 00 01 00 00 00 D8 5D 29 00 16 00 00 00 03 00 00 00 19 BB 11 00 BD 42 1F 00 D8 5D 29 00 17 00 00 00 05 00 00 00 18 99 0F 00 F0 46 11 00 29 BB 11 00 BD 42 1F 00 D8 5D 29 00 18 00 00 00 01 00 00 00 D8 5D 29 00 19 00 00 00 08 00 00 00 68 4E 0F 00 5D 54 0F 00 FE 72 0F 00 17 99 0F 00 17 D4 10 00 08 47 11 00 BC 42 1F 00 D8 5D 29 00 1E 00 00 00 03 00 00 00 AA 94 11 00 87 09 12 00 D8 5D 29 00 30 00 00 00 01 00 00 00 E1 FC 10 00 32 00 00 00 01 00 00 00 E1 FC 10 00 E8 03 00 00 04 00 00 00 FE 72 0F 00 17 99 0F 00 BC 42 1F 00 D8 5D 29 00 EA 03 00 00 05 00 00 00 1C C0 0F 00 F0 FB 10 00 D8 1F 11 00 D3 46 11 00 D8 5D 29 00 EB 03 00 00 01 00 00 00 D8 5D 29 00 04 00 00 00 06 00 61 72 63 61 6E 65 00 00 00 00 0B 00 63 6F 6D 70 65 74 69 74 69 6F 6E 00 00 00 00 06 00 67 72 6F 77 74 68 00 00 00 00 05 00 73 74 6F 72 79 00 00 00 00");
        return outPacket;
    }


}
