package net.swordie.ms.client.character;

import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.UserPool;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.QuestConstants;
import net.swordie.ms.enums.QuestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 26-5-2019.
 *
 * @author Asura
 */
public class PortableChair {
    private Char chr;
    private int itemID;
    private String msg = "";
    private List<String> displayChrs = new ArrayList<>();
    private long meso;
    private int displayedNumber;

    public PortableChair(Char chr, int itemID) {
        this.chr = chr;
        this.itemID = itemID;
    }

    public Char getChr() {
        return chr;
    }

    public void setChr(Char chr) {
        this.chr = chr;
    }

    public int getItemID() {
        return itemID;
    }

    public boolean isTextChair() {
        return ItemConstants.isTextChair(getItemID());
    }

    public boolean isTowerChair() {
        return ItemConstants.isTowerChair(getItemID());
    }

    public boolean isMesoChair() {
        return ItemConstants.isMesoChair(getItemID());
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getDisplayChrs() {
        return displayChrs;
    }

    public void setDisplayChrs(List<String> displayChrs) {
        this.displayChrs = displayChrs;
    }

    public void addDisplayChrs(String name) {
        getDisplayChrs().add(name);
    }

    public long getMeso() {
        return meso;
    }

    public void setMeso(long meso) {
        this.meso = meso;
    }

    public int getDisplayedNumber() {
        return displayedNumber;
    }

    public void setDisplayedNumber(int displayedNumber) {
        this.displayedNumber = displayedNumber;
    }

    public void addMeso(long meso) {
        if (meso > 0 && isMesoChair()) {
            setMeso(getMeso() + meso);
            chr.getField().broadcastPacket(UserPool.addMesoChairCount(chr.getId(), meso));
        }
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getItemID());
        outPacket.encodeInt(isTextChair() ? 1 : 0);
        if (isTextChair()) {
            outPacket.encodeString(getMsg());
        }
        outPacket.encodeInt(0); // new 176
        outPacket.encodeInt(0); // randInt
        outPacket.encodeByte(0); // randByte

        QuestManager qm = chr.getQuestManager();
        Quest q = qm.getQuests().getOrDefault(QuestConstants.TOWER_CHAIR, null);
        if (q == null) {
            q = new Quest(QuestConstants.TOWER_CHAIR, QuestStatus.Started);
            qm.addQuest(q);
        }
        q.convertQRValueToProperties();
        outPacket.encodeInt(q.getProperties().size());
        for (Map.Entry<String, String> entry : q.getProperties().entrySet()) {
            int towerChairID = Integer.parseInt(entry.getValue());
            outPacket.encodeInt(towerChairID);
        }

        boolean bool;
        outPacket.encodeByte(getDisplayChrs().size() > 0); // legion shit
        if (getDisplayChrs().size() > 0) {
            // sub_B5ABB0
            outPacket.encodeInt(chr.getAccount().getTotalLvOfAllChrs());
            outPacket.encodeInt(getDisplayChrs().size());
            for (String name : getDisplayChrs()) {
                Char displayChr = Char.getFromDBByName(name);
                outPacket.encodeInt(displayChr.getLevel());
                outPacket.encodeString(name);

                bool = true;
                outPacket.encodeByte(bool);
                if (bool) {
                    displayChr.getAvatarData().getAvatarLook().encode(outPacket);
                }
                bool = false;
                outPacket.encodeByte(bool);
                if (bool) {
                    displayChr.getAvatarData().getAvatarLook().encode(outPacket);
                }
            }
            outPacket.encodeInt(0);
        }

        outPacket.encodeLong(getMeso()); // meso

        bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            // virtual func
        }

    }
}
