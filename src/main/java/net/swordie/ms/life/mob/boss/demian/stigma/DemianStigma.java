package net.swordie.ms.life.mob.boss.demian.stigma;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.packet.DemianFieldPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.constants.BossConstants;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSword;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSwordType;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.Stigma;

/**
 * Created on 18-8-2019.
 *
 * @author Asura
 */
public class DemianStigma {
    public static void resetStigma(Char chr) {
        changeStigma(chr, 0);
    }

    public static void incStigma(Char chr) {
        incStigma(chr, 1);
        chr.write(FieldPacket.playSound("SoundEff/BossDemian/incStigma"));
        chr.write(DemianFieldPacket.stigmaEffect(chr.getId(), true));
    }

    public static void incStigma(Char chr, int amount) {
        changeStigma(chr, getStigmaCount(chr) + amount);
    }

    public static void decStigma(Char chr) {
        decStigma(chr, 1);
    }

    public static void decStigma(Char chr, int amount) {
        changeStigma(chr, getStigmaCount(chr) - amount);
    }

    public static void incCorruption(Char chr) {
        changeCorruption(chr, getCorruptionCount(chr) + 1);
    }

    public static void changeCorruption(Char chr, int newCorruption) {
        int maxCorruption = 7;
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.bOption = BossConstants.DEMIAN_MAX_STIGMA;
        o.nOption = getStigmaCount(chr);
        o.rOption = 237;
        o.xOption = newCorruption > maxCorruption ? maxCorruption : newCorruption < 0 ? 0 : newCorruption;
        tsm.putCharacterStatValue(Stigma, o);
        tsm.sendSetStatPacket();
        chr.write(DemianFieldPacket.corruptionChange(false, o.xOption));
    }

    public static void changeStigma(Char chr, int newStigma) {
        //int curStigma = getStigmaCount(chr);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.bOption = BossConstants.DEMIAN_MAX_STIGMA;
        o.nOption = newStigma > o.bOption ? o.bOption : newStigma < 0 ? 0 : newStigma;
        o.rOption = 237;
        o.xOption = getCorruptionCount(chr);
        tsm.putCharacterStatValue(Stigma, o);
        tsm.sendSetStatPacket();

        // if curStigma reaches maxStigma
        if (o.nOption >= BossConstants.DEMIAN_MAX_STIGMA) {
            chr.damage(chr.getMaxHP()); // kill
            incCorruption(chr);         // increase Corruption by 1
            resetStigma(chr);

            // spawn extra sword
            Mob mob = chr.getField().getMobs().stream().findFirst().orElse(null);
            if (mob != null) {
                DemianFlyingSword sword = DemianFlyingSword.createDemianFlyingSword(chr, mob);
                sword.setSwordType(DemianFlyingSwordType.SecondSword);
                chr.getField().spawnLife(sword, null);
                sword.startPath();
                sword.target();
            }
        }
    }

    public static int getStigmaCount(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Stigma)) {
            return tsm.getOption(Stigma).nOption;
        }
        return 0;
    }

    public static int getCorruptionCount(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Stigma)) {
            return tsm.getOption(Stigma).xOption;
        }
        return 0;
    }
}
