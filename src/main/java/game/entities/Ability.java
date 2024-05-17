package game.entities;

import communication.Attribute;
import communication.AttributeType;
import communication.Source;

public class Ability extends Attribute<Ability.Type> {
    private int value;
    private int modifier;
    public Ability(Type type, int value) {
        super(type);
        this.value = value;
        this.modifier = (value - 10) / 2;
    }

    public int score() {
        return value;
    }

    public int modifier() {
        return modifier;
    }
    @Override
    public int compareTo(Object o) {
        if (o instanceof Ability ability) {
            return value - ability.value;
        } else
            throw new IllegalArgumentException("Cannot compare Ability to " + o.getClass().getName());
    }

    public enum Type implements AttributeType<Ability> {
        STR, DEX, CON, INT, WIS, CHA;

        public static Type parse(String s) {
            return switch (s.toUpperCase()) {
                case "STR", "STRENGTH" -> STR;
                case "DEX", "DEXTERITY" -> DEX;
                case "CON", "CONSTITUTION" -> CON;
                case "INT", "INTELLIGENCE" -> INT;
                case "WIS", "WISDOM" -> WIS;
                case "CHA", "CHARISMA" -> CHA;
                default -> throw new IllegalArgumentException("Invalid ability: " + s);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case STR -> "Strength";
                case DEX -> "Dexterity";
                case CON -> "Constitution";
                case INT -> "Intelligence";
                case WIS -> "Wisdom";
                case CHA -> "Charisma";
            };
        }

        @Override
        public Ability retrieve(Source<AttributeType> source) {
            return (Ability) source.provide(this);
        }
    }
}

