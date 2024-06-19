package game.play;


import java.util.Queue;

/**
 * Class representing the different playable character classes in D&D 5e.
 */
public class PlayerClass {
    private BaseFeature baseFeature;
    private Queue<ClassFeatureComposite> features;

    /**
     * Enumeration of the different character classes in D&D 5e.
     */
    public enum Type {
        ARTIFICER, BARBARIAN, BARD, CLERIC, DRUID, FIGHTER, MONK, MYSTIC,
        PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD
    }
}
