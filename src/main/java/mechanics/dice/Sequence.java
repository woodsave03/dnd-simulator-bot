package mechanics.dice;

import mechanics.Construct;
import mechanics.Constructor;

import java.util.LinkedList;
import java.util.List;

/**
 * A Sequence is a collection of Dice and Constants that are rolled together.
 */
public class Sequence extends DiceComposite implements Construct {
    /**
     * Create a new Sequence with the given Dice and Constants from the Builder.
     * @param builder the Builder to construct the Sequence
     */
    protected Sequence(Builder builder) {
        setConstant(builder.constant);
        for (DiceComposite die : builder.dice) {
            add(die);
        }
    }

    /**
     * Copy the Sequence and its children.
     * @return a new Sequence with the same children as this Sequence
     */
    public Sequence copy() {
        Builder builder = new Builder();
        for (DiceComposite die : getChildren()) {
            if (die instanceof Sequence sequence)
                builder.with(sequence.copy());
            else if (die instanceof Constant constant)
                builder.with(constant.rollAfter());
            else
                builder.with(die);
        }
        return builder.build();
    }

    /**
     * Compose a new Sequence from the given Dice and Constants.
     * @param dice the Dice and Constants to compose
     * @return a new Sequence with the given Dice and Constants
     */
    public static Sequence compose(List<DiceComposite> dice) {
        return new Builder().with(dice).build();
    }

    /**
     * Convert the Sequence to a readable format.
     * @return a String representation of the Sequence
     */
    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        // Allocate room for the six Die types
        int[] counts = new int[Die.Type.values().length];
        // Count the number of each Die type
        for (DiceComposite dc : getChildren()) {
            if (dc instanceof Die die)
                counts[die.type().ordinal()]++;
        }
        // Append the number of each Die type
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0)
                sb.append(counts[i]).append(Die.Type.values()[i]).append(" + ");
        }
        // Append the constant if it exists
        if (rollAfter() > 0)
            sb.append(rollAfter());
        else if (!sb.isEmpty())
            // Remove the trailing " + "
            sb.delete(sb.length() - 3, sb.length());
        else
            sb.append("0");
        return sb.toString();
    }

    /**
     * Convert the Sequence into JSON format.
     * @return a JSON representation of the Sequence
     */
    @Override
    public String toString() {
        return "Sequence{" +
                substring() +
                '}';
    }

    /**
     * Check if the Sequence is equal to another Object.
     * @param other the Object to compare
     * @return true if the Object is a Sequence with the same children, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Sequence sequence) {
            List<DiceComposite> otherDice = sequence.copy().getChildren();
            for (DiceComposite die : getChildren()) {
                if (!otherDice.remove(die))
                    return false;
            }
            return otherDice.isEmpty();
        }
        return false;
    }

    /**
     * Deconstruct the Sequence into a Builder.
     * @return a Builder with the children and constant of the Sequence
     */
    @Override
    public Builder deconstruct() {
        return new Builder().with(getChildren()).with(rollAfter());
    }

    /**
     * Explode the Sequence into a new Sequence with larger Dice.
     * @return a new Sequence with larger Dice
     */
    @Override
    public Sequence explode() {
        Builder builder = new Builder();
        for (DiceComposite die : getChildren())
            builder.with(die.explode());
        return builder.with(rollAfter()).build();
    }

    /**
     * A Builder for constructing a Sequence.
     */
    public static class Builder implements Constructor {
        private List<DiceComposite> dice = new LinkedList<>();
        private int constant = 0;

        public Builder with(List<DiceComposite> dice) {
            for (DiceComposite die : dice) {
                with(die);
            }
            return this;
        }
        public Builder with(DiceComposite die) {
            if (die instanceof Constant constant) {
                this.constant += constant.rollAfter();
                return this;
            }
            if (die.getClass() == Sequence.class) {
                for (DiceComposite child : die.getChildren()) {
                    with(child);
                }
                return this;
            }
            this.dice.add(die);
            return this;
        }
        public Builder with(int constant) {
            this.constant += constant;
            return this;
        }
        @Override
        public Sequence build() {
            return new Sequence(this);
        }
    }
}
