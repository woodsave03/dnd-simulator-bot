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
 *
 * @see Roll
 */
public class Attack extends Roll {
    // The damage and damage type of the attack
    private Damage damage;
    // The title of the attack
    private String title;
    // The weapon group of the attack
    private Weapon.Group group;
    // The range of the Attack
    private Range range;

    /**
     * Constructs an Attack object with the given Builder.
     *
     * @param builder the Builder object to use
     */
    public Attack(Builder builder) {
        super(builder, Type.ATTACK);
        this.damage = builder.damage;
        this.title = builder.title;
        this.group = builder.group;
        this.range = builder.range;
    }

    /**
     * Returns the title of the attack.
     *
     * @return the title of the attack
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the attack.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Uses the communication system to resolve the ideal Ability.Type for the Attack.
     *
     * @param creature the Creature to resolve the Ability.Type for
     * @return the resolved Ability.Type
     */
    public Ability.Type resolveType(Creature creature) {
        Resolvable<Ability.Type> resolvable = new Resolvable<>(getAbilityOptions());
        return creature.resolve(resolvable.options()).type();
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
        if (o instanceof Attack attack) {
            return super.equals(attack) && attack.damage.equals(damage)
                    && attack.title.equals(title) && attack.group.equals(group);
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
        return super.substring() + ", damage=" + damage + ", title='" + title + "', group=" + group;
    }

    /**
     * Returns the damage object of the attack.
     *
     * @return the damage object of the attack
     */
    public Damage getDamage() {
        return damage;
    }

    /**
     * Sets the damage object of the attack.
     *
     * @param damage the damage object to set
     */
    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    @Override
    protected int resolveBonus(Creature creature) {
        return creature.getProficiency(this) + creature.provide(resolveType(creature)).modifier();
    }

    /**
     * Returns a readable String representation of the Attack.
     *
     * @return a String representation of the Attack
     */
    @Override
    public String display() {
        return title + " (" + damage.display() + ")";
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

    @Override
    public Constructor builder() {
        return new Builder();
    }

    /**
     * Abstract Builder class for the Attack class.
     *
     * @see Roll.Builder
     */
    public static class Builder extends Roll.Builder {
        // The default title of the attack
        private static final String DEFAULT_TITLE = "Attack";
        // The title of the attack
        private String title = DEFAULT_TITLE;
        // The damage object of the attack
        protected Damage damage;
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
         * Sets the title of the attack.
         *
         * @param title the title to set
         * @return the Builder object (as per Builder pattern)
         */
        public Builder as(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the damage object of the attack.
         *
         * @param damage the damage object to set
         * @return the Builder object (as per Builder pattern)
         */
        public Builder with(Damage damage) {
            this.damage = damage;
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
         * @param longRange the long range to set
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
        public Attack build() {
            return new Attack(this);
        }
    }
}
