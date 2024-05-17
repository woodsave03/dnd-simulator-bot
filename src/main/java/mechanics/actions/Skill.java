package mechanics.actions;

import game.entities.Ability;

import java.io.Serializable;

public enum Skill implements Serializable {
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

    Skill(Ability.Type ability) {
        this.ability = ability;
    }

    public Ability.Type ability() {
        return ability;
    }

    public String display() {
        return name() + " (" + ability + ")";
    }
}

