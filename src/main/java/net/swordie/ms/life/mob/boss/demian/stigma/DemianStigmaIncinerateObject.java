package net.swordie.ms.life.mob.boss.demian.stigma;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.packet.DemianFieldPacket;
import net.swordie.ms.life.Life;
import net.swordie.ms.util.Position;
import net.swordie.ms.world.field.Field;

import java.util.Random;

/**
 * Created on 18-8-2019.
 *
 * @author Asura
 */
public class DemianStigmaIncinerateObject extends Life {

    public DemianStigmaIncinerateObject(int templateId) {
        super(templateId);
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        Field field = getField();
        setPosition(new Position(new Random().nextInt(1400) + 100, 16)); // randomise position before spawning
        field.broadcastPacket(DemianFieldPacket.stigmaIncinerateObjectPacket(this, true));
    }

    @Override
    public void broadcastLeavePacket() {
        Field field = getField();
        field.broadcastPacket(DemianFieldPacket.stigmaIncinerateObjectPacket(null, true)); // null -> remove
    }
}
