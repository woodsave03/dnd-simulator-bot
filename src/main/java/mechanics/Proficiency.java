package mechanics;

/**
 * Proficiency enum for Skills, Saving Throws, and Tools.
 * NONE, PROFICIENT, and EXPERTISE are the three levels of proficiency.
 *
 * @see mechanics.actions.Skill;
 * @see mechanics.actions.Save;
 * @see game.items.Weapon.Group;
 */
public enum Proficiency {
    NONE, PROFICIENT, EXPERTISE;

    /**
     * Returns the respective bonus given the Creature's proficiency bonus.
     *
     * @param proficiency the Creature's proficiency bonus
     * @return the bonus for the proficiency level
     */
    public int bonus(int proficiency) {
        return switch (this) {
            case NONE -> 0;
            case PROFICIENT -> proficiency;
            case EXPERTISE -> proficiency * 2;
        };
    }

    /**
     * Static method to return the respective bonus given the Creature's proficiency bonus.
     *
     * @param proficiency the Creature's proficiency type
     * @param proficiencyBonus the Creature's proficiency bonus
     * @return the bonus for the proficiency level
     */
    public static int bonus(Proficiency proficiency, int proficiencyBonus) {
        if (proficiency == null) {
            return 0;
        }
        return switch (proficiency) {
            case NONE -> 0;
            case PROFICIENT -> proficiencyBonus;
            case EXPERTISE -> proficiencyBonus * 2;
        };
    }
}
