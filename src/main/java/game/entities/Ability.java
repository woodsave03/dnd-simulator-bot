package game.entities;

import communication.Attribute;
import communication.AttributeType;
import communication.Source;

public class Ability implements Attribute<Ability.Type> {
    private int value;
    private int modifier;
    private final Ability.Type type;
    public Ability(Type type, int value) {
        this.type = type;
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
    public boolean over(Attribute<Type> other) {
        return compareTo(other) > 0;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Ability ability) {
            return value - ability.value;
        } else
            throw new IllegalArgumentException("Cannot compare Ability to " + o.getClass().getName());
    }

    public enum Type implements AttributeType {
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
        public Attribute<AttributeType> retrieveFrom(Source<AttributeType> source) {
            return source.provide(this);
        }
    }
}

