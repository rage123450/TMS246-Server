package net.swordie.ms.world.map;

public class BossReward {
    private int objectId;

    private int mobId;

    private int partyId;

    private int price;

    public BossReward(int objectId, int mobId,int partyId, int price) {
        setObjectId(objectId);
        setMobId(mobId);
        setPartyId(partyId);
        setPrice(price);
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getMobId() {
        return this.mobId;
    }

    public void setMobId(int mobId) {
        this.mobId = mobId;
    }

    public int getPartyId() {
        return this.partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
