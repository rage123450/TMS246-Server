package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.Familiar;
import net.swordie.ms.life.movement.MovementInfo;
import net.swordie.ms.util.Util;

/**
 * @author Sjonnie
 * Created on 6/9/2018.
 */
public class CFamiliar {
    // Thanks to nox, my ida wasn't able to give me the correct opcodes :(

    public static OutPacket familiarEnterField(int charID, boolean transfer, Familiar familiar, boolean on, boolean animation) {
        OutPacket outPacket = new OutPacket(transfer ? OutHeader.FAMILIAR_TRANSFER_FIELD : OutHeader.FAMILIAR_ENTER_FIELD);
        outPacket.encodeInt(charID);
        outPacket.encodeByte(on); // on/off
        outPacket.encodeByte(!animation); // animation
        outPacket.encodeByte(0); // idk
        if (on) {
            outPacket.encodeInt(familiar.getFamiliarID());
            outPacket.encodeInt(familiar.getFatigue()); // fatigue
            outPacket.encodeInt(familiar.getVitality() * GameConstants.FAMILIAR_ORB_VITALITY); // total vitality
            outPacket.encodeString(familiar.getName());
            outPacket.encodePosition(familiar.getPosition());
            outPacket.encodeByte(familiar.getMoveAction());
            outPacket.encodeShort(familiar.getFh());
        }
        return outPacket;
    }

    public static OutPacket familiarMove(int charID, MovementInfo movementInfo) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_MOVE);

        outPacket.encodeInt(charID);
        outPacket.encodeByte(0);
        outPacket.encode(movementInfo);

        return outPacket;
    }

    public static OutPacket familiarAttack(int charID, AttackInfo attackInfo){
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_ATTACK);

        outPacket.encodeInt(charID);
        outPacket.encodeByte(0); // ?
        outPacket.encodeByte(attackInfo.idk);
        outPacket.encodeByte(attackInfo.mobCount);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            outPacket.encodeInt(mai.mobId);
            outPacket.encodeByte(mai.byteIdk1);
            outPacket.encodeByte(mai.damages.length);
            for (long dmg : mai.damages) {
                outPacket.encodeInt(Util.maxInt(dmg));
            }
        }

        return outPacket;
    }

    public static OutPacket familiarUpdateInfo(int charID, Familiar familiar) {
        OutPacket outPacket = new OutPacket(OutHeader.FAMILIAR_UPDATE_INFO);

        outPacket.encodeInt(charID);
        outPacket.encodeInt(familiar.getFamiliarID());
        outPacket.encodeInt(familiar.getFatigue());
        outPacket.encodeFT(familiar.getExpiration());

        return outPacket;
    }

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
}
