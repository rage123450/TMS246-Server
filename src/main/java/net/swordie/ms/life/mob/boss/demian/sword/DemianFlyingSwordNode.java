package net.swordie.ms.life.mob.boss.demian.sword;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.util.Position;

/**
 * Created on 17-8-2019.
 *
 * @author Asura
 */
public class DemianFlyingSwordNode {
    private DemianFlyingSwordNodeType nodeType;
    private DemianFlyingSwordPathIdx pathIdx;
    private short nodeIdx;
    private short velocity;
    private int startDelay;
    private int endDelay;
    private int duration;
    private boolean hide;
    private byte collisionType;
    private Position position;

    public DemianFlyingSwordNode(DemianFlyingSwordNodeType nodeType, Position position) {
        this.nodeType = nodeType;
        this.velocity = 35;
        this.position = position;
    }

    public DemianFlyingSwordNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(DemianFlyingSwordNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public DemianFlyingSwordPathIdx getPathIdx() {
        return pathIdx;
    }

    public void setPathIdx(DemianFlyingSwordPathIdx pathIdx) {
        this.pathIdx = pathIdx;
    }

    public short getNodeIdx() {
        return nodeIdx;
    }

    public void setNodeIdx(short nodeIdx) {
        this.nodeIdx = nodeIdx;
    }

    public short getVelocity() {
        return velocity;
    }

    public void setVelocity(short velocity) {
        this.velocity = velocity;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public int getEndDelay() {
        return endDelay;
    }

    public void setEndDelay(int endDelay) {
        this.endDelay = endDelay;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public byte getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(byte collisionType) {
        this.collisionType = collisionType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void encodeNodeData(OutPacket outPacket) {
        outPacket.encodeByte(getNodeType().getVal());
        outPacket.encodeShort(getPathIdx().getVal());
        outPacket.encodeShort(getNodeIdx());
        outPacket.encodeShort(getVelocity());
        outPacket.encodeInt(getStartDelay());
        outPacket.encodeInt(getEndDelay());
        outPacket.encodeInt(getDuration());
        outPacket.encodeByte(isHide());
        outPacket.encodeByte(getCollisionType());
        outPacket.encodePositionInt(getPosition());
    }
}
