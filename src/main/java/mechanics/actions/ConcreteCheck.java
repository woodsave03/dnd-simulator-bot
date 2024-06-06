package mechanics.actions;

import communication.Attribute;
import game.entities.Creature;
import mechanics.Construct;

/**
 * A concrete version of the Check class, containing a reference to the Creature that is making the check.
 * This allows the class to act as an Attribute of type Skill, as it has access the Creature's abilities,
 * and therefore the modifier to its own roll.
 */
public class ConcreteCheck extends Check implements Attribute<Skill>, Construct {
    private final Creature source;

    /**
     * Constructs a new ConcreteCheck with the given Builder and Creature.
     * @param builder the Builder to construct this ConcreteCheck
     * @param source the Creature that is making the check
     */
    public ConcreteCheck(Check.Builder builder, Creature source) {
        super(builder);
        this.source = source;
    }

    /**
     * Compares this Attribute to another Attribute of the same type.
     * @param other The other Attribute to compare to.
     * @return true if this Attribute is greater than the other, false otherwise.
     */
    @Override
    public boolean over(Attribute<Skill> other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns the Skill type of this Attribute.
     * @return the Skill type of this Attribute
     */
    @Override
    public Skill type() {
        return super.skill;
    }

    /**
     * Compares this ConcreteCheck to another object.
     * @param other the object to be compared.
     * @return return 0 if the objects are equal, a positive number if this object is greater,
     *  and a negative number if the other object is greater.
     */
    @Override
    public int compareTo(Object other) {
        if (other instanceof ConcreteCheck check) {
            return Integer.compare(this.getBonus(), check.getBonus());
        }
        throw new IllegalArgumentException("Cannot compare Check to " + other.getClass().getName());
    }

    /**
     * Returns the bonus of the check.
     * @return the bonus of the check
     */
    public int getBonus() {
        return resolveBonus(source);
    }

    /**
     * Inner Builder class to construct ConcreteCheck objects.
     */
    public static class Builder extends Check.Builder {
        private Creature source;

        /**
         * Associates a Creature with the ConcreteCheck.
         * @param source the Creature to associate with the ConcreteCheck
         * @return this Builder as per the Builder pattern
         */
        public Builder with(Creature source) {
            this.source = source;
            return this;
        }

        /**
         * Constructs a new ConcreteCheck with the given Builder.
         * @return a new ConcreteCheck with the given Builder
         */
        @Override
        public ConcreteCheck build() {
            super.with(Roll.Type.CHECK);
            return new ConcreteCheck(this, source);
        }

        /**
         * Overrides the with method of the parent class to associate a Skill with the Check.
         * @param skill the Skill to associate with this Check
         * @return this Builder as per the Builder pattern
         */
        @Override
        public Builder with(Skill skill) {
            super.with(skill);
            return this;
        }
    }
}
