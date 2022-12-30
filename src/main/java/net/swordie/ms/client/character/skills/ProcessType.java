package net.swordie.ms.client.character.skills;

import net.swordie.ms.client.character.skills.info.SkillUseInfo;
import net.swordie.ms.connection.InPacket;

/**
 * Created on 11-4-2019.
 */
public class ProcessType {

    public SkillUseInfo skillUseInfo;

    public ProcessType(SkillUseInfo skillUseInfo) {
        this.skillUseInfo = skillUseInfo;
    }

    public void decode(InPacket inPacket) {
        inPacket.decodeInt(); // 22 CF 13 EE
        boolean bool = inPacket.decodeByte() != 0;
        if (bool) {
            int processType = inPacket.decodeInt();
            while (processType != -1) {
                if (processType >= 1 && processType <= 16 || processType >= 19 && processType <= 21) {
                    bool = inPacket.decodeByte() != 0;
                    if (bool) {
                        switch (processType) {
                            case 1:
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                break;
                            case 2:
                                inPacket.decodeByte();
                                inPacket.decodeByte();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeArr(25);//??
                                break;
                            case 3:
                                inPacket.decodeByte();
                                inPacket.decodeInt();
                                break;
                            case 7:
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeByte();
                                break;
                            case 8:
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                break;
                            case 9:
                                inPacket.decodeInt();
                                skillUseInfo.endingPosition = inPacket.decodePositionInt(); // ending Position
                                skillUseInfo.isLeft = inPacket.decodeInt() == -1; // isLeft   -1 = Left  |  1 = Right
                                break;
                            case 20:
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                inPacket.decodeInt();
                                break;
                            case 21:
                                int size = inPacket.decodeInt();
                                for (int i = 0; i < size; i++) {
                                    inPacket.decodeInt();
                                    inPacket.decodeInt();
                                    inPacket.decodeInt();
                                }
                                break;
                        }
                    }
                }
                processType = inPacket.decodeInt();
            }
        }
    }
}
