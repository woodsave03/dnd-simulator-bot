package mechanics.dice;

import mechanics.Construct;
import mechanics.Constructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Sequence extends DiceComposite implements Construct {

    protected Sequence(Builder builder) {
        setConstant(builder.constant);
        for (DiceComposite die : builder.dice) {
            add(die);
        }
    }

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

    public static Sequence compose(List<DiceComposite> dice) {
        return new Builder().with(dice).build();
    }

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
        else
            // Remove the trailing " + "
            sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Sequence{" +
                substring() +
                '}';
    }

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

    @Override
    public Builder deconstruct() {
        return new Builder().with(getChildren()).with(rollAfter());
    }
    @Override
    public Sequence explode() {
        Builder builder = new Builder();
        for (DiceComposite die : getChildren())
            builder.with(die.explode());
        return builder.with(rollAfter()).build();
    }

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
