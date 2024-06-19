package mechanics.actions;

import game.entities.Creature;

import java.util.HashMap;

/**
 * A Check is a Roll that is associated with a Skill.
 * It is used to determine the success of a Creature's attempt to perform a task.
 * Success or failure is determined by the result of the Roll, which is combined with
 * the Creature's respective ability modifier and proficiency bonus against a given Difficulty Class.
 */
public class Check extends Roll {
    // The Skill associated with this Check
    protected Skill skill;

    /**
     * Constructs a new Check with the given Builder.
     * @param builder the Builder to construct this Check
     */
    protected Check(Builder builder) {
        super(builder);
        this.skill = builder.skill;
    }

    /**
     * Returns the Skill associated with this Check.
     * @return the Skill associated with this Check
     */
    public Skill getSkill() { return skill; }

    /**
     * Sets the Skill associated with this Check.
     * @param skill the Skill to associate with this Check
     */
    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    /**
     * Deconstructs the Check into a Builder object.
     * @return a Builder object with the same parameters as the Check
     */
    @Override
    public Builder deconstruct() {
        return ((Check.Builder) super.deconstruct()).with(skill);
    }

    /**
     * A helper method used to resolve the bonus for this Check given a Creature.
     * The bonus is calculated by adding the Creature's respective ability modifier,
     * as well as their proficiency bonus if they are proficient in the associated Skill.
     * @param creature The relevant creature object
     * @return the bonus for this Check
     */
    @Override
    protected int resolveBonus(Creature creature) {
        return creature.abilities().provide(skill.ability()).modifier() + creature.getProficiency(this);
    }

    /**
     * Returns a readable String representation of this Check.
     * @return a readable String representation of this Check
     */
    @Override
    public String display() {
        return "Check" + subDisplay();
    }

    @Override
    public String subDisplay() {
        return ": " + skill.display();
    }

    /**
     * Returns a JSON representation of this Check.
     * @return a JSON representation of this Check
     */
    @Override
    public String toString() {
        return "Check{" + substring() + "}";
    }

    /**
     * Helper method to return a substring of the JSON representation of this Check,
     * made to avoid repeat code among child classes of Roll.
     * @return a substring of the JSON representation of this Check
     */
    @Override
    public String substring() {
        return super.substring() + ", skill=" + skill;
    }

    /**
     * Compares this Check to another object
     * @param other The object to compare to the roll
     * @return true if the other object is a Check with the same skill, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Check check) {
            return super.equals(other) &&
                    skill == check.skill;
        }
        return false;
    }

    /**
     * A factory class to create Check objects.
     */
    public static class Factory {
        private static final HashMap<Skill, Check> checks = new HashMap<>();
        public static Check create(Skill skill) {
            Check check = checks.get(skill);
            if (check == null) {
                check = new Check.Builder().with(skill).build();
                checks.put(skill, check);
            }
            return check;
        }
    }

    /**
     * A Builder class to construct Check objects.
     */
    public static class Builder extends Roll.Builder {
        // The Skill associated with this Check
        private Skill skill;

        /**
         * Takes a Skill and associates it with this Check.
         * @param skill the Skill to associate with this Check
         * @return this Builder as per the Builder pattern
         */
        public Builder with(Skill skill) {
            this.skill = skill;
            super.with(skill.ability());
            return this;
        }

        /**
         * Constructs a new Check with the given Builder.
         * @return a new Check with the given Builder
         */
        @Override
        public Check build() {
            super.with(Roll.Type.CHECK);
            return new Check(this);
        }
    }
}
