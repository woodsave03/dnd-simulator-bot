package game.play;

import java.util.Queue;

/**
 * Class representing the different playable character classes in D&D 5e.
 */
public class Aptitude {
    private Queue<ClassFeature> features;
    public enum Type {
        ARTIFICER, BARBARIAN, BARD, CLERIC, DRUID, FIGHTER, MONK, MYSTIC,
        PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD
    }
}
