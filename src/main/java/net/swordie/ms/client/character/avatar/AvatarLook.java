package net.swordie.ms.client.character.avatar;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.WeaponType;
import net.swordie.ms.util.Util;

import jakarta.persistence.*;
import java.util.*;

/**
 * Created on 2/18/2017.
 */
@Entity
@Table(name = "avatarlook")
public class AvatarLook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int gender;
    private int skin;
    private int face;
    private int hair;
    private int weaponStickerId;

    private int weaponId;
    private int subWeaponId;
    @ElementCollection
    @CollectionTable(name = "hairequips", joinColumns = @JoinColumn(name = "alId"))
    @MapKeyColumn(name = "bodypart")
    @Column(name = "equipid")
    private Map<Byte, Integer> hairEquips;

    @ElementCollection
    @CollectionTable(name = "unseenequips", joinColumns = @JoinColumn(name = "alId"))
    @MapKeyColumn(name = "bodypart")
    @Column(name = "equipid")
    private Map<Byte, Integer> unseenEquips;
    @ElementCollection
    @CollectionTable(name = "petids", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "petId")
    private List<Integer> petIDs;
    private int job;
    private boolean drawElfEar;
    private int demonSlayerDefFaceAcc;
    private int xenonDefFaceAcc;
    private int beastTamerDefFaceAcc;
    private boolean isZeroBetaLook;
    private int mixedHairColor;
    private int mixHairPercent;
    @ElementCollection
    @CollectionTable(name = "totems", joinColumns = @JoinColumn(name = "alId"))
    @Column(name = "totemId")
    private List<Integer> totems;
    private int ears;
    private int tail;
    @Transient
    private int demonWingID;
    @Transient
    private int kaiserWingID;
    @Transient
    private int kaiserTailID;

    public AvatarLook() {
        hairEquips = new HashMap<>();
        unseenEquips = new HashMap<>();
        petIDs = Arrays.asList(0, 0, 0);
        totems = new ArrayList<>();
    }

    public AvatarLook deepCopy() {
        AvatarLook res = new AvatarLook();
        res.setGender(getGender());
        res.setSkin(getSkin());
        res.setFace(getFace());
        res.setHair(getHair());
        res.setWeaponStickerId(getWeaponStickerId());
        res.setWeaponId(getWeaponId());
        res.setSubWeaponId(getSubWeaponId());
        Map<Byte, Integer> resHairEquips = new HashMap<>(getHairEquips());
        res.setHairEquips(resHairEquips);
        Map<Byte, Integer> resUnseenEquips = new HashMap<>(getUnseenEquips());
        res.setUnseenEquips(resUnseenEquips);
        List<Integer> resPetIDs = new ArrayList<>(getPetIDs());
        res.setPetIDs(resPetIDs);
        res.setJob(getJob());
        res.setDrawElfEar(isDrawElfEar());
        res.setDemonSlayerDefFaceAcc(getDemonSlayerDefFaceAcc());
        res.setXenonDefFaceAcc(getXenonDefFaceAcc());
        res.setBeastTamerDefFaceAcc(getBeastTamerDefFaceAcc());
        res.setZeroBetaLook(isZeroBetaLook());
        res.setMixedHairColor(getMixedHairColor());
        res.setMixHairPercent(getMixHairPercent());
        List<Integer> resTotems = new ArrayList<>(getTotems());
        res.setTotems(resTotems);
        res.setEars(getEars());
        res.setTail(getTail());
        return res;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getWeaponStickerId() {
        return weaponStickerId;
    }

    public void setWeaponStickerId(int weaponStickerId) {
        this.weaponStickerId = weaponStickerId;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getSubWeaponId() {
        return subWeaponId;
    }

    public void setSubWeaponId(int subWeaponId) {
        this.subWeaponId = subWeaponId;
    }

    public Map<Byte, Integer> getHairEquips() {
        return hairEquips;
    }

    public void setHairEquips(Map<Byte, Integer> hairEquips) {
        this.hairEquips = hairEquips;
    }

    public Map<Byte, Integer> getUnseenEquips() {
        return unseenEquips;
    }

    public void setUnseenEquips(Map<Byte, Integer> unseenEquips) {
        this.unseenEquips = unseenEquips;
    }

    public List<Integer> getPetIDs() {
        return petIDs;
    }

    public void setPetIDs(List<Integer> petIDs) {
        this.petIDs = petIDs;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public boolean isDrawElfEar() {
        return drawElfEar;
    }

    public void setDrawElfEar(boolean drawElfEar) {
        this.drawElfEar = drawElfEar;
    }

    public int getDemonSlayerDefFaceAcc() {
        return demonSlayerDefFaceAcc;
    }

    public void setDemonSlayerDefFaceAcc(int demonSlayerDefFaceAcc) {
        this.demonSlayerDefFaceAcc = demonSlayerDefFaceAcc;
    }

    public int getXenonDefFaceAcc() {
        return xenonDefFaceAcc;
    }

    public void setXenonDefFaceAcc(int xenonDefFaceAcc) {
        this.xenonDefFaceAcc = xenonDefFaceAcc;
    }

    public int getBeastTamerDefFaceAcc() {
        return beastTamerDefFaceAcc;
    }

    public void setBeastTamerDefFaceAcc(int beastTamerDefFaceAcc) {
        this.beastTamerDefFaceAcc = beastTamerDefFaceAcc;
    }

    public boolean isZeroBetaLook() {
        return isZeroBetaLook;
    }

    public void setZeroBetaLook(boolean zeroBetaLook) {
        isZeroBetaLook = zeroBetaLook;
    }

    public int getMixedHairColor() {
        return mixedHairColor;
    }

    public void setMixedHairColor(int mixedHairColor) {
        this.mixedHairColor = mixedHairColor;
    }

    public int getMixHairPercent() {
        return mixHairPercent;
    }

    public void setMixHairPercent(int mixHairPercent) {
        this.mixHairPercent = mixHairPercent;
    }


    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getGender());
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getJob());
        outPacket.encodeByte(0);
        outPacket.encodeInt(getHair());

        for (Map.Entry<Byte, Integer> entry : getHairEquips().entrySet()) {
            outPacket.encodeByte(entry.getKey());
            outPacket.encodeInt(entry.getValue());
        }
        outPacket.encodeByte(-1);//1
        for (Map.Entry<Byte, Integer> entry : getUnseenEquips().entrySet()) {
            outPacket.encodeByte(entry.getKey());
            outPacket.encodeInt(entry.getValue());
        }
        outPacket.encodeByte(-1);//2
        // v199 - new item loop, ignores bodypart restrictions
        for (int itemId : new int[]{}) {
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender()));
            outPacket.encodeInt(itemId);
        }
        outPacket.encodeByte(-1);//3
        for (int itemId : getTotems()) {
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender()));
            outPacket.encodeInt(itemId);
        }
        outPacket.encodeByte(-1);//4

        outPacket.encodeInt(getWeaponStickerId());
        outPacket.encodeInt(getWeaponId());
        outPacket.encodeInt(getSubWeaponId());

        outPacket.encodeInt(isDrawElfEar() ? 1 : 0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);

        for (int i = 0; i < 3; i++) {
            if (getPetIDs().size() > i) {
                outPacket.encodeInt(getPetIDs().get(i)); // always 3
            } else {
                outPacket.encodeInt(0);
            }
        }
        if (JobConstants.isDemon((short) getJob())) {
            outPacket.encodeInt(getDemonSlayerDefFaceAcc());
        }
        if (JobConstants.isArk((short) getJob())) {
            outPacket.encodeInt(0); // face acc?
        }
        if (JobConstants.isHoYeong((short) getJob())) {
            outPacket.encodeInt(getDemonSlayerDefFaceAcc());
        }
        if (JobConstants.isXenon((short) getJob())) {
            outPacket.encodeInt(getXenonDefFaceAcc());
        }
        if (JobConstants.isZero((short) getJob())) {
            outPacket.encodeByte(isZeroBetaLook());
        }
        if (JobConstants.isBeastTamer((short) getJob())) {
            boolean hasEars = getEars() > 0;
            boolean hasTail = getTail() > 0;
            outPacket.encodeInt(getBeastTamerDefFaceAcc());
            outPacket.encodeByte(hasEars);
            outPacket.encodeInt(getEars());
            outPacket.encodeByte(hasTail);
            outPacket.encodeInt(getTail());
        }

        outPacket.encodeByte(getMixedHairColor());
        outPacket.encodeByte(getMixHairPercent());
        outPacket.encodeArr("A1 59 02 00");
        outPacket.encodeArr(new byte[5]);
    }

    public void encodePackedCharacterLook(OutPacket outPacket, Char chr) {
        // flawlessy typed over
        // and let's call it loop unrolling
        int[] hairEquips = new int[11];
        for (int i = 0; i < hairEquips.length; i++) {
            Item item = chr.getEquippedInventory().getItemBySlot(i);
            if (item != null) {
                hairEquips[i] = item.getItemId();
            } else {
                hairEquips[i] = -1;
            }
        }
        byte[] data = new byte[24];
        int weaponID = getWeaponStickerId() != 0 ? getWeaponStickerId() : getWeaponId();
        WeaponType weaponType = ItemConstants.getWeaponType(weaponID);
        int wt = weaponType == null ? 0 : weaponType.getVal();
        int faceAcc = 0;
        if (getDemonSlayerDefFaceAcc() != 0) {
            faceAcc = getDemonSlayerDefFaceAcc();
        } else if (getXenonDefFaceAcc() != 0) {
            faceAcc = getXenonDefFaceAcc();
        } else if (getBeastTamerDefFaceAcc() != 0) {
            faceAcc = getBeastTamerDefFaceAcc();
        }
        data[0] |= getGender() & 1;
        data[0] |= (getSkin() & 0xF) << 1;
        data[0] |= ((getFace() % 1000) & 0x3FF) << 5;
        data[1] |= (getFace() / 1000 % 10 & 7) << 7;
        data[2] |= (getFace() / 10000 == 4 ? 1 : 0) << 2;
        data[2] |= ((getFace() % 1000) & 0x3FF) << 3;
        data[3] |= (getFace() / 1000 % 10 & 0xF) << 5;
        data[4] |= ((hairEquips[1] % 1000) & 0x3FF) << 1;
        data[5] |= (hairEquips[1] / 1000 % 10 & 7) << 3;
        data[5] |= ((hairEquips[2] % 1000) & 0x3FF) << 6;
        data[7] |= faceAcc / 1000 % 10 & 3;
        data[7] |= (hairEquips[3] % 1000) & 0x3FF;
        data[8] |= (hairEquips[3] / 1000 % 10 & 3) << 4;
        data[8] |= ((hairEquips[4] % 1000) & 0x3FF) << 6;
        data[10] |= hairEquips[4] / 1000 % 10 & 3;
        data[10] |= (hairEquips[5] / 10000 == 105 ? 1 : 0) << 2;
        data[10] |= ((hairEquips[5] % 1000) & 0x3FF) << 3;
        data[11] |= ((hairEquips[5] / 1000 % 10) & 0xF) << 5;
        data[12] |= ((hairEquips[6] % 1000) & 0x3FF) << 1;
        data[13] |= (hairEquips[6] / 1000 % 10 & 3) << 3;
        data[13] |= ((hairEquips[7] % 1000) & 0x3FF) << 5;
        data[14] |= (hairEquips[7] / 1000 % 10 & 3) << 7;
        data[15] |= ((hairEquips[8] % 1000) & 0x3FF) << 1;
        data[16] |= (hairEquips[8] / 1000 % 10 & 3) << 3;
        data[16] |= ((hairEquips[9] % 1000) & 0x3FF) << 5;
        data[17] |= (hairEquips[9] / 1000 % 10 & 3) << 7;
        int val = hairEquips[10];
        if (val / 10000 == 109) {
            val = 1;
        } else {
            val = (val / 10000 != 134 ? 1 : 0) + 2;
        }
        data[18] |= (val & 3) << 1;
        data[19] |= ((hairEquips[10] % 1000) & 0x3FF) << 3;
        data[19] |= (hairEquips[10] / 1000 % 10 & 0xF) << 5;
        data[20] |= getWeaponStickerId() / 100000 == 17 ? 2 : 0;
        data[20] |= ((weaponID % 1000) & 0x3FF) << 2;
        data[21] |= (weaponID / 1000 % 10 & 3) << 4;
        data[21] |= (wt & 0x1F) << 6;
        data[22] |= (isDrawElfEar() ? 1 : 0) << 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 7; j >= 0; j--) {
                // basically encoding 3 sequential longs in little endian
                outPacket.encodeByte(data[j + i * 8]);
            }
        }
    }

    public void decode(InPacket inPacket) {
        setGender(inPacket.decodeByte());
        setSkin(inPacket.decodeByte());
        setFace(inPacket.decodeInt());
        setJob(inPacket.decodeInt());
        inPacket.decodeByte(); // ignored
        setHair(inPacket.decodeInt());

        while (inPacket.decodeByte() != -1) { // Body Part
            int itemId = inPacket.decodeInt();

        }
        while (inPacket.decodeByte() != -1) { // Body Part
            int itemId = inPacket.decodeInt();

        }
        while (inPacket.decodeByte() != -1) { // Body Part
            int itemId = inPacket.decodeInt();
            // add
        }
        while (inPacket.decodeByte() != -1) { // Body Part
            int itemId = inPacket.decodeInt();
            getTotems().add(itemId);
        }

        setWeaponStickerId(inPacket.decodeInt());
        setWeaponId(inPacket.decodeInt());
        setSubWeaponId(inPacket.decodeInt());
        inPacket.decodeInt(); // new 199
        setDrawElfEar(inPacket.decodeByte() != 0);

        for (int i = 0; i < 3; i++) {
            int petId = inPacket.decodeInt();
            //getPetIDs().add(petId);
        }
        if (JobConstants.isDemon((short) getJob())) {
            setDemonSlayerDefFaceAcc(inPacket.decodeInt());
        }
        if (JobConstants.isArk((short) getJob())) {
            inPacket.decodeInt(); // face acc?
        }
        if (JobConstants.isXenon((short) getJob())) {
            setXenonDefFaceAcc(inPacket.decodeInt());
        }
        if (JobConstants.isZero((short) getJob())) {
            setZeroBetaLook(inPacket.decodeByte() != 0);
        }
        if (JobConstants.isBeastTamer((short) getJob())) {
            setBeastTamerDefFaceAcc(inPacket.decodeInt());
            inPacket.decodeByte(); // has Ears
            setEars(inPacket.decodeInt());
            inPacket.decodeByte(); // has Tail
            setTail(inPacket.decodeInt());
        }
        setMixedHairColor(inPacket.decodeByte());
        setMixHairPercent(inPacket.decodeByte());
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public List<Integer> getTotems() {
        return totems;
    }

    public int getEars() {
        return ears;
    }

    public void setEars(int ears) {
        this.ears = ears;
    }

    public int getTail() {
        return tail;
    }

    public void setTail(short tail) {
        this.tail = tail;
    }

    public void setTotems(List<Integer> totems) {
        this.totems = totems;
    }

    public void setTail(int tail) {
        this.tail = tail;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemonWingID() {
        return demonWingID;
    }

    public void setDemonWingID(int demonWingID) {
        this.demonWingID = demonWingID;
    }

    public int getKaiserWingID() {
        return kaiserWingID;
    }

    public void setKaiserWingID(int kaiserWingID) {
        this.kaiserWingID = kaiserWingID;
    }

    public int getKaiserTailID() {
        return kaiserTailID;
    }

    public void setKaiserTailID(int kaiserTailID) {
        this.kaiserTailID = kaiserTailID;
    }

    public void removeItem(byte bodyPart, int itemID, int overrideId, boolean cash) {
        if (!ItemConstants.isTotem(itemID)) {
            Map<Byte, Integer> hairEquips = getHairEquips();
            Map<Byte, Integer> unseenEquips = getUnseenEquips();
            if (ItemConstants.isWeapon(itemID) && bodyPart % 100 == BodyPart.Weapon.getVal()) {
                if (cash) {
                    setWeaponStickerId(0);
                    //unseenEquips.remove((byte) (bodyPart - 100));
                } else {
                    setWeaponId(0);
                    hairEquips.remove(bodyPart);
                }
            } else if (cash) {
                hairEquips.remove((byte) (bodyPart - 100));
                if (overrideId >= 0) {
                    unseenEquips.remove((byte) (bodyPart - 100));
                    hairEquips.put((byte) (bodyPart - 100), overrideId);
                }
            } else {
                if (overrideId >= 0) {
                    unseenEquips.remove(bodyPart);
                } else {
                    hairEquips.remove(bodyPart);
                }
                if (bodyPart == BodyPart.Shield.getVal()) {
                    setSubWeaponId(0);
                }
            }
        } else {
            if (ItemConstants.isTotem(itemID)) {
                unseenEquips.remove(bodyPart);
            }
        }
    }
}