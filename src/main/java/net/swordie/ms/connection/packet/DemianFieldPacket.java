package net.swordie.ms.connection.packet;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.mob.boss.demian.stigma.DemianStigmaIncinerateObject;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSword;

/**
 * Created on 16-8-2019.
 *
 * @author Asura
 */
public class DemianFieldPacket {

    public static OutPacket stigmaIncinerateObjectPacket(DemianStigmaIncinerateObject object, boolean animation) {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_STIGMA_INCINERATE_OBJECT_PACKET);

        boolean remove = object == null;
        outPacket.encodeInt(remove ? 1 : 0);
        if (!remove) {
            // OnStigmaObjectCreate
            outPacket.encodePositionInt(object.getPosition()); // nPos
            outPacket.encodeInt(2500); // tCastingTime
            outPacket.encodeInt(1); // SNFoothold
            outPacket.encodeString("Map/Obj/BossDemian.img/demian/altar");
            outPacket.encodeByte(!animation); // bImmediatelyStart/noAnimation
        }

        return outPacket;
    }

    public static OutPacket stigmaObjectEffect() {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_STIGMA_OBJECT_EFFECT);



        return outPacket;
    }

    public static OutPacket flyingSwordCreate(DemianFlyingSword demianFlyingSword) {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_FLYING_SWORD_CREATE);

        boolean create = true;
        outPacket.encodeByte(create); // create?
        outPacket.encodeInt(demianFlyingSword.getObjectId()); // oid
        if (create) {
            outPacket.encodeByte(demianFlyingSword.getSwordType().getVal()); // objType
            outPacket.encodeByte(4); // Attack Idx
            outPacket.encodeInt(demianFlyingSword.getOwner().getTemplateId()); // dwMobTemplateID  |  MobOwner
            outPacket.encodePositionInt(demianFlyingSword.getPosition());
        }

        return outPacket;
    }

    public static OutPacket flyingSwordNode(DemianFlyingSword demianFlyingSword) {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_FLYING_SWORD_NODE);

        outPacket.encodeInt(demianFlyingSword.getObjectId()); // flying Sword ObjId
        outPacket.encodeInt(demianFlyingSword.getDemianFlyingSwordPath().getNodes().size() > 1 ? demianFlyingSword.getTargetChrId() : 0); // char Id
        demianFlyingSword.getDemianFlyingSwordPath().encodeDemianFlyingSwordPath(outPacket);

        return outPacket;
    }

    public static OutPacket flyingSwordTarget(DemianFlyingSword demianFlyingSword) {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_FLYING_SWORD_TARGET);

        outPacket.encodeInt(demianFlyingSword.getObjectId()); // flying Sword ObjId
        outPacket.encodeInt(demianFlyingSword.getTargetChrId()); // targeted char Id

        return outPacket;
    }

    // The Window that shows the 30sec timer + 7 crystals,
    // This Packet only decides how many of the 7 crystals are broken.
    public static OutPacket corruptionChange(boolean remove, int stack) {
        OutPacket outPacket = new OutPacket(OutHeader.DEMIAN_CORRUPTION_CHANGE);

        outPacket.encodeByte(remove); // remove?
        outPacket.encodeInt(stack); // new Stacks

        return outPacket;
    }

    // The Window that shows the 30sec timer + 7 crystals,
    // This Packet only decides the timer.
    public static OutPacket stigmaRemainTime(int timeMS) {
        OutPacket outPacket = new OutPacket(OutHeader.STIGMA_REMAIN_TIME);

        outPacket.encodeInt(timeMS); // (ms)  | in seconds, in v206
        // outPacket.encodeInt(stack);
        // outPacket.encodeByte(0); // unk

        return outPacket;
    }

    public static OutPacket stigmaEffect(int chrId, boolean inc) {
        OutPacket outPacket = new OutPacket(OutHeader.STIGMA_EFFECT);

        outPacket.encodeInt(chrId);
        outPacket.encodeByte(inc); // inc or dec

        return outPacket;
    }
}
