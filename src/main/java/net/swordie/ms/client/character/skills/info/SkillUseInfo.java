    package net.swordie.ms.client.character.skills.info;

    import net.swordie.ms.util.Position;

    /**
     * Created on 21-8-2019.
     *
     * @author Asura
     */
    public class SkillUseInfo {
        // General
        public Position position;
        public Position endingPosition;
        public boolean isLeft;

        // Projectile
        public int projectileItemId;

        // Rock 'N Shock (Mechanic)
        public byte rockNshockSize;
        public int[] rockNshockLifes = new int[2]; // used on last rockNshock, client encodes 2 previous rockNshocks planted in the Field.
    }
