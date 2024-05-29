package mechanics.actions;

import communication.Pair;
import game.entities.Creature;
import mechanics.Construct;
import mechanics.Constructor;
import mechanics.RollMode;
import mechanics.dice.Die;
import mechanics.dice.Rollable;
import game.entities.Ability;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * The Roll class is an abstract representation of a roll in Dungeons and Dragons.
 * It is used to represent the various types of rolls that can be made in the game.
 * The Roll class is used to represent the following types of rolls:
 * - Attack
 * - Save
 * - Check
 * - Contest
 */
public abstract class Roll implements Rollable, Construct, Serializable {
    // The d20 die is used to roll the 20-sided die for all rolls
    private static final Die d20 = Die.Factory.d20();
    // The set of abilities that can be used for the roll
    private Set<Ability.Type> abilityOptions;
    // The type of roll that is being made
    private Type rollType;

    /**
     * Constructor for a new Roll object with the given ability options and type.
     *
     * @param builder The builder object used to create the Roll object
     * @param type The type of roll that is being made
     */
    public Roll(Builder builder, Type type) {
        this.abilityOptions = builder.abilities;
        this.rollType = type;
    }

    /**
     * Roll is considered a Rollable, but does not implement the constant field,
     * unlike the Damage or Sequence classes.
     *
     * @param constant The constant value to be ignored
     * @throws UnsupportedOperationException Throws an exception because rolls cannot have constants
     */
    @Override
    public void setConstant(int constant) {
        throw new UnsupportedOperationException("Rolls cannot have constants");
    }

    /**
     * Rolls the 20-sided die and returns the result.
     *
     * @return The result of the roll
     */
    @Override
    public int roll() {
        return d20.roll();
    }

    /**
     * All-encompassing method to execute a roll
     *
     * @param creature The relevant creature object
     * @param mode The mode of the roll
     * @return The result of the roll
     */
    public int execute(Creature creature, RollMode mode) {
        return rollWith(mode) + resolveBonus(creature);
    }

    /**
     * Resolves the bonus for the roll based on the given creature object.
     * @param creature The relevant creature object
     * @return The bonus for the roll
     */
    protected abstract int resolveBonus(Creature creature);

    /**
     * Rolls the 20-sided die and returns the result based on the given mode.
     *
     * @param mode The mode of the roll (STRAIGHT, ADVANTAGE, or DISADVANTAGE)
     * @return The result of the roll
     */
    public int rollWith(RollMode mode) {
        return switch (mode) {
            case STRAIGHT -> roll();
            case ADVANTAGE -> Math.max(roll(), roll());
            case DISADVANTAGE -> Math.min(roll(), roll());
        };
    }

    /**
     * Roll is considered a Rollable, but does not implement the rollAfter functionality,
     * unlike the Damage or Sequence classes.
     *
     * @return Throws an exception because rolls cannot have after modifiers
     * @throws UnsupportedOperationException Throws an exception because rolls cannot have after modifiers
     */
    @Override
    public int rollAfter() {
        throw new UnsupportedOperationException("Rolls cannot have after modifiers");
    }

    /**
     * Returns the set of ability options for the roll.
     *
     * @return The set of ability options for the roll
     */
    public Set<Ability.Type> getAbilityOptions() {
        return abilityOptions;
    }

    /**
     * Sets the ability options for the roll to the given set of abilities.
     *
     * @param abilityOptions The set of abilities to set for the roll
     */
    public void setAbilityOptions(Set<Ability.Type> abilityOptions) {
        this.abilityOptions = abilityOptions;
    }

    /**
     * Determines if the roll was successful based on the given DC and creature object
     *
     * @param dc The DC for the roll
     * @param creature The relevant creature object
     * @param mode The mode of the roll
     * @return A pair containing a boolean indicating success and the result of the roll
     */
    public Pair<Boolean, Integer> success(int dc, Creature creature, RollMode mode) {
        int roll = execute(creature, mode);
        return new Pair<>(roll >= dc, roll);
    }

    /**
     * Returns the type of roll that is being made.
     *
     * @return The type of roll that is being made
     */
    public Type getRollType() {
        return rollType;
    }

    /**
     * Sets the type of roll that is being made to the given type.
     *
     * @param rollType The type of roll to set for the roll
     */
    public void setRollType(Type rollType) {
        this.rollType = rollType;
    }

    /**
     * Returns a readable string representation of the roll.
     *
     * @return A string representation of the roll
     */
    public abstract String display();

    /**
     * Returns a JSON string representation of the roll.
     *
     * @return A JSON string representation of the roll
     */
    @Override
    public String toString() {
        return "Roll{" +
                substring() +
                '}';
    }

    /**
     * Helper method for toString() to return a substring of the roll.
     *
     * @return A substring of the roll
     */
    protected String substring() {
        return "abilityOptions=" + abilityOptions +
                ", rollType=" + rollType;
    }

    /**
     * Determines if the given object is equal to the roll.
     *
     * @param other The object to compare to the roll
     * @return A boolean indicating if the object is equal to the roll
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Roll roll) {
            return abilityOptions.equals(roll.abilityOptions) &&
                    rollType == roll.rollType;
        }
        return false;
    }

    /**
     * The Constructor class of the Roll Object as per the Builder Pattern
     */
    public static abstract class Builder implements Constructor {
        // The set of abilities that can be used for the roll
        protected Set<Ability.Type> abilities = new HashSet<>();

        /**
         * Adds the given set of abilities to the roll.
         *
         * @param abilities The set of abilities to add to the roll
         * @return The builder object with the added abilities
         */
        public Builder with(Set<Ability.Type> abilities) {
            assert !abilities.isEmpty();
            this.abilities.addAll(abilities);
            return this;
        }

        /**
         * Adds the given ability to the roll.
         *
         * @param type The ability to add to the roll
         * @return The builder object with the added ability
         */
        public Builder with(Ability.Type type) {
            this.abilities.add(type);
            return this;
        }
    }

    /**
     * The Type enum is used to represent the different types of rolls that can be made.
     */
    public enum Type {
        ATTACK, SAVE, CHECK, CONTEST;
    }
}
