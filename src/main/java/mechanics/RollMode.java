package mechanics;

/**
 * RollMode enum for rolling dice.
 * STRAIGHT, ADVANTAGE, and DISADVANTAGE are the three modes of rolling.
 *
 * @see mechanics.actions.Roll;
 */
public enum RollMode {
    STRAIGHT, ADVANTAGE, DISADVANTAGE;

    /**
     * Returns the calculated RollMode when combining two RollModes.
     * @param r the RollMode to combine with this RollMode
     * @return the calculated RollMode
     */
    public RollMode with(RollMode r) {
        return switch(this) {
            case STRAIGHT -> r;
            case ADVANTAGE -> (r == DISADVANTAGE) ? STRAIGHT : ADVANTAGE;
            case DISADVANTAGE -> (r == ADVANTAGE) ? STRAIGHT : DISADVANTAGE;
        };
    }
}