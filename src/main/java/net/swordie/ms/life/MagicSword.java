package net.swordie.ms.life;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.util.Position;

import java.util.concurrent.ScheduledFuture;

public class MagicSword extends Life {
    private Char chr;

    private int sourceid;

    private int swordcount;

    private int duration;

    private int type;

    private int delay;

    private int mobid;

    private boolean core = false;

    private ScheduledFuture<?> schedule = null;

    public MagicSword(int templateId) {
        super(templateId);
    }

    public Char getChr() {
        return this.chr;
    }
    public void setChr(Char chr) {
        this.chr = chr;
    }
    public int getSourceid() {
        return this.sourceid;
    }

    public void setSourceid(int sourceid) {
        this.sourceid = sourceid;
    }

    public int getSwordCount() {
        return this.swordcount;
    }

    public void setSwordCount(int swordcount) {
        this.swordcount = swordcount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean core() {
        return this.core;
    }

    public void setCore(boolean v) {
        this.core = v;
    }

    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }

    public void setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }

    public int getSType() {
        return this.type;
    }

    public void setSType(int type) {
        this.type = type;
    }

    public int getMobid() {
        return this.mobid;
    }

    public void setMobid(int mobid) {
        this.mobid = mobid;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public static MagicSword getMagicSwordBy(Char chr, int sourceid, int swordcount, int duration, boolean core) {
        MagicSword magicSword = new MagicSword(-1);
        magicSword.setChr(chr);
        magicSword.setSourceid(sourceid);
        magicSword.setSwordCount(swordcount);
        magicSword.setDuration(duration);
        magicSword.setCore(core);
        return magicSword;
    }

    public static MagicSword getMagicSwordBy(Char chr, int type, int mobid, int delay, int sourceid, int duration, Position position) {
        MagicSword magicSword = new MagicSword(-1);
        magicSword.setChr(chr);
        magicSword.setSType(type);
        magicSword.setMobid(mobid);
        magicSword.setDelay(delay);
        magicSword.setSourceid(sourceid);
        magicSword.setDuration(duration);
        magicSword.setDuration(duration);
        magicSword.setPosition(position);
        return magicSword;
    }

}
