package mechanics;

public enum RollMode {
    STRAIGHT, ADVANTAGE, DISADVANTAGE;

    public RollMode with(RollMode r) {
        return switch(this) {
            case STRAIGHT -> r;
            case ADVANTAGE -> (r == DISADVANTAGE) ? STRAIGHT : ADVANTAGE;
            case DISADVANTAGE -> (r == ADVANTAGE) ? STRAIGHT : DISADVANTAGE;
        };
    }
}