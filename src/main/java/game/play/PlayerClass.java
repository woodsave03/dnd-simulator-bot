package game.play;

import java.util.List;

import game.entities.Ability;
import mechanics.dice.Die;

/**
 * Class representing the different playable character classes in D&D 5e.
 */
public class PlayerClass {
    private Die hitDie;
    private List<ClassFeatureComposite> features;
    private final Ability.Type[] saveProficiencies = new Ability.Type[2];

    /**
     * Enumeration of the different character classes in D&D 5e.
     */
    public enum Type {
        ARTIFICER, BARBARIAN, BARD, BLOOD_HUNTER, CLERIC, DRUID, FIGHTER, MONK,
        MYSTIC, PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD
    }
}
