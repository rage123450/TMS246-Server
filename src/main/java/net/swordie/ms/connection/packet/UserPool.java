package net.swordie.ms.connection.packet;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.PortableChair;
import net.swordie.ms.client.character.avatar.AvatarLook;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.guild.Guild;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.QuestConstants;
import net.swordie.ms.enums.QuestStatus;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.handlers.PsychicLock;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.pet.Pet;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.Map;

/**
 * Created on 3/18/2018.
 */
public class UserPool {

    private static Field field;

    public static Field getField() {
        return field;
    }

    public static OutPacket userEnterField(Char chr) {
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        AvatarLook al = chr.getAvatarData().getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        System.out.println("進入userEnterField");
        OutPacket outPacket = new OutPacket(OutHeader.USER_ENTER_FIELD);

        outPacket.encodeLong(0); // 90 FE 6D 3A B1 CE D8 01
        outPacket.encodeInt(chr.getUserId());
        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(0);

        outPacket.encodeInt(chr.getLevel());
        outPacket.encodeString(chr.getName());
        outPacket.encodeString(""); // parent name, deprecated
        outPacket.encodeInt(0);
        if (chr.getGuild() != null) {
            chr.getGuild().encodeForRemote(outPacket);
        } else {
            Guild.defaultEncodeForRemote(outPacket);
        }

        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);

        tsm.encodeForRemote(outPacket, tsm.getCurrentStats());

        outPacket.encodeShort(chr.getJob());
        outPacket.encodeShort(cs.getSubJob());
        outPacket.encodeInt(chr.getTotalChuc()); // 38306
        outPacket.encodeInt(chr.getTotalAf()); // 3932160
        outPacket.encodeInt(0);
        al.encode(outPacket);
        if (JobConstants.isZero(chr.getJob())) {
            chr.getAvatarData().getZeroAvatarLook().encode(outPacket);
        }

        outPacket.encodeArr("00 00 00 00 FF 00 00 00 00 FF");

        outPacket.encodeInt(chr.getDriverID());
        outPacket.encodeInt(chr.getPassengerID()); // dwPassenserID
        // new 176: sub_191E2D0
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        // end sub_191E2D0
        outPacket.encodeInt(chr.getChocoCount());
        outPacket.encodeInt(chr.getActiveEffectItemID());
        outPacket.encodeInt(chr.getMonkeyEffectItemID());
        outPacket.encodeInt(chr.getActiveNickItemID());
        boolean bool = false;
        outPacket.encodeByte(bool); // new 188
        if (bool) {
            outPacket.encodeString(""); // new 188
        }

        outPacket.encodeInt(chr.getDamageSkin() == null ? 0 : chr.getDamageSkin().getDamageSkinID());
        outPacket.encodeInt(chr.getPremiumDamageSkin() == null ? 0 : chr.getPremiumDamageSkin().getDamageSkinID());
        outPacket.encodeInt(0);//248++
        outPacket.encodeString(""); // damage skin?
        outPacket.encodeString(""); // premium damage skin?
        outPacket.encodeInt(al.getDemonWingID());
        outPacket.encodeInt(al.getKaiserWingID());
        outPacket.encodeInt(al.getKaiserTailID());
        outPacket.encodeByte(true);//248++
        outPacket.encodeInt(chr.getCompletedSetItemID());
        outPacket.encodeInt(0);//248++
        outPacket.encodeInt(0);//248++
        outPacket.encodeShort(chr.getFieldSeatID());
        outPacket.encodeByte(false);//248++

