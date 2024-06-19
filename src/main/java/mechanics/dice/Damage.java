package mechanics.dice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a damage roll in D&D 5e.
 * Contains a type of damage and a dice sequence to roll.
 */
public class Damage extends Sequence {
    /**
     * The type of damage that this damage roll deals.
     */
    public enum Type {
        BLUDGEONING, PIERCING, SLASHING, FIRE, COLD, LIGHTNING, POISON, ACID,
        NECROTIC, RADIANT, FORCE, PSYCHIC, THUNDER, M_BLUDGEONING, M_PIERCING, M_SLASHING;

        public boolean isMagical() {
            return switch (ordinal()) {
                case 0, 1, 2 -> false;
                default -> true;
            };
        }
    }

    private Type type;
    // TODO: WHAT WAS THIS FOR?? I THINK FOR DAMAGE VULNERABILITY. Change to be handled by a Creature later
    private boolean twice = false;

    /**
     * Constructs a new Damage object with the given builder.
     * @param builder the builder to use
     */
    private Damage(Builder builder) {
        super(builder);
        this.type = builder.type;
    }

    /**
     * Rolls the dice sequence and returns the result.
     * @return the result of the dice roll
     */
    @Override
    public int roll() {
        // TODO: If this is for crits, then it's doing it wrong. It is adding the bonus twice, not the dice roll.
        return twice ? super.roll() + super.roll() : super.roll();
    }

    @Override
    public Set<Integer> getSides() {
        Set<Integer> sides = new HashSet<>();
        for (DiceComposite composite : getChildren()) {
            if (composite instanceof Die die) {
                sides.add(die.getSides());
            } else if (composite instanceof Sequence sequence) {
                sides.addAll(sequence.getSides());
            }
        }
        return sides;
    }

    @Override
    public int dieCount() {
        int count = 0;
        for (DiceComposite composite : getChildren()) {
            if (composite instanceof Sequence sequence)
                count += sequence.dieCount();
            else if (composite instanceof Die)
                count++;
        }
        return count;
    }

    public Type getType() {
        return type;
    }

    /**
     * Sets the damage roll to be a critical hit.
     */
    public void crit() {
        twice = true;
    }

    /**
     * Sets the damage roll to be a normal hit.
     */
    public void uncrit() {
        twice = false;
    }

    /**
     * Returns a readable string representation of the damage roll.
     * @return a string representation of the damage roll
     */
    @Override
    public String display() {
        return super.display() + " of " + type + " damage";
    }

    /**
     * Returns a new Damage object with increased dice sizes.
     * @return a new Damage object with increased dice sizes
     */
    @Override
    public Damage explode() {
        return new Builder().with(super.explode()).with(type).build();
    }

    /**
     * Compares this Damage object to another object.
     * @param other the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Damage damage) {
            return super.equals(damage) && damage.type.equals(type);
        }
        return false;
    }

    /**
     * Returns a JSON representation of the Damage object.
     * @return a JSON representation of the Damage object
     */
    @Override
    public String toString() {
        return "Damage{" +
                substring() +
                '}';
    }

    /**
     * Helper method for toString() to avoid code duplication.
     * @return a string representation of the Damage object
     */
    @Override
    protected String substring() {
        return super.substring() +
                ", type=" + type +
                ", twice=" + twice;
    }

    /**
     * Deconstructs the Damage object into a Builder object.
     * @return a Builder object with the same values as this Damage object
     */
    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct()).with(type);
    }

    public static Damage parse(String damageNotation) {
        Type type = Type.valueOf(damageNotation.substring(damageNotation.lastIndexOf(' ') + 1)
                .toUpperCase());
        String diceNotation = damageNotation.substring(0, damageNotation.lastIndexOf(' '));
        return new Builder().with(Die.Factory.parse(diceNotation)).with(type).build();
    }

    /**
     * Inner class for building Damage objects.
     * Extends the Sequence.Builder class.
     */
    public static class Builder extends Sequence.Builder {
        private Type type;

        public Builder with(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder with(List<DiceComposite> dice) {
            super.with(dice);
            return this;
        }

        @Override
        public Builder with(DiceComposite die) {
            super.with(die);
            return this;
        }

        public Builder with(int numDice, int dieType) {
            return with(numDice + "d" + dieType);
        }

        public Builder with(String diceNotation) {
            return with(Die.Factory.parse(diceNotation));
        }

        @Override
        public Damage build() {
            if (type == null)
                throw new IllegalArgumentException("Damage type must be specified");
            return new Damage(this);
        }
    }
}
