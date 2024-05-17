package mechanics;

public enum Proficiency {
    NONE, PROFICIENT, EXPERTISE;

    public int bonus(int proficiency) {
        return switch (this) {
            case NONE -> 0;
            case PROFICIENT -> proficiency;
            case EXPERTISE -> proficiency * 2;
        };
    }

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
