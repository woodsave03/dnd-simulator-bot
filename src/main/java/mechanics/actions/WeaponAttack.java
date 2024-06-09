package mechanics.actions;

import communication.Resolvable;
import game.entities.Ability;
import game.entities.Creature;
import game.items.Weapon;
import mechanics.Constructor;
import mechanics.RollMode;
import mechanics.dice.Damage;

import java.util.Set;

/**
 * An attack is a roll that can be made by a creature. It has a title, a damage
 * type, and a weapon group.
 * It extends the Roll class, so it also has an associated Ability Set and a roll mode.
 * Its subclasses will implement the use of a range.
 *
 * @implNote This class is not built to be used for spellcasting attacks. I suggest
 * making a new SpellAttack for this purpose.
 * @see Roll
 */
public class WeaponAttack extends AttackRoll {
    // The weapon group of the attack
    private Weapon.Group group;
    // The range of the Attack
    private Range range;

    /**
     * Constructs an Attack object with the given Builder.
     *
     * @param builder the Builder object to use
     */
    public WeaponAttack(Builder builder) {
        super(builder);
        this.group = builder.group;
        this.range = builder.range;
    }

    /**
     * Returns the weapon group of the attack.
     *
     * @return the weapon group of the attack
     */
    public Weapon.Group getGroup() {
        return group;
    }

    /**
     * Sets the weapon group of the attack.
     *
     * @param group the weapon group to set
     */
    public void setGroup(Weapon.Group group) {
        this.group = group;
    }

    /**
     * Compares this Attack to another Object.
     *
     * @param o the Object to compare to
     * @return true if the Object is an Attack and has the same fields as this Attack
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof WeaponAttack attack) {
            return super.equals(attack) && attack.group.equals(group);
        }
        return false;
    }

    /**
     * Returns a String representation of the Attack.
     *
     * @return a String representation of the Attack
     */
    @Override
    public String toString() {
        return "Attack{" +
                substring() +
                '}';
    }

    /**
     * Returns a substring of the Attack's fields.
     *
     * @return a substring of the Attack's fields
     */
    @Override
    protected String substring() {
        return super.substring() + ", group=" + group;
    }


    @Override
    protected int resolveBonus(Creature creature) {
        return creature.getProficiency(this) + creature.abilities().provide(resolveType(creature)).modifier();
    }

    /**
     * Returns a readable String representation of the Attack.
     *
     * @return a String representation of the Attack
     */
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Attack: ").append("(").append(getDamage().display()).append(")");
        if (range.isRanged()) {
            sb.append(" (").append(range.getShortRange()).append("/")
                    .append(range.getLongRange()).append(")");
        } else if (range.isReach()) {
            sb.append(" (Reach)");
        }
        return sb.toString();
    }

    /**
     * Returns true if the distance is within range of the attack.
     *
     * @param distance the distance to check
     * @return true if the distance is within range of the attack
     */
    public boolean inRange(int distance) {
        return range.inRange(distance);
    }

    /**
     * Returns the RollMode of the attack at the given distance.
     *
     * @param distance the distance to get the RollMode for
     * @return the RollMode of the attack at the given distance
     */
    public RollMode rollMode(int distance) {
        return range.rollMode(distance);
    }

    /**
     * Deconstructs the Attack into a Builder object.
     *
     * @return a Builder object with the same parameters as the Attack
     */
    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct())
                .with(group)
                .with(range.getShortRange(), range.getLongRange());
    }

    /**
     * Abstract Builder class for the Attack class.
     *
     * @see Roll.Builder
     */
    public static class Builder extends AttackRoll.Builder {
        // The default title of the attack
        // The weapon group of the attack
        private Weapon.Group group;
        private final Range range = new Range();

        @Override
        public Builder with(Set<Ability.Type> abilities) {
            super.with(abilities);
            return this;
        }

        @Override
        public Builder with(Ability.Type ability) {
            super.with(ability);
            return this;
        }

        /**
         * Sets the weapon group of the attack.
         *
         * @param group the weapon group to set
         * @return the Builder object (as per Builder pattern)
         */
        public Builder with(Weapon.Group group) {
            this.group = group;
            return this;
        }

        /**
         * Sets the melee range of the attack.
         *
         * @param shortRange the short range to set
         * @return the Builder object (as per Builder pattern)
         */
        public Builder with(int shortRange) {
            range.setShortRange(shortRange);
            return this;
        }

        /**
         * Sets the short and long range of the attack.
         *
         * @param shortRange the short range to set
         * @param longRange  the long range to set
         * @return the Builder object (as per Builder pattern)
         */
        public Builder with(int shortRange, int longRange) {
            range.setShortRange(shortRange);
            range.setLongRange(longRange);
            return this;
        }

        /**
         * Builds the Attack object.
         *
         * @return the Attack object
         */
        @Override
        public WeaponAttack build() {
            super.with(Type.ATTACK);
            return new WeaponAttack(this);
        }

        @Override
        public Builder with(Damage damage) {
            super.with(damage);
            return this;
        }

        @Override
        public Builder as(String title) {
            super.as(title);
            return this;
        }
    }
}
