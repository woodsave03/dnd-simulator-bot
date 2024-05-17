package mechanics.dice;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Die extends DiceComposite {
    private int sides;

    public Die() {
        this(20);
    }

    private Die(int sides) {
        this.sides = sides;
    }

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

    @Override
    public Die explode() {
        return Factory.weigh(Type.explode(type()));
    }

    @Override
    public int roll() {
        return (int) (Math.random() * sides) + 1;
    }
    @Override
    public String display() {
        return "1d" + sides;
    }

    public int getSides() {
        return sides;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    @Override
    public String toString() {
        return "Die{" +
                "sides=" + sides +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Die die)
            return sides == die.sides;
        return false;
    }

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

        public static Type explode(Type lower) {
            // return the die type higher than the lower
            if (lower == d20)
                throw new IllegalArgumentException("No die type above a d20.");
            int currIndex = lower.ordinal();
            return values()[--currIndex];
        }
    }

    public static class Factory {
        private static Map<Type, Die> dice = new HashMap<>();

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
