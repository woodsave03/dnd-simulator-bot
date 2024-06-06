package mechanics.dice;

import java.util.HashMap;
import java.util.Map;

/**
 * A die with a specified number of sides.
 */
public class Die extends DiceComposite {
    private int sides;

    /**
     * Default constructor for a 20-sided die.
     * @see #Die(int)
     */
    public Die() {
        this(20);
    }

    /**
     * Constructor for a die with a specified number of sides.
     * @param sides the number of sides on the die
     */
    private Die(int sides) {
        this.sides = sides;
    }

    /**
     * Get the type of the die.
     * @return the type of the die
     */
    public Type type() {
        return switch (sides) {
            case 4 -> Type.d4;
            case 6 -> Type.d6;
            case 8 -> Type.d8;
            case 10 -> Type.d10;
            case 12 -> Type.d12;
            case 20 -> Type.d20;
            default -> throw new IllegalArgumentException("Invalid die type");
        };
    }

    /**
     * Explode the die into a die with a type one higher.
     * @return a die with a type one higher
     */
    @Override
    public Die explode() {
        return Factory.weigh(Type.explode(type()));
    }

    /**
     * Roll the die.
     * @return a random number between 1 and the number of sides
     */
    @Override
    public int roll() {
        return (int) (Math.random() * sides) + 1;
    }

    /**
     * Display the die in a readable format.
     * @return the die in a readable format
     */
    @Override
    public String display() {
        return "1d" + sides;
    }

    /**
     * Getter for the number of sides on the die.
     * @return the number of sides on the die
     */
    public int getSides() {
        return sides;
    }

    /**
     * Setter for the number of sides on the die.
     * @param sides the number of sides on the die
     */
    public void setSides(int sides) {
        this.sides = sides;
    }

    /**
     * Convert the die to JSON format.
     * @return
     */
    @Override
    public String toString() {
        return "Die{" +
                "sides=" + sides +
                '}';
    }

    /**
     * Check if the die is equal to another object.
     * @param other the object to compare
     * @return true if the die is equal to the other object
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Die die)
            return sides == die.sides;
        return false;
    }

    /**
     * The types of dice available.
     */
    public enum Type {
        d20(20), d12(12), d10(10), d8(8), d6(6), d4(4);

        private int sides;

        Type(int sides) {
            this.sides = sides;
        }

        public int getSides() {
            return sides;
        }

        public static int sides(Type die) {
            return switch (die) {
                case d4 -> 4;
                case d6 -> 6;
                case d8 -> 8;
                case d10 -> 10;
                case d12 -> 12;
                case d20 -> 20;
            };
        }

        /**
         * Get the next die type higher than the given type.
         * @param lower the die type to get the next type higher than
         * @return the die type higher than the lower
         */
        public static Type explode(Type lower) {
            // return the die type higher than the lower
            if (lower == d20)
                throw new IllegalArgumentException("No die type above a d20.");
            int currIndex = lower.ordinal();
            return values()[--currIndex];
        }
    }

    /**
     * Flyweight factory class for creating dice.
     */
    public static class Factory {
        private static Map<Type, Die> dice = new HashMap<>();

        /**
         * Parse a dice notation string into a DiceComposite object.
         * @param notation the dice notation string
         * @return the DiceComposite object
         */
        public static DiceComposite parse(String notation) {
            notation = notation.toLowerCase().trim();

            String[] parts = notation.split("\\s+\\+\\s+");
            if (parts.length == 1) {
                String[] subparts = parts[0].split("d");
                if (subparts.length == 1)
                    return new Constant(Integer.parseInt(parts[0]));
                else if (subparts[0].equals("1")) {
                    return weigh(Type.valueOf("d" + subparts[1]));
                } else {
                    int count = Integer.parseInt(subparts[0]);
                    Sequence.Builder sequence = new Sequence.Builder();
                    for (int i = 0; i < count; i++) {
                        sequence.with(weigh(Type.valueOf("d" + subparts[1])));
                    }
                    return sequence.build();
                }
            } else {
                Sequence.Builder sequence = new Sequence.Builder();
                for (String part : parts) {
                    sequence.with(parse(part));
                }
                return sequence.build();
            }
        }

        /**
         * Get a die of the specified type.
         * @param type the type of die to get
         * @return the die of the specified type
         */
        private static Die weigh(Type type) {
            Die die = dice.get(type);
            if (die == null) {
                die = new Die(Type.sides(type));
                dice.put(type, die);
            }
            return die;
        }
        public static Die d4() {
            return weigh(Type.d4);
        }
        public static Die d6() {
            return weigh(Type.d6);
        }
        public static Die d8() {
            return weigh(Type.d8);
        }
        public static Die d10() {
            return weigh(Type.d10);
        }
        public static Die d12() {
            return weigh(Type.d12);
        }
        public static Die d20() {
            return weigh(Type.d20);
        }
    }
}
