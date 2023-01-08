package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.MagicSword;
import net.swordie.ms.util.Randomizer;

public class SkillPacket {

    public static OutPacket CreateSubObtacle(Char chr, int skillid) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_HIT);
        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(5);
        for (int i = 1; i <= 5; i++) {
            outPacket.encodeInt(i);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(i-1);
            outPacket.encodeInt(chr.getId());
            outPacket.encodeInt(0);
            outPacket.encodeInt((i == 2 || i == 3) ? 120 : ((i == 4 || i == 5) ? 240 : 0));
            outPacket.encodeInt(600);
            outPacket.encodeInt((i == 2) ? 15 : ((i == 3) ? -15 : ((i == 4) ? 30 : ((i == 5) ? -30 : 0))));
            outPacket.encodeInt(skillid);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(2400);
            outPacket.encodeInt(0);
            outPacket.encodeInt(1);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt((i == 1) ? chr.getPosition().getX() : ((i == 2) ? (chr.getPosition().getX() + 40) : ((i == 3) ? (chr.getPosition().getX() - 40) : ((i == 4) ? (chr.getPosition().getX() + 80) : ((i == 5) ? (chr.getPosition().getX() - 80) : 0)))));
            outPacket.encodeInt((i == 1) ? (chr.getPosition().getY() - 110) : ((i == 2 || i == 3) ? (chr.getPosition().getY() - 100) : ((i == 4 || i == 5) ? (chr.getPosition().getY() - 90) : 0)));
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
        }
        outPacket.encodeInt(0);
        return outPacket;
    }

    public static OutPacket CreateSwordReadyObtacle(Char chr, int skillid, int count) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_HIT);
        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(2);
        for (int i = 1; i <= 2; i++) {
            outPacket.encodeInt((i == 1) ? ((count - 1) * 10) : (count * 10));
            outPacket.encodeInt(0);
            outPacket.encodeInt((i == 1) ? (count - 1) : count);
            outPacket.encodeInt(0);
            outPacket.encodeInt(chr.getId());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(skillid);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(1);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(chr.getPosition().getX());
            outPacket.encodeInt(1);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
        }
        outPacket.encodeInt(0);
        return outPacket;
    }

    public static OutPacket removeSecondAtom(Char chr, int count) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_TRANSFER_FIELD);
        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(1);
        outPacket.encodeInt(count);
        outPacket.encodeInt(0);
        outPacket.encodeInt(2);
        return outPacket;
    }

    public static OutPacket AutoAttackObtacleSword(Char chr,final int sword, final int id) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_ATTACK);
        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(sword);
        if (id == 6 || id == 5) {
            outPacket.encodeInt(3);
        } else if (id == 4 || id == 3) {
            outPacket.encodeInt(2);
        } else if (id == 2 || id == 1) {
            outPacket.encodeInt(1);
        } else {
            outPacket.encodeInt(0);
        }
        return outPacket;
    }

    public static OutPacket RemoveSubObtacle(Char chr,final int id) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_TRANSFER_FIELD);
        outPacket.encodeByte(chr.getId());
        outPacket.encodeInt(1);
        outPacket.encodeInt(id);
        outPacket.encodeInt(0);
        outPacket.encodeInt(1);
        return outPacket;
    }

    public static OutPacket CreateSworldObtacle(final MagicSword ms) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_HIT);
        outPacket.encodeInt(ms.getChr().getId());
        outPacket.encodeInt(1);
        if (ms.core()) {
            outPacket.encodeInt(ms.getObjectId());
            outPacket.encodeInt(0);
            outPacket.encodeInt(8);
            outPacket.encodeInt(ms.getSwordCount());
            outPacket.encodeInt(ms.getChr().getId());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getSourceid());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getDuration());
            outPacket.encodeInt(ms.getChr().getPosition().getX() + Randomizer.rand(-500, 500));
            outPacket.encodeInt(ms.getChr().getPosition().getY() + Randomizer.rand(-300, 300));
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getChr().getPosition().getX() + Randomizer.rand(-500, 500));
            outPacket.encodeInt(ms.getChr().getPosition().getY() + Randomizer.rand(-300, 300));
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt((ms.getChr().getSkillCustomValue0(400011108) > 0L) ? 1 : 0);
        } else {
            outPacket.encodeInt(ms.getObjectId());
            outPacket.encodeInt(0);
            outPacket.encodeInt(7);
            outPacket.encodeInt(ms.getSwordCount());
            outPacket.encodeInt(ms.getChr().getId());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getSourceid());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getDuration());
            outPacket.encodeInt(ms.getChr().getPosition().getX() + ((ms.getSwordCount() == 0 || ms.getSwordCount() == 2 || ms.getSwordCount() == 4 || ms.getSwordCount() == 6 || ms.getSwordCount() == 8) ? 30 : -30));
            outPacket.encodeInt(ms.getChr().getPosition().getY());
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(ms.getChr().getPosition().getX() + ((ms.getSwordCount() == 0 || ms.getSwordCount() == 2 || ms.getSwordCount() == 4 || ms.getSwordCount() == 6 || ms.getSwordCount() == 8) ? 30 : -30));
            outPacket.encodeInt(ms.getChr().getPosition().getY());
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeByte(0);
            outPacket.encodeInt(3);
            switch (ms.getSwordCount()) {
                case 0:
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                case 12:
                case 14:
                case 16:
                case 18:
                case 20: {
                    outPacket.encodeArr("79 FF FF FF 5B FF FF FF 6A FF FF FF");
                    break;
                }
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 15:
                case 17:
                case 19:
                case 21: {
                    outPacket.encodeArr("87 00 00 00 A5 00 00 00 96 00 00 00");
                    break;
                }
                default: {
                    outPacket.encodeArr(new byte[12]);
                    break;
                }
            }
            outPacket.encodeInt(0);
        }

        return outPacket;
    }
}
