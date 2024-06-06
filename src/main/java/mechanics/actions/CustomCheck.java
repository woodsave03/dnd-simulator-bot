package mechanics.actions;

import game.entities.Ability;

import java.util.Set;

/**
 * A CustomCheck is a Check that uses a specific ability for a check disregarding the skill's default ability.
 * Ex: A rope-tying check may use an Intelligence(Sleight of Hand) check instead of the default Dexterity.
 */
public class CustomCheck extends Check {
    private final Ability.Type overrideAbility;

    /**
     * Constructs a new CustomCheck with the given parameters
     * @param builder the builder object containing the parameters for the CustomCheck
     */
    public CustomCheck(Builder builder) {
        super(builder);
        this.overrideAbility = builder.overrideAbility;
    }

    /**
     * Overrides the default getAbilityOptions method to return the ability that the CustomCheck uses
     * @return a set containing the ability that the CustomCheck uses
     */
    @Override
    public Set<Ability.Type> getAbilityOptions() {
        return Set.of(overrideAbility);
    }

    /**
     * Deconstructs the CustomCheck into a Builder object
     * @return a Builder object with the CustomCheck's parameters
     */
    @Override
    public Builder deconstruct() {
        return ((Builder) super.deconstruct()).with(overrideAbility);
    }

    /**
     * A Builder class for the CustomCheck
     */
    public static class Builder extends Check.Builder {
        private Ability.Type overrideAbility;

        /**
         * Assigns the overridden ability to the CustomCheck
         * @param overrideAbility The ability to add to the roll
         * @return the Builder object with the overridden ability
         */
        public Builder with(Ability.Type overrideAbility) {
            this.overrideAbility = overrideAbility;
            return this;
        }

        /**
         * Overrides the default with method to associate a Skill with the CustomCheck
         * @param skill the Skill to associate with this Check
         * @return the CustomCheck Builder with the given Skill
         */
        @Override
        public Builder with(Skill skill) {
            super.with(skill);
            return this;
        }

        /**
         * Builds the CustomCheck
         * @return the CustomCheck with the given parameters
         */
        @Override
        public CustomCheck build() {
            return new CustomCheck(this);
        }
    }
}