        // ==== START  Portable Chair Encoding ====and
        PortableChair chair = chr.getChair() != null ? chr.getChair() : new PortableChair(chr, 0);
        outPacket.encodeInt(chair.getItemID());
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodePosition(chr.getPosition());
        outPacket.encodeByte(0);
        outPacket.encodeShort(chr.getFoothold());
        outPacket.encodeByte(1);
        outPacket.encodeByte(1);
        boolean hasPortableChairMsg = chair.isTextChair();
        outPacket.encodeByte(hasPortableChairMsg);
        if (hasPortableChairMsg) {
            outPacket.encodeString(chair.getMsg());
        }
        outPacket.encodeByte(chair.isMesoChair()); // is Meso Chair
        if (chair.isMesoChair()) {
            outPacket.encodeLong(chair.getMeso()); // Meso Displayed
        }
/* ?????
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

        outPacket.encodeInt(0); // new 176
        outPacket.encodeInt(0); // RInt
        outPacket.encodeByte(0); // RandByte
        outPacket.encodeByte(chair.getDisplayChrs().size() > 0); // legion shit
        if (chair.getDisplayChrs().size() > 0) {
            outPacket.encodeInt(chr.getAccount().getTotalLvOfAllChrs());
            outPacket.encodeInt(chair.getDisplayChrs().size());
            for (String displayChrName : chair.getDisplayChrs()) {
                Char displayedChr = Char.getFromDBByName(displayChrName);
                outPacket.encodeInt(displayedChr.getLevel());
                outPacket.encodeString(displayedChr.getName());
                bool = true;
                outPacket.encodeByte(bool);
                if (bool) {
                    displayedChr.getAvatarData().getAvatarLook().encode(outPacket); // AL
                }
                bool = false;
                outPacket.encodeByte(bool);
                if (bool) {
                    displayedChr.getAvatarData().getAvatarLook().encode(outPacket); // subAL
                }
            }
            outPacket.encodeInt(0);
        }
        outPacket.encodePosition(chr.getPosition());
        outPacket.encodeByte(chr.getMoveAction());
        outPacket.encodeShort(chr.getFoothold());
        outPacket.encodeByte(1); // ? new
        bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            // virtual function :(
        }
        // ==== END  Portable Chair Encoding ====
*/
        for(Pet pet : chr.getPets()) {
            if (pet.getId() == 0) {
                continue;
            }
            outPacket.encodeByte(1);
            outPacket.encodeInt(pet.getIdx());
            pet.encode(outPacket);
        }
        outPacket.encodeByte(0); // indicating that pets are no longer being encoded

        outPacket.encodeByte(false); // if true, encode something. idk what (v4->vfptr[35].Update)(v4, iPacket);
        /*
        // start new 188
        size = 0;
        outPacket.encodeByte(size);
        for (int i = 0; i < size; i++) {
            bool = false;
            outPacket.encodeByte(bool);
            if (bool) {
                chr.getActiveFamiliar().encodeForRemote(outPacket);
            }
        }
        // end new 188
        */
        outPacket.encodeInt(chr.getTamingMobLevel());
        outPacket.encodeInt(chr.getTamingMobExp());
        outPacket.encodeInt(chr.getTamingMobFatigue());
        bool = false;
        outPacket.encodeByte(bool); // new 188
        if (bool) {
            // virtual function :(
        }
        byte miniRoomType = chr.getMiniRoom() != null ? chr.getMiniRoom().getType() : 0;
        outPacket.encodeByte(miniRoomType);
        if(miniRoomType > 0) {
            chr.getMiniRoom().encode(outPacket);
            chr.encodeChatInfo(outPacket, chr.getMiniRoom().getMsg());
        }

        outPacket.encodeByte(chr.getADBoardRemoteMsg() != null);
        if (chr.getADBoardRemoteMsg() != null) {
            outPacket.encodeString(chr.getADBoardRemoteMsg());
        }

        outPacket.encodeByte(chr.isInCouple());
        if(chr.isInCouple()) {
            chr.getCouple().encodeForRemote(outPacket);
        }

        outPacket.encodeByte(chr.hasFriendshipItem()); //未完成
        if(chr.hasFriendshipItem()) {
            chr.getFriendshipRingRecord().encode(outPacket);
        }

        outPacket.encodeByte(chr.isMarried());//未完成
        if(chr.isMarried()) {
            chr.getMarriageRecord().encodeForRemote(outPacket);
        }

        outPacket.encodeByte(true); //

        byte flag = 0;
        if (chr.getSkillLevel(1320016) > 0 && chr.getJob() == 132) {
            flag = (byte) (flag | 0x1);
        }
        if (JobConstants.isEvan(chr.getJob())) {
            flag = (byte) (flag | 0x2);
        }
        outPacket.encodeInt(0);

        outPacket.encodeByte(flag);
        //outPacket.encodeInt(0);
        outPacket.encodeInt(chr.getEvanDragonGlide());

        if(JobConstants.isKaiser(chr.getJob())) {
            outPacket.encodeInt(chr.getKaiserMorphRotateHueExtern());
            outPacket.encodeInt(chr.getKaiserMorphPrimiumBlack());
            outPacket.encodeByte(chr.getKaiserMorphRotateHueInnner());
        }
        outPacket.encodeInt(chr.getMakingMeisterSkillEff());

//        chr.getFarmUserInfo().encode(outPacket);

        for (int i = 0; i < 5; i++) {
            outPacket.encodeByte(-1); // activeEventNameTag
        }

        outPacket.encodeInt(chr.getCustomizeEffect());//??
        if(chr.getCustomizeEffect() > 0) {
            outPacket.encodeString(chr.getCustomizeEffectMsg());
        }

