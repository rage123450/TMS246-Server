package net.swordie.ms.handlers;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.handlers.header.OutHeader;

/**
 * Created on 1/10/2018.
 */
public class ClientSocket {

    public static OutPacket migrateCommand(boolean succeed, short port) {
        OutPacket outPacket = new OutPacket(OutHeader.MIGRATE_COMMAND);

        outPacket.encodeByte(succeed); // will disconnect if false
        if (succeed) {
            byte[] server = new byte[]{127, 0, 0, ((byte) 1)};
            outPacket.encodeArr(server);
            outPacket.encodeShort(port);
            outPacket.encodeInt(0); // ?
        }

        return outPacket;
    }

    public static OutPacket opcodeEncryption(int blockSize, byte[] buf) {
        OutPacket outPacket = new OutPacket(OutHeader.OPCODE_ENCRYPTION);

        outPacket.encodeInt(blockSize);
        outPacket.encodeInt(buf.length);
        outPacket.encodeArr(buf);

        return outPacket;
    }

    public static OutPacket ChannelChangeEnable() {
        OutPacket outPacket = new OutPacket(OutHeader.Channel_Change_Enable);
        outPacket.encodeInt(0xB3247D3F);
        outPacket.encodeShort(0x14);
        return outPacket;
    }

    public static OutPacket AuthCodeChanged() {
        OutPacket outPacket = new OutPacket(OutHeader.AUTH_CODE_CHANGED);

        outPacket.encodeByte(1);//0?
        outPacket.encodeInt(0);

        return outPacket;
    }
}
