package mechanics.actions;

import game.entities.Creature;
import mechanics.RollMode;

import java.util.HashSet;
import java.util.Set;

/**
 * A Contest is a Roll that compares the source creature's skill check against the
 * target creature's skill check. The target creature may be able to choose between multiple
 * skills to use in the contest.
 */
public class Contest extends Roll {
    // The skill that the source creature is using to make the check
    private Skill sourceSkill;
    // The skills that the target creature may use to make the check
    private Set<Skill> targetOptions;

    /**
     * Constructs a new Contest with the given parameters
     * @param builder the builder object containing the parameters for the Contest
     */
    private Contest(Builder builder) {
        super(builder);
        this.sourceSkill = builder.sourceCheck;
        this.targetOptions = builder.targetOptions;
    }

    /**
     * Deconstructs the Contest into a Builder object
     * @return a Builder object with the same parameters as the Contest
     */
    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct())
                .with(sourceSkill)
                .against(targetOptions);
    }

    /**
     * Executes the Contest by rolling the source creature's skill check and adding the bonus
     * @param source the creature making the check
     * @param mode the mode to roll with
     * @return the result of the roll
     */
    @Override
    public int execute(Creature source, RollMode mode) {
        return rollWith(mode) + resolveBonus(source);
    }

    /**
     * Resolves the bonus for the Contest by adding the source creature's proficiency bonus
     * and the source creature's ability modifier for the skill
     * @param source the creature making the check
     * @return the bonus for the Contest
     */
    @Override
    protected int resolveBonus(Creature source) {
        return source.getProficiency(new Check.Builder().with(sourceSkill).build()) +
                source.abilities().provide(sourceSkill.ability()).modifier();
    }

    /**
     * Returns a readable string representation of the Contest
     * @return a string representation of the Contest
     */
    @Override
    public String display() {
        StringBuilder builder = new StringBuilder("Contest: ").append(sourceSkill).append(" vs (");
        for (Skill skill : targetOptions) {
            builder.append(skill).append(" or ");
        }
        return builder.substring(0, builder.length() - 4) + ")";
    }

    /**
     * Returns a JSON string representation of the Contest
     * @return a JSON string representation of the Contest
     */
    @Override
    public String toString() {
        return "Contest{" + substring() + "}";
    }

    /**
     * Helper method for toString() to return a substring of the Contest
     * @return a substring of the Contest
     */
    @Override
    public String substring() {
        return super.substring() + ", sourceSkill=" + sourceSkill + ", targetOptions=" + targetOptions;
    }

    /**
     * Returns the skill that the source creature is using to make the check
     * @return the skill that the source creature is using to make the check
     */
    public Skill getSourceSkill() {
        return sourceSkill;
    }

    /**
     * Sets the skill that the source creature is using to make the check to the given skill
     * @param sourceSkill the skill to set for the source creature
     */
    public void setSourceSkill(Skill sourceSkill) {
        this.sourceSkill = sourceSkill;
    }

    /**
     * Returns the skills that the target creature may use to make the check
     * @return the skills that the target creature may use to make the check
     */
    public Set<Skill> getTargetOptions() {
        return targetOptions;
    }

    /**
     * Sets the skills that the target creature may use to make the check to the given skills
     * @param targetOptions the skills to set for the target creature
     */
    public void setTargetOptions(Set<Skill> targetOptions) {
        this.targetOptions = targetOptions;
    }

    /**
     * Determines if the given object is equal to the Contest
     * @param other the object to compare to the Contest
     * @return a boolean indicating if the object is equal to the Contest
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Contest contest) {
            return super.equals(other) &&
                    sourceSkill == contest.sourceSkill &&
                    targetOptions.equals(contest.targetOptions);
        }
        return false;
    }

    /**
     * Builder class for a Contest
     */
    public static class Builder extends Roll.Builder {
        private Skill sourceCheck;
        private Set<Skill> targetOptions = new HashSet<>();

        /**
         * Sets the source creature's skill check to the given skill
         * @param sourceCheck
         * @return
         */
        public Builder with(Skill sourceCheck) {
            this.sourceCheck = sourceCheck;
            super.with(sourceCheck.ability());
            return this;
        }

        /**
         * Adds the given skill to the set of skills that the target creature may use to make the check
         * @param targetOptions the skill to add to the set
         * @return the builder object
         */
        public Builder against(Set<Skill> targetOptions) {
            for (Skill skill : targetOptions)
                against(skill);
            return this;
        }

        /**
         * Adds the given skill to the set of skills that the target creature may use to make the check
         * @param targetOption the skill to add to the set
         * @return the builder object
         */
        public Builder against(Skill targetOption) {
            this.targetOptions.add(targetOption);
            super.with(targetOption.ability());
            return this;
        }

        /**
         * Builds the Contest object
         * @return the Contest object
         */
        @Override
        public Contest build() {
            super.with(Type.CONTEST);
            return new Contest(this);
        }
    }
}
