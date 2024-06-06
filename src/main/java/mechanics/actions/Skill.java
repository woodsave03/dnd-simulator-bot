package mechanics.actions;

import communication.Attribute;
import communication.AttributeType;
import communication.Source;
import game.entities.Ability;

import java.io.Serializable;

/**
 * Enumerates the skills available in the game.
 * Each skill is associated with an ability score.
 */
public enum Skill implements Serializable, AttributeType {
    ACROBATICS(Ability.Type.DEX),
    ANIMAL_HANDLING(Ability.Type.WIS),
    ARCANA(Ability.Type.INT),
    ATHLETICS(Ability.Type.STR),
    DECEPTION(Ability.Type.CHA),
    HISTORY(Ability.Type.INT),
    INSIGHT(Ability.Type.WIS),
    INTIMIDATION(Ability.Type.CHA),
    INVESTIGATION(Ability.Type.INT),
    MEDICINE(Ability.Type.WIS),
    NATURE(Ability.Type.INT),
    PERCEPTION(Ability.Type.WIS),
    PERFORMANCE(Ability.Type.CHA),
    PERSUASION(Ability.Type.CHA),
    RELIGION(Ability.Type.INT),
    SLEIGHT_OF_HAND(Ability.Type.DEX),
    STEALTH(Ability.Type.DEX),
    SURVIVAL(Ability.Type.WIS);

    private final Ability.Type ability;

    /**
     * Constructs a skill with an associated ability score.
     * @param ability the ability score associated with the skill
     */
    Skill(Ability.Type ability) {
        this.ability = ability;
    }

    /**
     * Returns the ability score associated with the skill.
     * @return the ability score associated with the skill
     */
    public Ability.Type ability() {
        return ability;
    }

    /**
     * Returns a readable string representation of the skill.
     * @return a readable string representation of the skill
     */
    public String display() {
        return name() + " (" + ability + ")";
    }

    /**
     * Retrieves the attribute from the source.
     * @param source The source (Creature) of the attribute.
     * @return The attribute (ConcreteCheck) from the source.
     */
    @Override
    public Attribute<AttributeType> retrieveFrom(Source<AttributeType> source) {
        return source.provide(this);
    }
}

