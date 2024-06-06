package game.entities;

import communication.Attribute;
import communication.AttributeType;
import communication.Source;

/**
 * Represents an ability score in D&D.
 */
public class Ability implements Attribute<Ability.Type> {
    private int value;
    private int modifier;
    private final Ability.Type type;

    /**
     * Creates an ability with the given type and value.
     * @param type The type of the ability.
     * @param value The value of the ability.
     */
    public Ability(Type type, int value) {
        this.type = type;
        this.value = value;
        this.modifier = (value - 10) / 2;
    }

    /**
     * Getter for the value of the ability.
     * @return The value of the ability.
     */
    public int score() {
        return value;
    }

    /**
     * Getter for the modifier of the ability.
     * @return The modifier of the ability.
     */
    public int modifier() {
        return modifier;
    }

    /**
     * Compares this Ability to another Attribute.
     * @param other The other Attribute to compare to.
     * @return true if this Ability is greater than the other Attribute, false otherwise.
     */
    @Override
    public boolean over(Attribute<Type> other) {
        return compareTo(other) > 0;
    }

    /**
     * Getter for the type of the ability.
     * @return The type of the ability.
     */
    @Override
    public Type type() {
        return type;
    }

    /**
     * Compares this Ability to another Attribute.
     * @param o The other Attribute to compare to.
     * @return The difference between the values of the two Attributes.
     * @throws IllegalArgumentException if the other Attribute is not an Ability.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Ability ability) {
            return value - ability.value;
        } else
            throw new IllegalArgumentException("Cannot compare Ability to " + o.getClass().getName());
    }

    /**
     * Enum representing the different types of abilities.
     */
    public enum Type implements AttributeType {
        STR, DEX, CON, INT, WIS, CHA;

        /**
         * Parses a string into an Ability.Type.
         * @param s The string to parse.
         * @return The Ability.Type represented by the string.
         * @throws IllegalArgumentException if the string does not represent a valid Ability.Type.
         */
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

        /**
         * Converts the Ability.Type to a string.
         * @return The string representation of the Ability.Type.
         */
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

        /**
         * Retrieves the Attribute from a Source.
         * @param source The Source to retrieve the Attribute from.
         * @return The Attribute retrieved from the Source.
         */
        @Override
        public Attribute<AttributeType> retrieveFrom(Source<AttributeType> source) {
            return source.provide(this);
        }
    }
}