        outPacket.encodeByte(chr.getSoulEffect());
        if(tsm.hasStat(CharacterTemporaryStat.RideVehicle)) {
            int vehicleID = tsm.getTSBByTSIndex(TSIndex.RideVehicle).getNOption();
            if(vehicleID == 1932249) { // is_mix_vehicle
                size = 0;
                outPacket.encodeInt(size); // ???
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0);
                }
            }
        }
        outPacket.encodeByte(0); // flashfire
        /*
         Flashfire (12101025) info
         not really interested in encoding this
         structure is:
         if(bool)
            if(bool)
                slv = int
                notused = int
                x = short
                y = short
         */

        outPacket.encodeArr("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 9B 0A 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF");
//        outPacket.encodeArr("00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 9B 0A 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF FF FF");

//        outPacket.encodeByte(0); // StarPlanetRank::Decode
        // CUser::DecodeStarPlanetTrendShopLook not interesting, will break REMOTE_AVATAR_MODIFIED if 1st int is != 0
/*        outPacket.encodeInt(0);
        // ~CUser::DecodeStarPlanetTrendShopLook
        outPacket.encodeInt(0); // CUser::DecodeTextEquipInfo
        chr.getFreezeHotEventInfo().encode(outPacket);
        outPacket.encodeInt(chr.getEventBestFriendAID()); // CUser::DecodeEventBestFriendInfo
        outPacket.encodeByte(tsm.hasStat(CharacterTemporaryStat.KinesisPsychicEnergeShield));
        outPacket.encodeByte(chr.isBeastFormWingOn());
        outPacket.encodeByte(false);
        outPacket.encodeByte(false);
        outPacket.encodeByte(false);
        outPacket.encodeInt(chr.getMesoChairCount());
        // end kmst
        outPacket.encodeInt(0);
        bool = false;
        outPacket.encodeByte(bool); // new 200
        if (bool) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        // next part: only if SecondaryStat::GetValue(v5 + 27522, iPacket_2) > 0
        outPacket.encodeInt(0);
        outPacket.encodeInt(0); // new 188
        outPacket.encodeString("");
        outPacket.encodeInt(0);
        bool = false;
        outPacket.encodeByte(bool);
        if(bool) {
            size = 0;
            outPacket.encodeInt(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
            }
        }
        int someID = 0;
        outPacket.encodeInt(someID);
        if(someID > 0) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeShort(0);
            outPacket.encodeShort(0);
        }
        outPacket.encodeInt(0);
        // start sub_16D99C0
        size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0);
        }
        // end sub_16D99C0
        */

        return outPacket;
    }

    public static OutPacket userLeaveField(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_LEAVE_FIELD);

        outPacket.encodeInt(chr.getId());

        return outPacket;
    }

    public static OutPacket releasePsychicLock(Char chr, int id) {
        OutPacket outPacket = new OutPacket(OutHeader.RELEASE_PSYCHIC_LOCK);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(id);

        return outPacket;
    }

    public static OutPacket releasePsychicArea(Char chr, int localAreaKey) {
        OutPacket outPacket = new OutPacket(OutHeader.RELEASE_PSYCHIC_AREA);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeInt(localAreaKey);
        outPacket.encodeInt(localAreaKey);

        return outPacket;
    }

    public static OutPacket createPsychicLock(Char chr, boolean approved, PsychicLock pl) {
        OutPacket outPacket = new OutPacket(OutHeader.CREATE_PSYCHIC_LOCK);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(approved);
        if (approved) {
            pl.encode(outPacket);
        }

        return outPacket;
    }

    public static OutPacket releasePsychicLockMob(Char chr, List<Integer> ids) {
        OutPacket outPacket = new OutPacket(OutHeader.RELEASE_PSYCHIC_LOCK_MOB);

        outPacket.encodeInt(chr.getId());
        for(int i : ids) {
            outPacket.encodeByte(1);
            outPacket.encodeInt(i);
        }
        outPacket.encodeByte(0);

        return outPacket;
    }

    public static OutPacket teslaTriangle(int chrId, List<Summon> rockNshockList) {
        OutPacket outPacket = new OutPacket(OutHeader.TESLA_TRIANGLE);

        outPacket.encodeInt(chrId);
        for (Summon rockNshockSummon : rockNshockList) {
            outPacket.encodeInt(rockNshockSummon.getObjectId());
        }

        return outPacket;
    }

    public static OutPacket skillOnOffEffect(int chrId, boolean show) {
        OutPacket outPacket = new OutPacket(OutHeader.SKILL_ON_OFF_EFFECT);

        outPacket.encodeInt(chrId);
        outPacket.encodeByte(show);

        return outPacket;
    }

    public static OutPacket addMesoChairCount(int chrId, long meso) {
        OutPacket outPacket = new OutPacket(OutHeader.ADD_MESO_CHAIR_COUNT);

        outPacket.encodeInt(chrId);
        outPacket.encodeInt(0); // unk
        outPacket.encodeLong(meso);
        outPacket.encodeLong(meso);

        return outPacket;
    }
}
